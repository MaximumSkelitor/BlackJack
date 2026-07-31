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
import com.weberpackage.blackjack.R
import com.weberpackage.blackjack.dashboard.presentation.model.RankData


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
            requiredGamesPlayed = 0,
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
            requiredChips = 10000,
            requiredGamesPlayed = 25,
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
            requiredChips = 20000,
            requiredGamesPlayed = 50,
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
            requiredChips = 30000,
            requiredGamesPlayed = 75,
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
            requiredChips = 100000,
            requiredGamesPlayed = 150,
            multiplier = 3f
        )
    )

    fun getMultiplier(chips: Int): Float {
        return ranks.lastOrNull { chips >= it.requiredChips }?.multiplier ?: 1.0f
    }

    fun getCurrentRank(chips: Int, gamesPlayed: Long): RankData {
        return ranks.lastOrNull { chips >= it.requiredChips && gamesPlayed >= it.requiredGamesPlayed } ?: ranks.first()
    }

    fun getNextRank(chips: Int, gamesPlayed: Long): RankData? {
        val currentRankIndex = ranks.indexOf(getCurrentRank(chips, gamesPlayed))
        return ranks.getOrNull(currentRankIndex + 1)
    }

    fun getRankProgress(
        rank: RankData,
        userChips: Int,
        userGamesPlayed: Long,
        prevRank: RankData? = null,
    ): Float {
        val startChips = prevRank?.requiredChips ?: 0
        val startGames = prevRank?.requiredGamesPlayed ?: 0L

        val chipRange = (rank.requiredChips - startChips).toFloat()
        val chipProgress = if (chipRange > 0) {
            ((userChips - startChips).toFloat() / chipRange).coerceIn(0f, 1f)
        } else 1f

        val gameRange = (rank.requiredGamesPlayed - startGames).toFloat()
        val gameProgress = if (gameRange > 0) {
            ((userGamesPlayed - startGames).toFloat() / gameRange).coerceIn(0f, 1f)
        } else 1f

        return (chipProgress + gameProgress) / 2f
    }
}
