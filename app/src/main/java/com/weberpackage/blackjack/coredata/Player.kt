package com.weberpackage.blackjack.coredata

class Player(var chips: Int) {
    val hand = Hand()
    var currentBet = 0

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
        chips += currentBet * 2
        currentBet = 0
    }

    fun blackjackWin() {
        chips += (currentBet * 2.5).toInt() // 3:2 payout
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