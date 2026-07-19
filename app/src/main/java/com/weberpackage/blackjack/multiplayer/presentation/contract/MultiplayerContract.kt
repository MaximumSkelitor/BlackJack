package com.weberpackage.blackjack.multiplayer.presentation.contract

import com.weberpackage.blackjack.common.presentation.base.ViewEvent
import com.weberpackage.blackjack.common.presentation.base.ViewSideEffect
import com.weberpackage.blackjack.common.presentation.base.ViewState
import com.weberpackage.blackjack.common.presentation.utils.UiText
import com.weberpackage.blackjack.multiplayer.presentation.model.MultiState

class MultiContract {

    sealed class Event : ViewEvent {
        data object StartNewGame : Event()
        data object Hit : Event()
        data object Stand : Event()
        data object ResetGame: Event()
    }

    data class State(
        val multiState: MultiState
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