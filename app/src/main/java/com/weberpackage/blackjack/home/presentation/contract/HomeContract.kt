package com.weberpackage.blackjack.home.presentation.contract

import android.content.Intent
import com.weberpackage.blackjack.common.presentation.base.ViewEvent
import com.weberpackage.blackjack.common.presentation.base.ViewSideEffect
import com.weberpackage.blackjack.common.presentation.base.ViewState
import com.weberpackage.blackjack.common.presentation.utils.UiText

class HomeContract {

    sealed class Event : ViewEvent

    data class State(
        val totalChips: Int?,
        val showBottomBar: Boolean,
        val shopNotification: String?,
        val isInitialLoading: Boolean,
        val isDataError: Boolean
    ) : ViewState

    sealed class Effect : ViewSideEffect {
        data class Notification(val text: UiText, val error: Boolean) : Effect()
        data class OpenUpdateIntent(val intent: Intent) : Effect()

        sealed class Navigation : Effect() {
            data object Back : Navigation()
            data object Changelog : Navigation()
            data class NavRoute(
                val route: Any,
                val popUpToRoute: Any? = null,
                val inclusive: Boolean = true
            ) : Navigation()
        }
    }
}