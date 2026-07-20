package com.weberpackage.blackjack.common.presentation.contract

import com.weberpackage.blackjack.common.presentation.base.ViewEvent
import com.weberpackage.blackjack.common.presentation.base.ViewSideEffect
import com.weberpackage.blackjack.common.presentation.base.ViewState
import com.weberpackage.blackjack.common.presentation.model.AppLanguage
import com.weberpackage.blackjack.common.presentation.theme.AppTheme
import com.weberpackage.blackjack.common.presentation.utils.UiText


class MainContract {

    sealed class Event : ViewEvent {
        data class UpdateTotalCredits(val credits: Int) : Event()
        data object RefreshChips : Event()
    }

    data class State(
        val totalChips: Int,
        val language: AppLanguage,
        val appTheme: AppTheme,
        val startDestination: Any,
    ) : ViewState

    sealed class Effect : ViewSideEffect {
        data class Notification(val text: UiText, val error: Boolean) : Effect()
        data object CheckForAppUpdates : Effect()

        sealed class Navigation : Effect() {
            data object Back : Navigation()
            data class NavRoute(
                val route: Any,
                val popUpToRoute: Any? = null,
                val inclusive: Boolean = true
            ) : Navigation()
            data class NavDest(val route: String) : Navigation()

        }
    }
}