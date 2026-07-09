package com.weberpackage.blackjack.coredata

class Player(var chips: Int) {
    val hand = Hand()
    var currentBet = 0
    var multiplier: Float = 1.0f

    fun placeBet(amount: Int): Boolean {
        return if (amount <= chips) {
            currentBet = amount
            chips -= amount
            true
        } else {
            false
        }
    }

    fun winBet() {
        val profit = currentBet
        chips += currentBet + (profit * multiplier).toInt()
        currentBet = 0
    }

    fun blackjackWin() {
        val profit = currentBet * 1.5f
        chips += currentBet + (profit * multiplier).toInt()
        currentBet = 0
    }

    fun push() {
        chips += currentBet
        currentBet = 0
    }
}

class Dealer {
    val hand = Hand()

    // Traditional dealer logic: must hit on anything under 17
    fun shouldHit(): Boolean {
        return hand.calculateScore() < 17
    }
}