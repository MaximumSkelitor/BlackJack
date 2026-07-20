package com.weberpackage.blackjack.betting.presentation.contract

import com.weberpackage.blackjack.betting.presentation.model.BetState
import com.weberpackage.blackjack.common.presentation.base.ViewEvent
import com.weberpackage.blackjack.common.presentation.base.ViewSideEffect
import com.weberpackage.blackjack.common.presentation.base.ViewState
import com.weberpackage.blackjack.common.presentation.utils.UiText

class BetContract {

    sealed class Event : ViewEvent {
        data object CheckTotalChips : Event()
        data class OnUpdateCredits(val amount: Int) : Event()
        data class OnAdjustBet(val amount: Int) : Event()
        data class OnSelectCustomBet(val amount: Int) : Event()
        data object OnConfirmBet : Event()
        data class OnCustomBetChange(val amount: Int) : Event()
    }

    data class State(
        val betState: BetState,
        val isInitialLoading: Boolean,
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