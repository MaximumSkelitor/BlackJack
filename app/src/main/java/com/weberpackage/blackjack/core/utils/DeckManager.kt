package com.weberpackage.blackjack.core.utils

import com.weberpackage.blackjack.common.presentation.model.PlayCard
import com.weberpackage.blackjack.common.presentation.model.Rank
import com.weberpackage.blackjack.common.presentation.model.Suit

class DeckManager {
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