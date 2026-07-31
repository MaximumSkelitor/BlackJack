package com.weberpackage.blackjack.profile.presentation.screens.achievements.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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
import androidx.compose.ui.unit.sp
import com.weberpackage.blackjack.R
import com.weberpackage.blackjack.common.presentation.base.cardColorStops
import com.weberpackage.blackjack.common.presentation.base.formatChipsLong
import com.weberpackage.blackjack.common.presentation.theme.BlackJackTheme
import com.weberpackage.blackjack.common.presentation.theme.completedCheck
import com.weberpackage.blackjack.common.presentation.utils.AutoScaleText
import com.weberpackage.blackjack.profile.presentation.screens.achievements.model.Achievement
import com.weberpackage.blackjack.profile.presentation.screens.achievements.model.AchievementType


@Composable
fun AchievementItem(
    achievement: Achievement,
    onClaimClicked: () -> Unit,
    onClick: () -> Unit
) {
    val progress =
        (achievement.currentValue.toFloat() / achievement.targetValue.toFloat()).coerceIn(0f, 1f)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .background(
                brush = Brush.horizontalGradient(colorStops = cardColorStops()),
                shape = RoundedCornerShape(24.dp)
            )
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = if (achievement.isUnlocked) 0.6f else 0.2f),
                shape = RoundedCornerShape(24.dp)
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(50.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = achievement.icon,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(if (achievement.isUnlocked) 1f else 0.3f)
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
                    contentDescription = null,
                    modifier = Modifier
                        .size(16.dp)
                        .align(Alignment.BottomEnd),
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom
            ) {
                // Title and desc
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = stringResource(achievement.titleRes),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (achievement.isUnlocked) {
                                MaterialTheme.colorScheme.tertiary
                            } else {
                                MaterialTheme.colorScheme.onSurface
                            }
                        )
                        if (achievement.isUnlocked) {
                            Spacer(Modifier.width(7.dp))
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Done",
                                tint = completedCheck
                            )
                        }
                    }

                    Text(
                        text = stringResource(achievement.descRes),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }

                Spacer(Modifier.weight(1f))

                // Claim button
                if (achievement.isUnlocked && (achievement.rewardPackId != null || achievement.rewardCredits != null)) {
                    Button(
                        modifier = Modifier
                            .border(
                                width = 2.dp,
                                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
                                shape = RoundedCornerShape(24.dp)
                            )
                            .sizeIn(maxWidth = 75.dp, maxHeight = 30.dp),
                        onClick = onClaimClicked,
                        enabled = !achievement.isClaimed,
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent.copy(0f),
                            disabledContainerColor = Color.Transparent.copy(0f)
                        ),
                        contentPadding = PaddingValues(3.dp)
                    ) {
                        AutoScaleText(
                            modifier = Modifier.padding(horizontal = 5.dp),
                            text = if (achievement.isClaimed) {
                                stringResource(R.string.claimed)
                            } else {
                                stringResource(R.string.claim)
                            },
                            color = if (achievement.isClaimed) {
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            } else {
                                MaterialTheme.colorScheme.onSurface
                            }
                        )
                    }
                }
            }


            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .weight(1f)
                        .height(6.dp),
                    trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                    color = MaterialTheme.colorScheme.onSurface,
                    gapSize = (-4).dp,
                    strokeCap = StrokeCap.Round,
                    drawStopIndicator = {}
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${formatChipsLong(achievement.currentValue)}/${
                        formatChipsLong(
                            achievement.targetValue
                        )
                    }",
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
private fun AchievementItemPreview() {
    BlackJackTheme {
        AchievementItem(
            achievement =
                Achievement(
                    id = "first_step",
                    titleRes = R.string.achievement_first_step_title,
                    descRes = R.string.achievement_first_step_desc,
                    icon = Icons.Default.Casino,
                    iconGradient = Brush.linearGradient(
                        colors = listOf(Color(0xFF81C784), Color(0xFF2E7D32))
                    ),
                    targetValue = 1,
                    currentValue = 1,
                    isUnlocked = true,
                    isClaimed = true,
                    type = AchievementType.GAMES_PLAYED
                ),
            onClaimClicked = {},
            onClick = {}
        )
    }
}