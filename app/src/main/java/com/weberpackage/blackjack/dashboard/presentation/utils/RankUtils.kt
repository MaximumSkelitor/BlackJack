package com.weberpackage.blackjack.dashboard.presentation.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.weberpackage.blackjack.R

data class RankData(
    val nameRes: Int,
    val descRes: Int,
    val icon: ImageVector,
    val gradient: Brush,
    val requiredChips: Int,
    val multiplier: Float
)

object RankUtils {
    val ranks = listOf(
        RankData(
            nameRes = R.string.rank_1,
            descRes = R.string.rank_1_desc,
            icon = Icons.Default.MilitaryTech,
            gradient = Brush.linearGradient(
                colors = listOf(
                    Color(0xFFFFD27B),
                    Color(0xFFF0A34A),
                    Color(0xFFC46B1E),
                    Color(0xFF6F3610)
                ),
                start = Offset.Zero,
                end = Offset(200f, 200f)
            ),
            requiredChips = 0,
            multiplier = 1.0f
        ),
        RankData(
            nameRes = R.string.rank_2,
            descRes = R.string.rank_2_desc,
            icon = Icons.Default.WorkspacePremium,
            gradient = Brush.linearGradient(
                colors = listOf(
                    Color(0xFFE3E3E3),
                    Color(0xFFD5D5D5),
                    Color(0xFF9A9A9A),
                    Color(0xFF414141)
                ),
                start = Offset.Zero,
                end = Offset(200f, 200f)
            ),
            requiredChips = 2000,
            multiplier = 1.5f
        ),
        RankData(
            nameRes = R.string.rank_3,
            descRes = R.string.rank_3_desc,
            icon = Icons.Default.Stars,
            gradient = Brush.linearGradient(
                listOf(
                    Color(0xFFE65100),
                    Color(0xFFFFB300),
                    Color(0xFFFFD54F)
                )
            ),
            requiredChips = 2500,
            multiplier = 2.0f
        ),
        RankData(
            nameRes = R.string.rank_4,
            descRes = R.string.rank_4_desc,
            icon = Icons.Default.Diamond,
            gradient = Brush.linearGradient(
                colors = listOf(
                    Color(0xFF65FFED),
                    Color(0xFF6FFDDC),
                    Color(0xFF44B7B7),
                    Color(0xFF369494)
                ),
                start = Offset.Zero,
                end = Offset(200f, 200f)
            ),
            requiredChips = 3000,
            multiplier = 2.5f
        ),
        RankData(
            nameRes = R.string.rank_5,
            descRes = R.string.rank_5_desc,
            icon = Icons.Default.EmojiEvents,
            gradient = Brush.linearGradient(
                colors = listOf(
                    Color(0xFFFF5252),
                    Color(0xFFCE3A3A),
                    Color(0xFFB74444),
                    Color(0xFF943636)
                ),
                start = Offset.Zero,
                end = Offset(200f, 200f)
            ),
            requiredChips = 5000,
            multiplier = 3f
        )
    )

    fun getMultiplier(chips: Int): Float {
        return ranks.lastOrNull { chips >= it.requiredChips }?.multiplier ?: 1.0f
    }
}
