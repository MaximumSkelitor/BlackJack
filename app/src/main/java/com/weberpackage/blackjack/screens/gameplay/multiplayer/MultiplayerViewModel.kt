package com.weberpackage.blackjack.screens.gameplay.multiplayer

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.weberpackage.blackjack.R
import com.weberpackage.blackjack.coredata.PlayCard
import com.weberpackage.blackjack.coredata.Dealer
import com.weberpackage.blackjack.coredata.Deck
import com.weberpackage.blackjack.coredata.Player

class MultiplayerViewModel : ViewModel() {
    private val deck = Deck()
    private val player1 = Player(chips = 0)
    private val player2 = Player(chips = 0)
    private val dealer = Dealer()

    var player1Hand by mutableStateOf<List<PlayCard>>(emptyList())
        private set
    var player2Hand by mutableStateOf<List<PlayCard>>(emptyList())
        private set
    var dealerHand by mutableStateOf<List<PlayCard>>(emptyList())
        private set
    var statusMessageResId by mutableIntStateOf(R.string.welcome_blackjack)
        private set
    var isGameOver by mutableStateOf(false)
        private set
    var currentPlayer by mutableIntStateOf(1) // 1 or 2
        private set
    var isPlayer1Done by mutableStateOf(false)
        private set
    var isPlayer2Done by mutableStateOf(false)
        private set

    init {
        startNewGame()
    }

    fun startNewGame() {
        deck.resetAndShuffle()
        player1.hand.clear()
        player2.hand.clear()
        dealer.hand.clear()

        // Deal initial cards
        player1.hand.addCard(deck.dealCard())
        player2.hand.addCard(deck.dealCard())
        player1.hand.addCard(deck.dealCard())
        player2.hand.addCard(deck.dealCard())
        dealer.hand.addCard(deck.dealCard())
        dealer.hand.addCard(deck.dealCard())

        updateState()
        currentPlayer = 1
        isPlayer1Done = false
        isPlayer2Done = false
        isGameOver = false
        statusMessageResId = R.string.player1_turn
    }

    fun hit() {
        if (isGameOver) return

        if (currentPlayer == 1 && !isPlayer1Done) {
            player1.hand.addCard(deck.dealCard())
            if (player1.hand.isBust()) {
                isPlayer1Done = true
                moveToNextPlayer()
            }
        } else if (currentPlayer == 2 && !isPlayer2Done) {
            player2.hand.addCard(deck.dealCard())
            if (player2.hand.isBust()) {
                isPlayer2Done = true
                finishGame()
            }
        }
        updateState()
    }

    fun stand() {
        if (isGameOver) return

        if (currentPlayer == 1) {
            isPlayer1Done = true
            moveToNextPlayer()
        } else if (currentPlayer == 2) {
            isPlayer2Done = true
            finishGame()
        }
        updateState()
    }

    private fun moveToNextPlayer() {
        currentPlayer = 2
        statusMessageResId = R.string.player2_turn
    }

    private fun finishGame() {
        isGameOver = true
        
        // Dealer plays if at least one player didn't bust
        if (!player1.hand.isBust() || !player2.hand.isBust()) {
            while (dealer.shouldHit()) {
                dealer.hand.addCard(deck.dealCard())
            }
        }

        determineWinner()
        updateState()
    }

    private fun determineWinner() {
        val p1Score = player1.hand.calculateScore()
        val p2Score = player2.hand.calculateScore()
        val dScore = dealer.hand.calculateScore()

        val p1Bust = player1.hand.isBust()
        val p2Bust = player2.hand.isBust()
        val dBust = dealer.hand.isBust()

        // Winner message logic
        statusMessageResId = when {
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
    }

    private fun updateState() {
        player1Hand = player1.hand.cards.toList()
        player2Hand = player2.hand.cards.toList()
        dealerHand = dealer.hand.cards.toList()
    }
}
