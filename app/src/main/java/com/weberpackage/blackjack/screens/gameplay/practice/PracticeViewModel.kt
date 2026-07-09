package com.weberpackage.blackjack.screens.gameplay.practice

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.weberpackage.blackjack.R
import com.weberpackage.blackjack.coredata.Dealer
import com.weberpackage.blackjack.coredata.Deck
import com.weberpackage.blackjack.coredata.PlayCard
import com.weberpackage.blackjack.coredata.Player
import com.weberpackage.blackjack.coredata.PreferenceManager

class PracticeViewModel(preferenceManager: PreferenceManager) : ViewModel() {
    private val deck = Deck()
    private val player = Player(chips = preferenceManager.getChips())
    private val dealer = Dealer()

    var playerHand by mutableStateOf<List<PlayCard>>(emptyList())
        private set
    var dealerHand by mutableStateOf<List<PlayCard>>(emptyList())
        private set
    var statusMessageResId by mutableIntStateOf(R.string.welcome_blackjack)
        private set
    var isGameOver by mutableStateOf(true)
        private set
    var totalChips by mutableIntStateOf(player.chips)
        private set

    fun startNewGame() {
        deck.resetAndShuffle()
        player.hand.clear()
        dealer.hand.clear()

        // Deal initial cards
        player.hand.addCard(deck.dealCard())
        dealer.hand.addCard(deck.dealCard())
        player.hand.addCard(deck.dealCard())
        dealer.hand.addCard(deck.dealCard())

        updateState()
        statusMessageResId = R.string.your_turn
        isGameOver = false

        checkInitialBlackjack()
    }

    private fun checkInitialBlackjack() {
        val playerValue = player.hand.calculateScore()
        val dealerValue = dealer.hand.calculateScore()

        if (playerValue == 21 && dealerValue == 21) {
            statusMessageResId = R.string.push_both_blackjack
            finishGame()
        } else if (playerValue == 21) {
            statusMessageResId = R.string.blackjack_win
            finishGame()
        } else if (dealerValue == 21) {
            statusMessageResId = R.string.dealer_blackjack
            finishGame()
        }
    }

    fun hit() {
        if (isGameOver) return

        player.hand.addCard(deck.dealCard())
        updateState()

        if (player.hand.isBust()) {
            statusMessageResId = R.string.player_bust
            finishGame()
        }
    }

    fun stand() {
        if (isGameOver) return

        while (dealer.shouldHit()) {
            dealer.hand.addCard(deck.dealCard())
        }
        updateState()

        val playerValue = player.hand.calculateScore()
        val dealerValue = dealer.hand.calculateScore()

        statusMessageResId = when {
            dealer.hand.isBust() -> R.string.dealer_bust
            playerValue > dealerValue -> R.string.player_win
            playerValue < dealerValue -> R.string.dealer_wins
            else -> R.string.push
        }
        finishGame()
    }

    private fun finishGame() {
        isGameOver = true
    }

    private fun updateState() {
        playerHand = player.hand.cards.toList()
        dealerHand = dealer.hand.cards.toList()
    }

    fun resetGame() {
        startNewGame()
    }
}

class PracticeViewModelFactory(private val preferenceManager: PreferenceManager) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PracticeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PracticeViewModel(preferenceManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
