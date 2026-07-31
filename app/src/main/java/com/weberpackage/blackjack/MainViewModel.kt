package com.weberpackage.blackjack

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.viewModelScope
import com.weberpackage.blackjack.common.presentation.base.BaseViewModel
import com.weberpackage.blackjack.common.presentation.contract.MainContract
import com.weberpackage.blackjack.common.presentation.model.AppLanguage
import com.weberpackage.blackjack.common.presentation.navigation.NavRoutes
import com.weberpackage.blackjack.common.presentation.theme.AppTheme
import com.weberpackage.blackjack.common.presentation.utils.UiText
import com.weberpackage.blackjack.common.presentation.utils.stringResArg
import com.weberpackage.blackjack.common.presentation.utils.uiTextArgsOf
import com.weberpackage.blackjack.core.prefs.Pref
import com.weberpackage.blackjack.core.prefs.Prefs
import com.weberpackage.blackjack.profile.presentation.screens.achievements.utils.AchievementUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val prefs: Prefs
) : BaseViewModel<MainContract.Event, MainContract.State, MainContract.Effect>() {

    init {
        collectPrefsFlow()
        resetCustomBet()
        checkForAppUpdates()
    }

    override fun setInitialState() = MainContract.State(
        language = AppLanguage.ENGLISH,
        appTheme = AppTheme.SYSTEM,
        totalChips = 0,
        startDestination = getStartDestination()
    )

    override fun handleEvents(event: MainContract.Event) {
        when (event) {
            is MainContract.Event.UpdateTotalCredits -> addCredits(event.credits)
            is MainContract.Event.RefreshChips -> refreshCredits()
        }
    }

    private fun collectPrefsFlow() {
        viewModelScope.launch {
            var previousChips = prefs.get(Pref.totalChips)
            
            // Sync career credits with highest reached if needed (one-time fix)
            val currentBest = prefs.get(Pref.highestChips)
            val currentCareer = prefs.get(Pref.careerCredits)
            if (currentCareer < currentBest) {
                prefs.set(Pref.careerCredits, currentBest.toLong())
            }

            prefs.collectPrefsFlow(Pref.totalChips).collect { chips ->
                val diff = chips - previousChips
                if (diff > 0) {
                    val career = prefs.get(Pref.careerCredits)
                    prefs.set(Pref.careerCredits, career + diff)
                }
                previousChips = chips

                setState {
                    copy(totalChips = chips)
                }
            }
        }

        viewModelScope.launch {
            var previousUnlockedIds = emptySet<String>()
            val statFlows = listOf(
                prefs.collectPrefsFlow(Pref.gamesPlayed),
                prefs.collectPrefsFlow(Pref.highestChips),
                prefs.collectPrefsFlow(Pref.totalChips),
                prefs.collectPrefsFlow(Pref.careerCredits),
                prefs.collectPrefsFlow(Pref.claimedAchievements),
                prefs.collectPrefsFlow(Pref.notifiedAchievements)
            )

            kotlinx.coroutines.flow.combine(statFlows) { args: Array<Any?> ->
                val games = args[0] as Long
                val highest = (args[1] as Int).toLong()
                val total = (args[2] as Int).toLong()
                val career = args[3] as Long
                val claimedStr = args[4] as String
                val notifiedStr = args[5] as String

                val claimedIds = claimedStr.split(",").filter { it.isNotEmpty() }
                val notifiedIds = notifiedStr.split(",").filter { it.isNotEmpty() }.toSet()

                val achievements = AchievementUtils.evaluateAchievements(games, highest, total, career, claimedIds)
                val currentUnlockedIds = achievements.filter { it.isUnlocked }.map { it.id }.toSet()
                val newUnlocks = currentUnlockedIds - previousUnlockedIds

                if (previousUnlockedIds.isNotEmpty() && newUnlocks.isNotEmpty()) {
                    val updatedNotified = (notifiedIds + newUnlocks).joinToString(",")
                    prefs.set(Pref.notifiedAchievements, updatedNotified)

                    newUnlocks.forEach { id ->
                        achievements.find { it.id == id }?.let { achievement ->
                            setEffect {
                                MainContract.Effect.Notification(
                                    text = UiText(
                                        resId = R.string.achievement_unlocked_alert,
                                        args = uiTextArgsOf(stringResArg(achievement.titleRes))
                                    ),
                                    error = false
                                )
                            }
                        }
                    }
                }
                previousUnlockedIds = currentUnlockedIds
            }.collect {}
        }

        collectAndUpdateState(Pref.setLanguage) {
            val appLanguage = AppLanguage.from(it)
            setCurrentLocales(languageTag = appLanguage.code)
            copy(
                language = appLanguage,
            )
        }
        collectAndUpdateState(Pref.appTheme) {
            copy(
                appTheme = AppTheme.fromName(it),
            )
        }
    }

    private fun getStartDestination(): Any {
        val hasSetUsername = prefs.get(Pref.username).isNotBlank()
        return when (hasSetUsername) {
            true -> NavRoutes.HomeGraph
            false -> NavRoutes.SettingsDest.Username(firstTimeSetup = true)
        }
    }

    private fun setCurrentLocales(languageTag: String) {
        val currentLocales = AppCompatDelegate.getApplicationLocales()

        if (currentLocales.isEmpty || currentLocales.get(0)?.language != languageTag) {
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(languageTag))
        }
    }

    private fun addCredits(amount: Int) {
        val newTotal = viewState.value.totalChips + amount
        prefs.set(Pref.totalChips, newTotal)
        
        val currentHigh = prefs.get(Pref.highestChips)
        if (newTotal > currentHigh) {
            prefs.set(Pref.highestChips, newTotal)
        }

        setState { copy(totalChips = newTotal) }
    }

    private fun refreshCredits() {
        val credits = prefs.get(Pref.totalChips)
        setState {
            copy(totalChips = credits)
        }
    }

    private fun resetCustomBet() {
        prefs.set(Pref.customBet, 100)
    }

    private fun checkForAppUpdates() {
        setEffect {
            MainContract.Effect.CheckForAppUpdates
        }
    }

    private fun <T> collectAndUpdateState(
        pref: Pref<T>,
        update: MainContract.State.(T) -> MainContract.State
    ) {
        viewModelScope.launch {
            prefs.collectPrefsFlow(pref).collect { value ->
                setState { update(value) }
            }
        }
    }
}
