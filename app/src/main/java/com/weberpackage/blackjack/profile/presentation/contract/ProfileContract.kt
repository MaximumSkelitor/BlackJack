package com.weberpackage.blackjack.profile.presentation.contract

import com.weberpackage.blackjack.common.presentation.base.ViewEvent
import com.weberpackage.blackjack.common.presentation.base.ViewSideEffect
import com.weberpackage.blackjack.common.presentation.base.ViewState
import com.weberpackage.blackjack.common.presentation.utils.UiText
import com.weberpackage.blackjack.profile.presentation.model.ProfileState


class ProfileContract {

    sealed class Event : ViewEvent {
        data object CheckTotalChips : Event()
        data object CheckHighestChips : Event()
        data class OnEquipPack(val packId: Int) : Event()
    }

    data class State(
        val profileState: ProfileState,
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