package com.weberpackage.blackjack.dashboard.presentation.screens

import androidx.lifecycle.viewModelScope
import com.weberpackage.blackjack.R
import com.weberpackage.blackjack.common.presentation.base.BaseViewModel
import com.weberpackage.blackjack.common.presentation.navigation.NavRoutes
import com.weberpackage.blackjack.common.presentation.utils.DialogAction
import com.weberpackage.blackjack.common.presentation.utils.DialogController
import com.weberpackage.blackjack.common.presentation.utils.DialogEvent
import com.weberpackage.blackjack.common.presentation.utils.UiText
import com.weberpackage.blackjack.core.prefs.Pref
import com.weberpackage.blackjack.core.prefs.Prefs
import com.weberpackage.blackjack.dashboard.presentation.contract.DashContract
import com.weberpackage.blackjack.dashboard.presentation.model.DashState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val prefs: Prefs
) : BaseViewModel<DashContract.Event, DashContract.State, DashContract.Effect>() {

    init {
        collectPrefsFlow()
    }

    override fun setInitialState() = DashContract.State(
        uiState = DashState(),
        isInitialLoading = true,
    )

    override fun handleEvents(event: DashContract.Event) {
        when (event) {
            is DashContract.Event.CheckTotalChips -> checkTotalChips()
            is DashContract.Event.PlayNowNoCredits -> showNoCreditsDialog()
        }
    }

    private fun collectPrefsFlow() {
        collectAndUpdateState(Pref.totalChips) {
            copy(
                uiState = uiState.copy(totalChips = it)
            )
        }
        collectAndUpdateState(Pref.gamesPlayed) {
            copy(
                uiState = uiState.copy(gamesPlayed = it)
            )
        }
        collectAndUpdateState(Pref.username) {
            copy(
                uiState = uiState.copy(username = it),
                isInitialLoading = false
            )
        }
        checkTotalChips()
    }

    private fun <T> collectAndUpdateState(
        pref: Pref<T>,
        update: DashContract.State.(T) -> DashContract.State
    ) {
        viewModelScope.launch {
            prefs.collectPrefsFlow(pref).collect { value ->
                setState { update(value) }
            }
        }
    }

    private fun checkTotalChips() {
        if (viewState.value.uiState.totalChips <= 0) {
            viewModelScope.launch {
                DialogController.sendEvent(
                    DialogEvent(
                        title = UiText(
                            R.string.alert_dialog_no_credits_title,
                        ),
                        message = UiText(
                            R.string.alert_dialog_no_credits_message,
                        ),
                        positiveAction = DialogAction(
                            buttonText = UiText(R.string.yes),
                            action = {
                                setEffect {
                                    DashContract.Effect.Navigation.NavRoute(
                                        route = NavRoutes.PlayDest.Practice,
                                        popUpToRoute = NavRoutes.HomeGraph,
                                        inclusive = false
                                    )
                                }
                            }
                        ),
                        negativeAction = DialogAction(
                            buttonText = UiText(R.string.no),
                            action = {}
                        )
                    )
                )
            }
        }
    }

    private fun showNoCreditsDialog() {
        if (viewState.value.uiState.totalChips <= 0) {
            viewModelScope.launch {
                DialogController.sendEvent(
                    DialogEvent(
                        title = UiText(R.string.alert_dialog_no_credits_title),
                        message = UiText(R.string.alert_dialog_play_now_no_credits_message),
                        positiveAction = DialogAction(
                            buttonText = UiText(R.string.ok),
                            action = {}
                        )
                    )
                )
            }
        }
    }
}