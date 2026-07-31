package com.weberpackage.blackjack.profile.presentation.screens.achievements.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.weberpackage.blackjack.R
import com.weberpackage.blackjack.profile.presentation.screens.achievements.model.Achievement
import com.weberpackage.blackjack.profile.presentation.screens.achievements.model.AchievementType

object AchievementUtils {
    val initialAchievements = listOf(
        Achievement(
            id = "first_step",
            titleRes = R.string.achievement_first_step_title,
            descRes = R.string.achievement_first_step_desc,
            icon = Icons.Default.Casino,
            iconGradient = Brush.linearGradient(
                colors = listOf(Color(0xFF81C784), Color(0xFF2E7D32))
            ),
            targetValue = 1,
            currentValue = 0,
            rewardPackId = null,
            rewardCredits = null,
            type = AchievementType.GAMES_PLAYED
        ),
        Achievement(
            id = "veteran",
            titleRes = R.string.achievement_veteran_title,
            descRes = R.string.achievement_veteran_desc,
            icon = Icons.Default.MilitaryTech,
            iconGradient = Brush.linearGradient(
                colors = listOf(Color(0xFF90A4AE), Color(0xFF455A64))
            ),
            targetValue = 100,
            currentValue = 0,
            rewardPackId = 102,
            rewardCredits = 5000,
            type = AchievementType.GAMES_PLAYED
        ),
        Achievement(
            id = "high_roller",
            titleRes = R.string.achievement_high_roller_title,
            descRes = R.string.achievement_high_roller_desc,
            icon = Icons.Default.MonetizationOn,
            iconGradient = Brush.linearGradient(
                colors = listOf(Color(0xFFFFD54F), Color(0xFFFF8F00))
            ),
            targetValue = 50000,
            currentValue = 0,
            rewardPackId = 103,
            rewardCredits = 10000,
            type = AchievementType.HIGHEST_CHIPS
        ),
        Achievement(
            id = "millionaire",
            titleRes = R.string.achievement_millionaire_title,
            descRes = R.string.achievement_millionaire_desc,
            icon = Icons.Default.WorkspacePremium,
            iconGradient = Brush.linearGradient(
                colors = listOf(Color(0xFFB3E5FC), Color(0xFF0288D1))
            ),
            targetValue = 1000000,
            currentValue = 0,
            rewardPackId = 104,
            rewardCredits = 20000,
            type = AchievementType.HIGHEST_CHIPS
        ),
        Achievement(
            id = "unstoppable",
            titleRes = R.string.achievement_unstoppable_title,
            descRes = R.string.achievement_unstoppable_desc,
            icon = Icons.Default.Star,
            iconGradient = Brush.linearGradient(
                colors = listOf(Color(0xFFFF8A65), Color(0xFFBF360C))
            ),
            targetValue = 500,
            currentValue = 0,
            rewardPackId = 105,
            rewardCredits = 30000,
            type = AchievementType.GAMES_PLAYED
        ),
        Achievement(
            id = "apprentice",
            titleRes = R.string.achievement_apprentice_title,
            descRes = R.string.achievement_apprentice_desc,
            icon = Icons.Default.Casino,
            iconGradient = Brush.linearGradient(
                colors = listOf(Color(0xFFCE93D8), Color(0xFF6A1B9A))
            ),
            targetValue = 5000,
            currentValue = 0,
            rewardPackId = 101, // Reusing Emerald Ace as a mid-tier reward
            rewardCredits = 500,
            type = AchievementType.CAREER_CHIPS
        ),
        Achievement(
            id = "hustler",
            titleRes = R.string.achievement_hustler_title,
            descRes = R.string.achievement_hustler_desc,
            icon = Icons.Default.MonetizationOn,
            iconGradient = Brush.linearGradient(
                colors = listOf(Color(0xFFFFF59D), Color(0xFFFBC02D))
            ),
            targetValue = 25000,
            currentValue = 0,
            rewardCredits = 2500,
            type = AchievementType.CAREER_CHIPS
        ),
        Achievement(
            id = "grinder",
            titleRes = R.string.achievement_grinder_title,
            descRes = R.string.achievement_grinder_desc,
            icon = Icons.Default.MilitaryTech,
            iconGradient = Brush.linearGradient(
                colors = listOf(Color(0xFFB0BEC5), Color(0xFF263238))
            ),
            targetValue = 100000,
            currentValue = 0,
            rewardCredits = 10000,
            type = AchievementType.CAREER_CHIPS
        ),
        Achievement(
            id = "legend",
            titleRes = R.string.achievement_legend_title,
            descRes = R.string.achievement_legend_desc,
            icon = Icons.Default.WorkspacePremium,
            iconGradient = Brush.linearGradient(
                colors = listOf(Color(0xFFFFCC80), Color(0xFFE65100))
            ),
            targetValue = 500000,
            currentValue = 0,
            rewardCredits = 20000,
            type = AchievementType.CAREER_CHIPS
        )
    )

    fun evaluateAchievements(
        gamesPlayed: Long,
        highestChips: Long,
        totalChips: Long,
        careerCredits: Long,
        claimedIds: List<String>
    ): List<Achievement> {
        return initialAchievements.map { achievement ->
            val currentValue = when (achievement.type) {
                AchievementType.GAMES_PLAYED -> gamesPlayed
                AchievementType.HIGHEST_CHIPS -> highestChips
                AchievementType.TOTAL_CHIPS -> totalChips
                AchievementType.CAREER_CHIPS -> careerCredits
            }
            achievement.copy(
                currentValue = currentValue.coerceIn(0, achievement.targetValue),
                isUnlocked = currentValue >= achievement.targetValue,
                isClaimed = claimedIds.contains(achievement.id)
            )
        }
    }
}
