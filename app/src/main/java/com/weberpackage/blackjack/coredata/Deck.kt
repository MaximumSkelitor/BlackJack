package com.weberpackage.blackjack.coredata

class Deck {
    private val cards = mutableListOf<PlayCard>()

    init {
        resetAndShuffle()
    }

    fun resetAndShuffle() {
        cards.clear()
        for (suit in Suit.entries) {
            for (rank in Rank.entries) {
                cards.add(PlayCard(suit, rank))
            }
        }
        cards.shuffle()
    }

    fun dealCard(): PlayCard {
        if (cards.isEmpty()) {
            resetAndShuffle()
        }
        return cards.removeAt(0)
    }
}