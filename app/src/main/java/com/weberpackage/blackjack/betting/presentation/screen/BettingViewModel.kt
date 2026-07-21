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
        collectAndUpdateState(Pref.currentBet) {
            copy(
                betState = betState.copy(
                    currentBet = it
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
        collectAndUpdateState(Pref.saveCurrentBet) {
            copy(
                betState = betState.copy(
                    saveCurrentBetEnabled = it
                )
            )
        }
        collectAndUpdateState(Pref.saveCustomBet) {
            copy(
                betState = betState.copy(
                    saveCustomBetEnabled = it
                )
            )
        }
    }

    override fun setInitialState(): BetContract.State {
        val editEnabled = prefs.get(Pref.creditsOptionSelected)
        val saveCurrent = prefs.get(Pref.saveCurrentBet)
        val saveCustom = prefs.get(Pref.saveCustomBet)

        val currentBet = if (saveCurrent) prefs.get(Pref.currentBet) else 0
        val customBet = if (saveCustom) prefs.get(Pref.customBet) else 100

        return BetContract.State(
            betState = BetState(
                totalChips = prefs.get(Pref.totalChips),
                currentBet = currentBet,
                customBet = customBet,
                longPressString = if (editEnabled) R.string.custom_bet_message2 else R.string.custom_bet_message1,
                isCustomBetEditEnabled = editEnabled,
                saveCurrentBetEnabled = saveCurrent,
                saveCustomBetEnabled = saveCustom
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
                val newBet = if (event.amount == 0) 0
                else (viewState.value.betState.currentBet + event.amount).coerceIn(0, currentTotal)
                onUpdateCurrentBet(newBet)
            }

            is BetContract.Event.OnSelectCustomBet -> {
                val newBet = event.amount.coerceIn(
                    minimumValue = 0,
                    maximumValue = viewState.value.betState.totalChips
                )
                onUpdateCurrentBet(newBet)
            }

            is BetContract.Event.OnConfirmBet -> onConfirmBet()
            is BetContract.Event.OnCustomBetChange -> onCustomBetChange(event.amount)
        }
    }

    private fun onUpdateCurrentBet(amount: Int) {
        if (viewState.value.betState.saveCurrentBetEnabled) {
            prefs.set(Pref.currentBet, amount)
        } else {
            setState {
                copy(
                    betState = betState.copy(
                        currentBet = amount
                    )
                )
            }
        }
    }

    private fun onCustomBetChange(amount: Int) {
        if (viewState.value.betState.saveCustomBetEnabled) {
            prefs.set(Pref.customBet, amount)
        } else {
            setState {
                copy(
                    betState = betState.copy(
                        customBet = amount
                    )
                )
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