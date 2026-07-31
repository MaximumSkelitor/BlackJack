package com.weberpackage.blackjack.profile.presentation.model

import com.weberpackage.blackjack.dashboard.presentation.model.RankData

data class ProfileState(
    val username: String = "",
    val totalChips: Int = 0,
    val highestChips: Int = 0,
    val gamesPlayed: Long = 0,
    val careerCredits: Long = 0L,
    val ownedPacks: List<Int> = emptyList(),
    val equippedPack: Int = 0,
    val currentRank: RankData? = null,
    val nextRank: RankData? = null,
    val rankProgress: Float = 0f,
    val unlockedAchievements: Int = 0,
    val totalAchievements: Int = 0
)
