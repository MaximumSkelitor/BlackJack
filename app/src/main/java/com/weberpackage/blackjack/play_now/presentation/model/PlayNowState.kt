package com.weberpackage.blackjack.play_now.presentation.model

import com.weberpackage.blackjack.common.presentation.model.PlayCard
import com.weberpackage.blackjack.common.presentation.utils.UiText

data class PlayNowState(
    val totalChips: Int,
    val equippedPack: Int,
    val playerHand: List<PlayCard>,
    val dealerHand: List<PlayCard>,
    val statusMessage: UiText,
    val isGameOver: Boolean,
    val currentBet: Int = 0,
)