package com.weberpackage.blackjack.profile.presentation.screens

import androidx.lifecycle.viewModelScope
import com.weberpackage.blackjack.common.presentation.base.BaseViewModel
import com.weberpackage.blackjack.core.prefs.Pref
import com.weberpackage.blackjack.core.prefs.Prefs
import com.weberpackage.blackjack.profile.presentation.contract.ProfileContract
import com.weberpackage.blackjack.profile.presentation.model.ProfileState
import com.weberpackage.blackjack.dashboard.presentation.utils.RankUtils
import com.weberpackage.blackjack.profile.presentation.screens.achievements.utils.AchievementUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val prefs: Prefs
) : BaseViewModel<ProfileContract.Event, ProfileContract.State, ProfileContract.Effect>() {

    init {
        collectPrefsFlow()
    }

    private fun collectPrefsFlow() {
        collectAndUpdateState(Pref.username) {
            copy(
                profileState = profileState.copy(username = it)
            )
        }
        viewModelScope.launch {
            prefs.collectPrefsFlow(Pref.totalChips).collect { chips ->
                val currentHighest = prefs.get(Pref.highestChips)
                if (chips > currentHighest) {
                    prefs.set(Pref.highestChips, chips)
                }
                setState {
                    val newState = copy(
                        profileState = profileState.copy(totalChips = chips)
                    )
                    newState.updateRankInfo()
                }
            }
        }
        collectAndUpdateState(Pref.careerCredits) {
            copy(
                profileState = profileState.copy(careerCredits = it)
            )
        }
        collectAndUpdateState(Pref.highestChips) {
            copy(
                profileState = profileState.copy(highestChips = it),
            )
        }
        collectAndUpdateState(Pref.ownedPacks) {
            copy(
                profileState = profileState.copy(
                    ownedPacks = it.split(",").filter { s -> s.isNotEmpty() }.map { s -> s.toInt() }
                )
            )
        }
        collectAndUpdateState(Pref.equippedPack) {
            copy(
                profileState = profileState.copy(equippedPack = it)
            )
        }
        collectAndUpdateState(Pref.gamesPlayed) {
            val newState = copy(
                profileState = profileState.copy(gamesPlayed = it)
            )
            newState.updateRankInfo()
        }
    }

    private fun ProfileContract.State.updateRankInfo(): ProfileContract.State {
        val chips = profileState.totalChips
        val games = profileState.gamesPlayed
        val currentRank = RankUtils.getCurrentRank(chips, games)
        val nextRank = RankUtils.getNextRank(chips, games)
        val progress = nextRank?.let {
            RankUtils.getRankProgress(it, chips, games, currentRank)
        } ?: 1f

        val career = prefs.get(Pref.careerCredits)
        val claimedIds = prefs.get(Pref.claimedAchievements).split(",").filter { it.isNotEmpty() }
        val achievements = AchievementUtils.evaluateAchievements(games, profileState.highestChips.toLong(), chips.toLong(), career, claimedIds)
        val unlocked = achievements.count { it.isUnlocked }

        return copy(
            profileState = profileState.copy(
                currentRank = currentRank,
                nextRank = nextRank,
                rankProgress = progress,
                unlockedAchievements = unlocked,
                totalAchievements = achievements.size
            )
        )
    }

    override fun setInitialState(): ProfileContract.State {
        val chips = prefs.get(Pref.totalChips)
        val games = prefs.get(Pref.gamesPlayed)
        val highest = prefs.get(Pref.highestChips)
        val currentRank = RankUtils.getCurrentRank(chips, games)
        val nextRank = RankUtils.getNextRank(chips, games)
        val progress = nextRank?.let {
            RankUtils.getRankProgress(it, chips, games, currentRank)
        } ?: 1f

        val career = prefs.get(Pref.careerCredits)
        val claimedIds = prefs.get(Pref.claimedAchievements).split(",").filter { it.isNotEmpty() }
        val achievements = AchievementUtils.evaluateAchievements(games, highest.toLong(), chips.toLong(), career, claimedIds)
        val unlocked = achievements.count { it.isUnlocked }

        return ProfileContract.State(
            profileState = ProfileState(
                username = prefs.get(Pref.username),
                totalChips = chips,
                highestChips = highest,
                gamesPlayed = games,
                careerCredits = prefs.get(Pref.careerCredits),
                ownedPacks = prefs.get(Pref.ownedPacks).split(",").filter { s -> s.isNotEmpty() }.map { s -> s.toInt() },
                equippedPack = prefs.get(Pref.equippedPack),
                currentRank = currentRank,
                nextRank = nextRank,
                rankProgress = progress,
                unlockedAchievements = unlocked,
                totalAchievements = achievements.size
            ),
            isInitialLoading = false,
        )
    }

    override fun handleEvents(event: ProfileContract.Event) {
        when (event) {
            is ProfileContract.Event.CheckTotalChips -> {}
            is ProfileContract.Event.CheckHighestChips -> {}
            is ProfileContract.Event.OnEquipPack -> prefs.set(Pref.equippedPack, event.packId)
        }
    }

    private fun <T> collectAndUpdateState(
        pref: Pref<T>,
        update: ProfileContract.State.(T) -> ProfileContract.State
    ) {
        viewModelScope.launch {
            prefs.collectPrefsFlow(pref).collect { value ->
                setState { update(value) }
            }
        }
    }
}
