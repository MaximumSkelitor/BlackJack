package com.weberpackage.blackjack.dashboard.presentation.model

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector

data class RankData(
    val nameRes: Int,
    val descRes: Int,
    val icon: ImageVector,
    val gradient: Brush,
    val requiredChips: Int,
    val requiredGamesPlayed: Long,
    val multiplier: Float
)