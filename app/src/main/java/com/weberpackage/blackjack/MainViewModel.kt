package com.weberpackage.blackjack

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.viewModelScope
import com.weberpackage.blackjack.common.presentation.base.BaseViewModel
import com.weberpackage.blackjack.common.presentation.contract.MainContract
import com.weberpackage.blackjack.common.presentation.model.AppLanguage
import com.weberpackage.blackjack.common.presentation.navigation.NavRoutes
import com.weberpackage.blackjack.common.presentation.theme.AppTheme
import com.weberpackage.blackjack.core.prefs.Pref
import com.weberpackage.blackjack.core.prefs.Prefs
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
        collectAndUpdateState(Pref.totalChips) {
            copy(
                totalChips = it
            )
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
