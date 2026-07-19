package com.weberpackage.blackjack.common.presentation.model

data class BetItem(
    val amount: Int,
) {
    val isPositive: Boolean = amount > 0
    val isClear: Boolean = amount == 0
    val text = when {
        isPositive -> "+$amount"
        isClear -> "CLR"
        else -> "$amount"
    }
}