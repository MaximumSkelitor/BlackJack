package com.weberpackage.blackjack.profile.presentation.screens.achievements.model

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector

data class Achievement(
    val id: String,
    val titleRes: Int,
    val descRes: Int,
    val icon: ImageVector,
    val iconGradient: Brush,
    val targetValue: Long,
    val currentValue: Long,
    val isUnlocked: Boolean = false,
    val isClaimed: Boolean = false,
    val rewardPackId: Int? = null,
    val rewardCredits: Int? = null,
    val type: AchievementType
)

enum class AchievementType {
    GAMES_PLAYED,
    HIGHEST_CHIPS,
    TOTAL_CHIPS,
    CAREER_CHIPS
}
