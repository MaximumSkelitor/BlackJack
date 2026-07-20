package com.weberpackage.blackjack.play_now.presentation.contract

import com.weberpackage.blackjack.common.presentation.base.ViewEvent
import com.weberpackage.blackjack.common.presentation.base.ViewSideEffect
import com.weberpackage.blackjack.common.presentation.base.ViewState
import com.weberpackage.blackjack.common.presentation.utils.UiText
import com.weberpackage.blackjack.play_now.presentation.model.PlayNowState

class PlayNowContract {

    sealed class Event : ViewEvent {
        data object CheckTotalChips : Event()
        data object StartNewGame : Event()
        data object Hit : Event()
        data object Stand : Event()
        data object ResetGame: Event()
        data class PlaceBet(val amount: Int) : Event()
        data object ShowNoCreditsAlert : Event()
        data object ShowExitDialog : Event()
    }

    data class State(
        val playNowState: PlayNowState,
    ) : ViewState

    sealed class Effect : ViewSideEffect {
        data class Notification(val text: UiText, val error: Boolean) : Effect()

        sealed class Navigation : Effect() {
            data object Back : Navigation()
            data class NavRoute(
                val route: Any,
                val popUpToRoute: Any? = null,
                val inclusive: Boolean = true
            ) : Navigation()
        }
    }
}