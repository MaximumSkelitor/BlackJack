package com.weberpackage.blackjack.betting.presentation.model

import com.weberpackage.blackjack.R

data class BetState(
    val totalChips: Int = 1000,
    val currentBet: Int = 0,
    val customBet: Int = 100,
    val longPressString: Int = R.string.custom_bet_message1,
    val isCustomBetEditEnabled: Boolean = false,
)