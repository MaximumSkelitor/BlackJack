package com.weberpackage.blackjack.profile.presentation.screens.achievements.contract

import com.weberpackage.blackjack.common.presentation.base.ViewEvent
import com.weberpackage.blackjack.common.presentation.base.ViewSideEffect
import com.weberpackage.blackjack.common.presentation.base.ViewState
import com.weberpackage.blackjack.common.presentation.utils.UiText
import com.weberpackage.blackjack.profile.presentation.screens.achievements.model.Achievement

class AchievementsContract {
    sealed class Event : ViewEvent {
        data object OnBackClicked : Event()
        data class OnClaimClicked(val achievement: Achievement) : Event()
    }

    data class State(
        val achievements: List<Achievement> = emptyList(),
        val isLoading: Boolean = true
    ) : ViewState

    sealed class Effect : ViewSideEffect {
        data class Notification(val text: UiText, val error: Boolean = false) : Effect()

        sealed class Navigation : Effect() {
            data object Back : Navigation()
        }
    }
}
