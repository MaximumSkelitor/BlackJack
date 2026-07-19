package com.weberpackage.blackjack.settings.presentation.contract

import com.weberpackage.blackjack.common.presentation.base.ViewEvent
import com.weberpackage.blackjack.common.presentation.base.ViewSideEffect
import com.weberpackage.blackjack.common.presentation.base.ViewState
import com.weberpackage.blackjack.common.presentation.model.AppLanguage
import com.weberpackage.blackjack.common.presentation.theme.AppTheme
import com.weberpackage.blackjack.common.presentation.utils.UiText

class SettingsContract {

    sealed class Event : ViewEvent {
        data object ShowAppInfo : Event()
        data class OnUsernameEdit(val username: String) : Event()
        data object OnSaveUsername : Event()
        data object OnSetUsername : Event()
        data class OnAppThemeSave(val appTheme: AppTheme) : Event()
        data class OnSetLanguage(val language: AppLanguage) : Event()
        data class OnSelectCredits(val selected: Boolean) : Event()
    }

    data class State(
        val username: String,
        val language: AppLanguage,
        val appTheme: AppTheme,
        val creditsSelected: Boolean,
        val isInitialLoading: Boolean,
    ) : ViewState

    sealed class Effect : ViewSideEffect {
        data class Notification(val text: UiText, val error: Boolean) : Effect()

        sealed class Navigation : Effect() {
            data object Back : Navigation()
            data class NavRoute(val route: Any, val popUp: Boolean = false) : Navigation()
            data class NavDest(val route: String) : Navigation()
        }
    }
}