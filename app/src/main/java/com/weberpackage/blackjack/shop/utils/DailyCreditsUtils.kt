package com.weberpackage.blackjack.coredata

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.ui.graphics.vector.ImageVector
import com.weberpackage.blackjack.R
import java.util.Locale

data class DailyCreditsData(
    val id: Int,
    val nameRes: Int,
    val descRes: Int,
    val credits: Int,
    val icon: ImageVector,
)

object DailyCreditsUtils {
    const val CLAIM_COOLDOWN_MS = 24 * 60 * 60 * 1000L // 24 hours

    val creditsPacks = listOf(
        DailyCreditsData(
            id = 1,
            nameRes = R.string.daily_credits,
            descRes = R.string.daily_credits_small,
            credits = 150,
            icon = Icons.Default.CalendarToday
        ),
    )

    fun getTimeRemaining(lastClaimTime: Long): Long {
        val currentTime = System.currentTimeMillis()
        val timePassed = currentTime - lastClaimTime
        return (CLAIM_COOLDOWN_MS - timePassed).coerceAtLeast(0L)
    }

    fun formatTimeRemaining(millis: Long): String {
        val hours = millis / (1000 * 60 * 60)
        val minutes = (millis / (1000 * 60)) % 60
        val seconds = (millis / 1000) % 60
        return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds)
    }
}