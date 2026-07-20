package com.weberpackage.blackjack.betting.presentation.screen

import androidx.lifecycle.viewModelScope
import com.weberpackage.blackjack.R
import com.weberpackage.blackjack.betting.presentation.contract.BetContract
import com.weberpackage.blackjack.betting.presentation.model.BetState
import com.weberpackage.blackjack.common.presentation.base.BaseViewModel
import com.weberpackage.blackjack.common.presentation.navigation.NavRoutes
import com.weberpackage.blackjack.core.prefs.Pref
import com.weberpackage.blackjack.core.prefs.Prefs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BettingViewModel @Inject constructor(
    private val prefs: Prefs
) : BaseViewModel<BetContract.Event, BetContract.State, BetContract.Effect>() {

    init {
        collectPrefsFlow()
    }

    private fun collectPrefsFlow() {
        collectAndUpdateState(Pref.totalChips) {
            copy(
                betState = betState.copy(
                    totalChips = it
                )
            )
        }
        collectAndUpdateState(Pref.customBet) {
            copy(
                betState = betState.copy(
                    customBet = it
                )
            )
        }
        collectAndUpdateState(Pref.creditsOptionSelected) {
            copy(
                betState = betState.copy(
                    isCustomBetEditEnabled = it,
                    longPressString = if (it) R.string.custom_bet_message2 else R.string.custom_bet_message1
                )
            )
        }
    }

    override fun setInitialState(): BetContract.State {
        val editEnabled = prefs.get(Pref.creditsOptionSelected)
        return BetContract.State(
            betState = BetState(
                totalChips = prefs.get(Pref.totalChips),
                customBet = prefs.get(Pref.customBet),
                longPressString = if (editEnabled) R.string.custom_bet_message2 else R.string.custom_bet_message1,
                isCustomBetEditEnabled = editEnabled
            ),
            isInitialLoading = true
        )
    }

    override fun handleEvents(event: BetContract.Event) {
        when (event) {
            is BetContract.Event.CheckTotalChips -> {}
            is BetContract.Event.OnUpdateCredits -> onUpdateCredits(event.amount)
            is BetContract.Event.OnAdjustBet -> {
                val currentTotal = viewState.value.betState.totalChips
                setState {
                    val newBet = if (event.amount == 0) 0
                    else (betState.currentBet + event.amount).coerceIn(0, currentTotal)
                    copy(
                        betState = betState.copy(
                            currentBet = newBet
                        )
                    )
                }
            }

            is BetContract.Event.OnSelectCustomBet -> {
                setState {
                    copy(
                        betState = betState.copy(
                            currentBet = event.amount.coerceIn(
                                minimumValue = 0,
                                maximumValue = viewState.value.betState.totalChips
                            )
                        )
                    )
                }
            }

            is BetContract.Event.OnConfirmBet -> onConfirmBet()
            is BetContract.Event.OnCustomBetChange -> {
                prefs.set(Pref.customBet, event.amount)
            }
        }
    }

    private fun onUpdateCredits(amount: Int) {
        val currentChips = prefs.get(Pref.totalChips)
        prefs.set(Pref.totalChips, currentChips + amount)
    }

    private fun onConfirmBet() {
        val currentBet = viewState.value.betState.currentBet
        if (currentBet > 0) {
            val currentTotal = viewState.value.betState.totalChips
            prefs.set(Pref.currentBet, currentBet)
            prefs.set(Pref.totalChips, currentTotal - currentBet)

            setEffect {
                BetContract.Effect.Navigation.NavRoute(
                    route = NavRoutes.PlayDest.PlayNow,
                    popUpToRoute = NavRoutes.PlayDest.Betting,
                    inclusive = true
                )
            }
        }
    }


    private fun <T> collectAndUpdateState(
        pref: Pref<T>,
        update: BetContract.State.(T) -> BetContract.State
    ) {
        viewModelScope.launch {
            prefs.collectPrefsFlow(pref).collect { value ->
                setState { update(value) }
            }
        }
    }
}