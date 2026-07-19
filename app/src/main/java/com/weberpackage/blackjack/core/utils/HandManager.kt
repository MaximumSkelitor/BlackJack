package com.weberpackage.blackjack.core.utils

import com.weberpackage.blackjack.common.presentation.model.PlayCard
import com.weberpackage.blackjack.common.presentation.model.Rank

class HandManager {
    val cards = mutableListOf<PlayCard>()

    fun addCard(card: PlayCard) {
        cards.add(card)
    }

    fun calculateScore(): Int {
        var score = cards.sumOf { it.rank.value }
        var aceCount = cards.count { it.rank == Rank.ACE }

        // If the score is over 21, and we have Aces, treat them as 1 instead of 11
        while (score > 21 && aceCount > 0) {
            score -= 10
            aceCount--
        }
        return score
    }

    fun isBust(): Boolean = calculateScore() > 21

    fun clear() {
        cards.clear()
    }
}