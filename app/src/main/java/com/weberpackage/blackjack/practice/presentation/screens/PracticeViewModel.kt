package com.weberpackage.blackjack.practice.presentation.screens

import androidx.lifecycle.viewModelScope
import com.weberpackage.blackjack.R
import com.weberpackage.blackjack.common.presentation.base.BaseViewModel
import com.weberpackage.blackjack.common.presentation.utils.UiText
import com.weberpackage.blackjack.core.prefs.Pref
import com.weberpackage.blackjack.core.prefs.Prefs
import com.weberpackage.blackjack.core.utils.Dealer
import com.weberpackage.blackjack.core.utils.DeckManager
import com.weberpackage.blackjack.core.utils.Player
import com.weberpackage.blackjack.practice.presentation.contract.PracticeContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PracticeViewModel @Inject constructor(
    private val prefs: Prefs
): BaseViewModel<PracticeContract.Event, PracticeContract.State, PracticeContract.Effect>() {

    private val player = Player(chips = 1000)
    private val deckManager = DeckManager()

    private val dealer = Dealer()

    init {
        collectPrefsFlow()
    }

    private fun collectPrefsFlow() {
        collectAndUpdateState(Pref.equippedPack) {
            copy(
                equippedPack = it
            )
        }
    }

    override fun setInitialState() = PracticeContract.State(
        playerHand = emptyList(),
        dealerHand = emptyList(),
        statusMessage = UiText(R.string.welcome_blackjack),
        equippedPack = 1,
        isGameOver = true
    )

    override fun handleEvents(event: PracticeContract.Event) {
        when (event) {
            is PracticeContract.Event.Hit -> hit()
            is PracticeContract.Event.Stand -> stand()
            is PracticeContract.Event.StartNewGame -> startNewGame()
            is PracticeContract.Event.ResetGame -> startNewGame()
        }
    }

    private fun startNewGame() {
        deckManager.resetAndShuffle()
        player.handManager.clear()
        dealer.handManager.clear()

        // Deal initial cards
        player.handManager.addCard(deckManager.dealCard())
        dealer.handManager.addCard(deckManager.dealCard())
        player.handManager.addCard(deckManager.dealCard())
        dealer.handManager.addCard(deckManager.dealCard())

        updateState()
        updateStatusMessage(R.string.your_turn)
        setState {
            copy(
                isGameOver = false
            )
        }
        checkInitialBlackjack()
    }

    private fun checkInitialBlackjack() {
        val playerValue = player.handManager.calculateScore()
        val dealerValue = dealer.handManager.calculateScore()

        if (playerValue == 21 && dealerValue == 21) {
            updateStatusMessage(R.string.push_both_blackjack)
            finishGame()
        } else if (playerValue == 21) {
            updateStatusMessage(R.string.blackjack_win)
            finishGame()
        } else if (dealerValue == 21) {
            updateStatusMessage(R.string.dealer_blackjack)
            finishGame()
        }
    }

    private fun hit() {
        if (viewState.value.isGameOver) return

        player.handManager.addCard(deckManager.dealCard())
        updateState()

        if (player.handManager.isBust()) {
            updateStatusMessage(R.string.player_bust)
            finishGame()
        }
    }

    private fun stand() {
        if (viewState.value.isGameOver) return

        while (dealer.shouldHit()) {
            dealer.handManager.addCard(deckManager.dealCard())
        }
        updateState()

        val playerValue = player.handManager.calculateScore()
        val dealerValue = dealer.handManager.calculateScore()

        val statusMessage = when {
            dealer.handManager.isBust() -> R.string.dealer_bust
            playerValue > dealerValue -> R.string.player_win
            playerValue < dealerValue -> R.string.dealer_wins
            else -> R.string.push
        }
        updateStatusMessage(message = statusMessage)
        finishGame()
    }

    private fun updateStatusMessage(message: Int) {
        setState {
            copy(
                statusMessage = UiText(message)
            )
        }
    }

    private fun updateState() {
        setState {
            copy(
                playerHand = player.handManager.cards.toList(),
                dealerHand = dealer.handManager.cards.toList()
            )
        }
    }

    private fun finishGame() {
        setState {
            copy(
                isGameOver = true
            )
        }
    }

    private fun <T> collectAndUpdateState(
        pref: Pref<T>,
        update: PracticeContract.State.(T) -> PracticeContract.State
    ) {
        viewModelScope.launch {
            prefs.collectPrefsFlow(pref).collect { value ->
                setState { update(value) }
            }
        }
    }
}