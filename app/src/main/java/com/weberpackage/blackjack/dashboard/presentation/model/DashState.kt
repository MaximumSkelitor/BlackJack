package com.weberpackage.blackjack.dashboard.presentation.model

data class DashState(
    val totalChips: Int = 0,
    val gamesPlayed: Long = 0L,
    val username: String = "Player",
)