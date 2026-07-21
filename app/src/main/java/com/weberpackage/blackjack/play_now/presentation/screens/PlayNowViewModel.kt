package com.weberpackage.blackjack.play_now.presentation.screens

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
import com.weberpackage.blackjack.core.utils.Dealer
import com.weberpackage.blackjack.core.utils.DeckManager
import com.weberpackage.blackjack.core.utils.Player
import com.weberpackage.blackjack.dashboard.presentation.utils.RankUtils
import com.weberpackage.blackjack.play_now.presentation.contract.PlayNowContract
import com.weberpackage.blackjack.play_now.presentation.model.PlayNowState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayNowViewModel @Inject constructor(
    private val prefs: Prefs
) : BaseViewModel<PlayNowContract.Event, PlayNowContract.State, PlayNowContract.Effect>() {

    private val player = Player(chips = prefs.get(Pref.totalChips)).apply {
        multiplier = RankUtils.getMultiplier(chips)
    }
    private val deckManager = DeckManager()
    private val dealer = Dealer()

    init {
        collectPrefsFlow()
        val bet = prefs.get(Pref.currentBet)
        if (bet > 0) {
            player.currentBet = bet
            startNewGame()
        }
    }

    private fun collectPrefsFlow() {
        collectAndUpdateState(Pref.equippedPack) {
            copy(
                playNowState = (playNowState.copy(equippedPack = it))
            )
        }
        viewModelScope.launch {
            prefs.collectPrefsFlow(Pref.totalChips).collect { chips ->
                player.chips = chips
                player.multiplier = RankUtils.getMultiplier(chips)
                setState {
                    copy(
                        playNowState = (playNowState.copy(totalChips = chips))
                    )
                }
            }
        }
    }

    override fun setInitialState(): PlayNowContract.State {
        val bet = prefs.get(Pref.currentBet)
        return PlayNowContract.State(
            playNowState = PlayNowState(
                playerHand = emptyList(),
                dealerHand = emptyList(),
                statusMessage = UiText(R.string.welcome_blackjack),
                equippedPack = prefs.get(Pref.equippedPack),
                totalChips = prefs.get(Pref.totalChips),
                isGameOver = bet <= 0,
                currentBet = bet,
            )
        )
    }

    override fun handleEvents(event: PlayNowContract.Event) {
        when (event) {
            is PlayNowContract.Event.CheckTotalChips -> {}
            is PlayNowContract.Event.Hit -> hit()
            is PlayNowContract.Event.Stand -> stand()
            is PlayNowContract.Event.StartNewGame -> startNewGame()
            is PlayNowContract.Event.ResetGame -> resetGame()
            is PlayNowContract.Event.PlaceBet -> {
                // This event is now theoretically handled by BettingScreen, 
                // but keeping logic for internal use if needed.
                placeBet(event.amount)
            }
            is PlayNowContract.Event.ShowExitDialog -> showExitDialog()
            is PlayNowContract.Event.ShowNoCreditsAlert -> {}
        }
    }

    private fun placeBet(amount: Int) {
        player.multiplier = RankUtils.getMultiplier(player.chips)

        if (player.placeBet(amount)) {
            setState {
                copy(
                    playNowState = (
                        playNowState.copy(
                            currentBet = amount,
                            totalChips = player.chips,
                        )
                    )
                )
            }
            prefs.set(Pref.totalChips, player.chips)
            startNewGame()
        } else {
            setEffect {
                PlayNowContract.Effect.Notification(
                    UiText(R.string.alert_dialog_no_credits_title),
                    true
                )
            }
        }
    }

    private fun startNewGame() {
        deckManager.resetAndShuffle()
        player.handManager.clear()
        dealer.handManager.clear()
        updateHandState()

        repeat(2) {
            player.handManager.addCard(deckManager.dealCard())
            dealer.handManager.addCard(deckManager.dealCard())
        }

        updateHandState()
        updateStatusMessage(R.string.your_turn)
        setState {
            copy(
                playNowState = playNowState.copy(
                    isGameOver = false
                )
            )
        }
        checkInitialBlackjack()
    }

    private fun checkInitialBlackjack() {
        val playerValue = player.handManager.calculateScore()
        val dealerValue = dealer.handManager.calculateScore()

        if (playerValue == 21 && dealerValue == 21) {
            updateStatusMessage(R.string.push_both_blackjack)
            player.push()
            finishGame()
        } else if (playerValue == 21) {
            updateStatusMessage(R.string.blackjack_win)
            player.blackjackWin()
            finishGame()
        } else if (dealerValue == 21) {
            updateStatusMessage(R.string.dealer_blackjack)
            finishGame()
        }
    }

    private fun hit() {
        if (viewState.value.playNowState.isGameOver) return

        player.handManager.addCard(deckManager.dealCard())
        updateHandState()

        if (player.handManager.isBust()) {
            updateStatusMessage(R.string.player_bust)
            finishGame()
        }
    }

    private fun stand() {
        if (viewState.value.playNowState.isGameOver) return

        while (dealer.shouldHit()) {
            dealer.handManager.addCard(deckManager.dealCard())
        }
        updateHandState()

        val playerValue = player.handManager.calculateScore()
        val dealerValue = dealer.handManager.calculateScore()

        val statusMessage = when {
            dealer.handManager.isBust() -> {
                player.winBet()
                R.string.dealer_bust
            }

            playerValue > dealerValue -> {
                player.winBet()
                R.string.player_win
            }

            playerValue < dealerValue -> R.string.dealer_wins
            else -> {
                player.push()
                R.string.push
            }
        }
        updateStatusMessage(message = statusMessage)
        finishGame()
    }

    private fun updateStatusMessage(message: Int) {
        setState {
            copy(
                playNowState = playNowState.copy(
                    statusMessage = UiText(message)
                )
            )
        }
    }

    private fun updateHandState() {
        setState {
            copy(
                playNowState = playNowState.copy(
                    playerHand = player.handManager.cards.toList(),
                    dealerHand = dealer.handManager.cards.toList()
                )
            )
        }
    }

    private fun finishGame() {
        setState {
            copy(
                playNowState = playNowState.copy(
                    isGameOver = true,
                    totalChips = player.chips,
                    currentBet = 0
                )
            )
        }
        prefs.set(Pref.totalChips, player.chips)
        if (!prefs.get(Pref.saveCurrentBet)) {
            prefs.set(Pref.currentBet, 0)
        }
    }

    private fun resetGame() {
        setEffect {
            PlayNowContract.Effect.Navigation.NavRoute(
                route = NavRoutes.PlayDest.Betting,
                popUpToRoute = NavRoutes.PlayDest.PlayNow,
                inclusive = true
            )
        }
    }

    private fun showExitDialog() {
        viewModelScope.launch {
            DialogController.sendEvent(
                DialogEvent(
                    title = UiText(R.string.dialog_exit_game_title),
                    message = UiText(R.string.dialog_exit_game_desc),
                    negativeAction = DialogAction(
                        buttonText = UiText(R.string.no),
                        action = {}
                    ),
                    positiveAction = DialogAction(
                        buttonText = UiText(R.string.yes),
                        action = {
                            setEffect { PlayNowContract.Effect.Navigation.Back }
                        }
                    )
                )
            )
        }
    }



    private fun <T> collectAndUpdateState(
        pref: Pref<T>,
        update: PlayNowContract.State.(T) -> PlayNowContract.State
    ) {
        viewModelScope.launch {
            prefs.collectPrefsFlow(pref).collect { value ->
                setState { update(value) }
            }
        }
    }
}