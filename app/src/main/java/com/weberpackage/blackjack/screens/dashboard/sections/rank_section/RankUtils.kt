package com.weberpackage.blackjack.screens.dashboard.sections.rank_section

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.ui.graphics.vector.ImageVector
import com.weberpackage.blackjack.R

data class RankData(
    val nameRes: Int,
    val descRes: Int,
    val icon: ImageVector,
    val requiredChips: Int,
    val multiplier: Float
)

object RankUtils {
    val ranks = listOf(
        RankData(
            nameRes = R.string.rank_1,
            descRes = R.string.rank_1_desc,
            icon = Icons.Default.MilitaryTech,
            requiredChips = 0,
            multiplier = 1.0f
        ),
        RankData(
            nameRes = R.string.rank_2,
            descRes = R.string.rank_2_desc,
            icon = Icons.Default.WorkspacePremium,
            requiredChips = 500,
            multiplier = 1.5f
        ),
        RankData(
            nameRes = R.string.rank_3,
            descRes = R.string.rank_3_desc,
            icon = Icons.Default.Stars,
            requiredChips = 1000,
            multiplier = 2.0f
        ),
        RankData(
            nameRes = R.string.rank_4,
            descRes = R.string.rank_4_desc,
            icon = Icons.Default.Diamond,
            requiredChips = 2000,
            multiplier = 2.5f
        ),
        RankData(
            nameRes = R.string.rank_5,
            descRes = R.string.rank_5_desc,
            icon = Icons.Default.EmojiEvents,
            requiredChips = 5000,
            multiplier = 3f
        )
    )

    fun getMultiplier(chips: Int): Float {
        return ranks.lastOrNull { chips >= it.requiredChips }?.multiplier ?: 1.0f
    }
}