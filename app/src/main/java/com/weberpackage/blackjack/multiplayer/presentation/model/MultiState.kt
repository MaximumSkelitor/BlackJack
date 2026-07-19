package com.weberpackage.blackjack.multiplayer.presentation.model

import com.weberpackage.blackjack.common.presentation.model.PlayCard
import com.weberpackage.blackjack.common.presentation.utils.UiText

data class MultiState(
    val equippedPack: Int,
    val player1Hand: List<PlayCard>,
    val player2Hand: List<PlayCard>,
    val dealerHand: List<PlayCard>,
    val isPlayer1Done: Boolean,
    val isPlayer2Done: Boolean,
    val currentPlayer: Int,
    val statusMessage: UiText,
    val isGameOver: Boolean
)
