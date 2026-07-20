package com.weberpackage.blackjack.shop.presentation.contract

import com.weberpackage.blackjack.common.presentation.base.ViewEvent
import com.weberpackage.blackjack.common.presentation.base.ViewSideEffect
import com.weberpackage.blackjack.common.presentation.base.ViewState
import com.weberpackage.blackjack.common.presentation.utils.UiText

class ShopContract {

    sealed class Event : ViewEvent {
        data class OnPurchasePack(val packId: Int, val price: Int) : Event()
        data class OnUpdateCredits(val amount: Int) : Event()
        data class OnClaimDailyCredits(val credits: Int) : Event()
    }

    data class State(
        val totalChips: Int,
        val ownedPacks: List<Int>,
        val lastClaimTime: Long,
        val timeRemaining: Long,
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
            data class NavDest(val route: String) : Navigation()
        }
    }
}
