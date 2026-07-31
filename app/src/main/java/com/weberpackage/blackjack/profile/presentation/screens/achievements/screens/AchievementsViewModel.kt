package com.weberpackage.blackjack.profile.presentation.screens.achievements

import androidx.lifecycle.viewModelScope
import com.weberpackage.blackjack.common.presentation.base.BaseViewModel
import com.weberpackage.blackjack.core.prefs.Pref
import com.weberpackage.blackjack.core.prefs.Prefs
import com.weberpackage.blackjack.profile.presentation.screens.achievements.contract.AchievementsContract
import com.weberpackage.blackjack.profile.presentation.screens.achievements.model.Achievement
import com.weberpackage.blackjack.profile.presentation.screens.achievements.utils.AchievementUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AchievementsViewModel @Inject constructor(
    private val prefs: Prefs
) : BaseViewModel<AchievementsContract.Event, AchievementsContract.State, AchievementsContract.Effect>() {

    init {
        collectStats()
    }

    override fun setInitialState() = AchievementsContract.State()

    override fun handleEvents(event: AchievementsContract.Event) {
        when (event) {
            is AchievementsContract.Event.OnBackClicked -> setEffect { AchievementsContract.Effect.Navigation.Back }
            is AchievementsContract.Event.OnClaimClicked -> claimAchievement(event.achievement)
        }
    }

    private fun claimAchievement(achievement: Achievement) {
        if (!achievement.isUnlocked || achievement.isClaimed) return

        viewModelScope.launch {
            val claimed = prefs.get(Pref.claimedAchievements)
                .split(",")
                .filter { it.isNotEmpty() }
                .toMutableList()
            
            if (!claimed.contains(achievement.id)) {
                claimed.add(achievement.id)
                prefs.set(Pref.claimedAchievements, claimed.joinToString(","))
                
                achievement.rewardPackId?.let { packId ->
                    val owned = prefs.get(Pref.ownedPacks)
                        .split(",")
                        .filter { it.isNotEmpty() }
                        .toMutableList()
                    
                    if (!owned.contains(packId.toString())) {
                        owned.add(packId.toString())
                        prefs.set(Pref.ownedPacks, owned.joinToString(","))
                    }
                }

                achievement.rewardCredits?.let { credits ->
                    val currentChips = prefs.get(Pref.totalChips)
                    prefs.set(Pref.totalChips, currentChips + credits)
                }
            }
        }
    }

    private fun collectStats() {
        viewModelScope.launch {
            combine(
                prefs.collectPrefsFlow(Pref.gamesPlayed),
                prefs.collectPrefsFlow(Pref.highestChips),
                prefs.collectPrefsFlow(Pref.totalChips),
                prefs.collectPrefsFlow(Pref.careerCredits),
                prefs.collectPrefsFlow(Pref.claimedAchievements)
            ) { games, highest, total, career, claimedStr ->
                val claimedIds = claimedStr.split(",").filter { it.isNotEmpty() }
                AchievementUtils.evaluateAchievements(games, highest.toLong(), total.toLong(), career, claimedIds)
            }.collect { achievements ->
                setState {
                    copy(
                        achievements = achievements,
                        isLoading = false
                    )
                }
            }
        }
    }
}
