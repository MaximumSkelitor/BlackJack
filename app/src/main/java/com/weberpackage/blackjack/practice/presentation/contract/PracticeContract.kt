package com.weberpackage.blackjack.practice.presentation.contract

import com.weberpackage.blackjack.common.presentation.base.ViewEvent
import com.weberpackage.blackjack.common.presentation.base.ViewSideEffect
import com.weberpackage.blackjack.common.presentation.base.ViewState
import com.weberpackage.blackjack.common.presentation.model.PlayCard
import com.weberpackage.blackjack.common.presentation.utils.UiText

class PracticeContract {

    sealed class Event : ViewEvent {
        data object StartNewGame : Event()
        data object Hit : Event()
        data object Stand : Event()
        data object ResetGame: Event()
    }

    data class State(
        val equippedPack: Int,
        val playerHand: List<PlayCard>,
        val dealerHand: List<PlayCard>,
        val statusMessage: UiText,
        val isGameOver: Boolean
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