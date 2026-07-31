package com.weberpackage.blackjack.dashboard.presentation.components

import android.content.res.Configuration
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Lock
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.weberpackage.blackjack.common.presentation.theme.BlackJackTheme
import com.weberpackage.blackjack.common.presentation.theme.completedCheck
import com.weberpackage.blackjack.dashboard.presentation.model.RankData
import com.weberpackage.blackjack.dashboard.presentation.utils.RankUtils
import kotlin.math.roundToInt

@Composable
fun RankRow(
    rank: RankData,
    isLocked: Boolean,
    isPreviousUnlocked: Boolean,
    userChips: Int,
    userGamesPlayed: Long,
    prevRank: RankData?,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val progress = remember(userChips, userGamesPlayed, rank, prevRank) {
        RankUtils.getRankProgress(
            rank = rank,
            userChips = userChips,
            userGamesPlayed = userGamesPlayed,
            prevRank = prevRank
        )
    }

    val updatedProgress = (progress * 100).roundToInt()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(vertical = 8.dp)
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.outlineVariant.copy(.3f),
                shape = RoundedCornerShape(24.dp)
            )
            .padding(vertical = 8.dp, horizontal = 5.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon with Lock Overlay
            Box(
                modifier = Modifier.size(60.dp)
            ) {
                Icon(
                    imageVector = rank.icon,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(if (!isPreviousUnlocked) 0.5f else 1f)
                        .graphicsLayer(alpha = 0.99f)
                        .drawWithContent {
                            drawContent()
                            drawRect(brush = rank.gradient, blendMode = BlendMode.SrcAtop)
                        },
                    tint = Color.Unspecified
                )
                if (!isPreviousUnlocked) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Locked",
                        modifier = Modifier
                            .size(20.dp)
                            .align(Alignment.BottomEnd)
                            .alpha(1f),
                        tint = MaterialTheme.colorScheme.errorContainer
                    )
                }
            }

            Spacer(Modifier.width(16.dp))

            // Text Content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .alpha(if (!isPreviousUnlocked) 0.5f else 1f)
            ) {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(rank.nameRes),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                    )
                    if (!isLocked) {
                        Spacer(Modifier.width(5.dp))
                        Icon(
                            imageVector = Icons.Default.Done,
                            contentDescription = "Done",
                            tint = completedCheck
                        )
                    }
                }

                if (!isLocked) {
                    Text(
                        text = stringResource(rank.descRes),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(0.7f)
                    )
                } else if (isPreviousUnlocked) {

                    Row(
                        Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier
                                .padding(top = 5.dp)
                                .width(200.dp)
                                .height(7.dp),
                            trackColor = MaterialTheme.colorScheme.onSurface.copy(0.1f),
                            color = MaterialTheme.colorScheme.onSurface.copy(.9f),
                            strokeCap = StrokeCap.Round,
                            gapSize = (-4).dp,
                            drawStopIndicator = {}
                        )
//                        Spacer(Modifier.width(10.dp))
//                        Text(
//                            text = "$updatedProgress%",
//                            color = MaterialTheme.colorScheme.onSurface,
//                            fontWeight = FontWeight.SemiBold
//                        )
                    }

                } else {
                    Text(
                        text = stringResource(rank.descRes),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(0.7f)
                    )
                }
            }

            // Percentage text
            if (isPreviousUnlocked && isLocked) {
                Text(
                    text = "$updatedProgress%",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(start = 10.dp, end = 4.dp)
                )
            }

            // Action Icon
            Icon(
                modifier = Modifier
                    .alpha(if (!isPreviousUnlocked) 0.5f else 1f),
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
private fun RankSectionPreview() {
    BlackJackTheme {
        RankSection(userChips = 1000, userGamesPlayed = 25)
    }
}
