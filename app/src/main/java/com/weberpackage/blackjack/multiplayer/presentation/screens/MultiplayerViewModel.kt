package com.weberpackage.blackjack.multiplayer.presentation.screens

import androidx.lifecycle.viewModelScope
import com.weberpackage.blackjack.R
import com.weberpackage.blackjack.common.presentation.base.BaseViewModel
import com.weberpackage.blackjack.common.presentation.utils.UiText
import com.weberpackage.blackjack.core.prefs.Pref
import com.weberpackage.blackjack.core.prefs.Prefs
import com.weberpackage.blackjack.core.utils.Dealer
import com.weberpackage.blackjack.core.utils.DeckManager
import com.weberpackage.blackjack.core.utils.Player
import com.weberpackage.blackjack.multiplayer.presentation.contract.MultiContract
import com.weberpackage.blackjack.multiplayer.presentation.model.MultiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MultiplayerViewModel @Inject constructor(
    private val prefs: Prefs
) : BaseViewModel<MultiContract.Event, MultiContract.State, MultiContract.Effect>() {

    private val player1 = Player(chips = 1000)
    private val player2 = Player(chips = 1000)
    private val deckManager = DeckManager()
    private val dealer = Dealer()

    init {
        collectPrefsFlow()
    }

    private fun collectPrefsFlow() {
        collectAndUpdateState(Pref.equippedPack) {
            copy(
                multiState = (multiState.copy(equippedPack = it))
            )
        }
    }

    override fun setInitialState() = MultiContract.State(
        multiState = MultiState(
            player1Hand = emptyList(),
            player2Hand = emptyList(),
            dealerHand = emptyList(),
            statusMessage = UiText(R.string.welcome_blackjack),
            equippedPack = prefs.get(Pref.equippedPack),
            isPlayer1Done = false,
            isPlayer2Done = false,
            currentPlayer = 1,
            isGameOver = true,
        )
    )

    override fun handleEvents(event: MultiContract.Event) {
        when (event) {
            is MultiContract.Event.Hit -> hit()
            is MultiContract.Event.Stand -> stand()
            is MultiContract.Event.StartNewGame -> startNewGame()
            is MultiContract.Event.ResetGame -> resetGame()
        }
    }
    
    private fun startNewGame() {
        deckManager.resetAndShuffle()
        player1.handManager.clear()
        player2.handManager.clear()
        dealer.handManager.clear()

        // Deal initial cards
        player1.handManager.addCard(deckManager.dealCard())
        player2.handManager.addCard(deckManager.dealCard())
        player1.handManager.addCard(deckManager.dealCard())
        player2.handManager.addCard(deckManager.dealCard())
        dealer.handManager.addCard(deckManager.dealCard())
        dealer.handManager.addCard(deckManager.dealCard())
        
        setState { 
            copy(
                multiState = multiState.copy(
                    statusMessage = UiText(R.string.player1_turn),
                    isGameOver = false,
                    isPlayer1Done = false,
                    isPlayer2Done = false,
                    currentPlayer = 1
                )
            )
        }
        updateHandState()
    }

    private fun hit() {
        if (viewState.value.multiState.isGameOver) return

        val currentPlayer = viewState.value.multiState.currentPlayer
        if (currentPlayer == 1) {
            player1.handManager.addCard(deckManager.dealCard())
            if (player1.handManager.isBust()) {
                setState { copy(multiState = multiState.copy(isPlayer1Done = true)) }
                moveToNextPlayer()
            }
        } else {
            player2.handManager.addCard(deckManager.dealCard())
            if (player2.handManager.isBust()) {
                setState { copy(multiState = multiState.copy(isPlayer2Done = true)) }
                finishGame()
            }
        }
        updateHandState()
    }

    private fun stand() {
        if (viewState.value.multiState.isGameOver) return

        val currentPlayer = viewState.value.multiState.currentPlayer
        if (currentPlayer == 1) {
            setState { copy(multiState = multiState.copy(isPlayer1Done = true)) }
            moveToNextPlayer()
        } else {
            setState { copy(multiState = multiState.copy(isPlayer2Done = true)) }
            finishGame()
        }
    }

    private fun moveToNextPlayer() {
        setState {
            copy(
                multiState = multiState.copy(
                    currentPlayer = 2,
                    statusMessage = UiText(R.string.player2_turn)
                )
            )
        }
    }

    private fun updateStatusMessage(message: Int) {
        setState {
            copy(
                multiState = multiState.copy(
                    statusMessage = UiText(message)
                )
            )
        }
    }
    
    private fun updateHandState() {
        setState {
            copy(
                multiState = multiState.copy(
                    player1Hand = player1.handManager.cards.toList(),
                    player2Hand = player2.handManager.cards.toList(),
                    dealerHand = dealer.handManager.cards.toList()
                )
            )
        }
    }

    private fun finishGame() {
        // Dealer plays if at least one player didn't bust
        if (!player1.handManager.isBust() || !player2.handManager.isBust()) {
            while (dealer.shouldHit()) {
                dealer.handManager.addCard(deckManager.dealCard())
            }
        }

        determineWinner()
        
        setState {
            copy(
                multiState = multiState.copy(
                    isGameOver = true
                )
            )
        }
        updateHandState()
    }

    private fun resetGame() {
        setState {
            copy(
                multiState = multiState.copy(
                    isGameOver = true,
                    player1Hand = emptyList(),
                    player2Hand = emptyList(),
                    dealerHand = emptyList(),
                    statusMessage = UiText(R.string.welcome_blackjack)
                )
            )
        }
    }

    private fun determineWinner() {
        val p1Score = player1.handManager.calculateScore()
        val p2Score = player2.handManager.calculateScore()
        val dScore = dealer.handManager.calculateScore()

        val p1Bust = player1.handManager.isBust()
        val p2Bust = player2.handManager.isBust()
        val dBust = dealer.handManager.isBust()

        // Winner message logic
        val statusRes = when {
            p1Bust && p2Bust -> R.string.both_bust
            p1Bust -> if (dBust || p2Score > dScore) R.string.player2_wins else if (p2Score < dScore) R.string.dealer_wins else R.string.push
            p2Bust -> if (dBust || p1Score > dScore) R.string.player1_wins else if (p1Score < dScore) R.string.dealer_wins else R.string.push
            dBust -> if (p1Score > p2Score) R.string.player1_wins else if (p2Score > p1Score) R.string.player2_wins else R.string.tie
            else -> {
                val p1WinsVsD = p1Score > dScore
                val p2WinsVsD = p2Score > dScore

                if (p1WinsVsD && p2WinsVsD) {
                    if (p1Score > p2Score) R.string.player1_wins else if (p2Score > p1Score) R.string.player2_wins else R.string.tie
                } else if (p1WinsVsD) {
                    R.string.player1_wins
                } else if (p2WinsVsD) {
                    R.string.player2_wins
                } else {
                    R.string.dealer_wins_all
                }
            }
        }
        updateStatusMessage(statusRes)
    }

    private fun <T> collectAndUpdateState(
        pref: Pref<T>,
        update: MultiContract.State.(T) -> MultiContract.State
    ) {
        viewModelScope.launch {
            prefs.collectPrefsFlow(pref).collect { value ->
                setState { update(value) }
            }
        }
    }
}
