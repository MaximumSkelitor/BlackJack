package com.weberpackage.blackjack.dashboard.presentation.model

data class DashState(
    val totalChips: Int = 0,
    val gamesPlayed: Long = 0L,
    val username: String = "Player",
    val currentRank: RankData? = null,
    val nextRank: RankData? = null,
    val rankProgress: Float = 0f
)