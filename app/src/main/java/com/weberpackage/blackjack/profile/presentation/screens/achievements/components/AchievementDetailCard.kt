package com.weberpackage.blackjack.profile.presentation.screens.achievements.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.weberpackage.blackjack.R
import com.weberpackage.blackjack.common.presentation.base.cardColorStops
import com.weberpackage.blackjack.common.presentation.base.formatChipsCompact
import com.weberpackage.blackjack.common.presentation.base.formatChipsLong
import com.weberpackage.blackjack.common.presentation.theme.BlackJackTheme
import com.weberpackage.blackjack.common.presentation.theme.completedCheck
import com.weberpackage.blackjack.common.presentation.utils.AutoScaleText
import com.weberpackage.blackjack.dashboard.presentation.components.BenefitItem
import com.weberpackage.blackjack.profile.presentation.screens.achievements.model.Achievement
import com.weberpackage.blackjack.profile.presentation.screens.achievements.model.AchievementType
import com.weberpackage.blackjack.shop.presentation.model.cardPacks
import kotlin.math.roundToInt

@Composable
fun AchievementDetailCard(
    modifier: Modifier = Modifier,
    achievement: Achievement,
) {
    val progress =
        (achievement.currentValue.toFloat() / achievement.targetValue.toFloat()).coerceIn(0f, 1f)
    val percentage = (progress * 100).roundToInt()

    val cornerShape = RoundedCornerShape(24.dp)
    Column(
        modifier = modifier
            .clip(cornerShape)
            .background(
                brush = Brush.verticalGradient(colorStops = cardColorStops()),
                shape = cornerShape
            )
            .padding(20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Icon
            Box(modifier = Modifier.size(80.dp)) {
                Icon(
                    imageVector = achievement.icon,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(if (achievement.isUnlocked) 1f else 0.5f)
                        .graphicsLayer(alpha = 0.99f)
                        .drawWithContent {
                            drawContent()
                            drawRect(
                                brush = achievement.iconGradient,
                                blendMode = BlendMode.SrcAtop
                            )
                        },
                    tint = Color.Unspecified
                )
                if (!achievement.isUnlocked) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Locked",
                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.BottomEnd),
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }

            Spacer(Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    AutoScaleText(
                        text = stringResource(achievement.titleRes),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (achievement.isUnlocked) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    if (achievement.isUnlocked) {
                        Spacer(Modifier.width(10.dp))
                        Icon(
                            imageVector = Icons.Default.Done,
                            contentDescription = "Done",
                            tint = completedCheck
                        )
                    } else {
                        Text(
                            text = "$percentage%",
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }

                Text(
                    text = stringResource(achievement.descRes),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface.copy(0.7f)
                )

                if (!achievement.isUnlocked) {
                    Spacer(Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(7.dp),
                        trackColor = MaterialTheme.colorScheme.onSurface.copy(0.1f),
                        color = MaterialTheme.colorScheme.onSurface.copy(.9f),
                        strokeCap = StrokeCap.Round,
                        gapSize = (-4).dp,
                        drawStopIndicator = {}
                    )
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
        ) {
            if (achievement.rewardPackId != null || achievement.rewardCredits != null) {
                // Rewards Section
                Text(
                    text = stringResource(R.string.rewards),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface.copy(0.8f)
                )

                Spacer(Modifier.height(12.dp))

                achievement.rewardPackId?.let { id ->
                    val rewardPack = cardPacks.find { it.id == id }
                    BenefitItem(
                        isUnlocked = achievement.isClaimed,
                        text = rewardPack?.let {
                            stringResource(
                                R.string.exclusive_card_pack,
                                stringResource(it.nameResId)
                            )
                        } ?: "Exclusive Rewards"
                    )
                }

                achievement.rewardCredits?.let { credits ->
                    BenefitItem(
                        isUnlocked = achievement.isClaimed,
                        text = stringResource(
                            R.string.credits_amount,
                            formatChipsCompact(credits)
                        )
                    )
                }

                Spacer(Modifier.height(24.dp))
            }

            // Requirements Section
            Text(
                text = stringResource(R.string.requirement),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface.copy(0.8f),
            )
            Spacer(Modifier.height(12.dp))

            val progressText = "${formatChipsLong(achievement.currentValue)}/${formatChipsLong(achievement.targetValue)}"
            val reqText = when (achievement.type) {
                AchievementType.GAMES_PLAYED -> stringResource(R.string.games_played_req, progressText)
                AchievementType.HIGHEST_CHIPS -> stringResource(R.string.highest_credits_req, progressText)
                AchievementType.TOTAL_CHIPS -> stringResource(R.string.total_credits_req, progressText)
                AchievementType.CAREER_CHIPS -> stringResource(R.string.career_credits_req, progressText)
            }

            BenefitItem(
                isUnlocked = achievement.isUnlocked,
                text = reqText
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun AchievementDetailCardPreview() {
    BlackJackTheme {
        AchievementDetailCard(
            achievement = Achievement(
                id = "first_step",
                titleRes = R.string.achievement_first_step_title,
                descRes = R.string.achievement_first_step_desc,
                icon = Icons.Default.Casino,
                iconGradient = Brush.linearGradient(listOf(Color.Green, Color.Black)),
                targetValue = 1,
                currentValue = 0,
                rewardPackId = 101,
                type = AchievementType.GAMES_PLAYED
            )
        )
    }
}
