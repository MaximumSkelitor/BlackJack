package com.weberpackage.blackjack.dashboard.presentation.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.HorizontalDivider
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
import com.weberpackage.blackjack.common.presentation.base.formatChips
import com.weberpackage.blackjack.common.presentation.theme.BlackJackTheme
import com.weberpackage.blackjack.common.presentation.theme.completedCheck
import com.weberpackage.blackjack.dashboard.presentation.model.RankData
import com.weberpackage.blackjack.dashboard.presentation.utils.RankUtils
import kotlin.math.roundToInt

@Composable
fun RankDetailCard(
    modifier: Modifier = Modifier,
    rank: RankData,
    userChips: Int,
    userGamesPlayed: Long,
    prevRank: RankData? = null,
) {
    val prevUnlocked = prevRank == null || (userChips >= prevRank.requiredChips && userGamesPlayed >= prevRank.requiredGamesPlayed)
    val isUnlocked = userChips >= rank.requiredChips && userGamesPlayed >= rank.requiredGamesPlayed

    val progress = RankUtils.getRankProgress(
        rank = rank,
        userChips = userChips,
        userGamesPlayed = userGamesPlayed,
        prevRank = prevRank
    )

    val cornerShape = RoundedCornerShape(24.dp)
    Column(
        modifier = modifier
            .clip(cornerShape)
            .background(
                brush = Brush.verticalGradient(colorStops = cardColorStops()),
                shape = cornerShape
            )
            .padding(20.dp)
            .height(IntrinsicSize.Max)
            .width(IntrinsicSize.Max)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Icon
            Box(modifier = Modifier.size(80.dp)) {
                Icon(
                    imageVector = rank.icon,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(if (!isUnlocked) 0.5f else 1f)
                        .graphicsLayer(alpha = 0.99f)
                        .drawWithContent {
                            drawContent()
                            drawRect(brush = rank.gradient, blendMode = BlendMode.SrcAtop)
                        },
                    tint = Color.Unspecified
                )
                if (!isUnlocked) {
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
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(rank.nameRes),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (isUnlocked) {
                        Spacer(Modifier.width(10.dp))
                        Icon(
                            imageVector = Icons.Default.Done,
                            contentDescription = "Done",
                            tint = completedCheck
                        )
                    } else if (prevUnlocked) {
                        val updatedProgress = (progress * 100).roundToInt()
                        Spacer(Modifier.weight(1f))
                        Text(
                            text = "$updatedProgress%",
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                if (isUnlocked) {
                    Text(
                        text = stringResource(rank.descRes),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface.copy(0.7f)
                    )
                } else if (prevUnlocked) {
                    Spacer(Modifier.height(8.dp))
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
                } else {
                    Text(
                        text = stringResource(rank.descRes),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface.copy(0.7f)
                    )
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        // Benefits
        Text(
            text = stringResource(R.string.benefits),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface.copy(0.8f)
        )

        Spacer(Modifier.height(12.dp))

        // Multiplier
        BenefitItem(
            isUnlocked = isUnlocked,
            text = stringResource(
                R.string.rank_benefit_multiplier,
                rank.multiplier,
                stringResource(R.string.credits)
            )
        )

        // Requirements
        Text(
            text = stringResource(R.string.requirements),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface.copy(0.8f),
            modifier = Modifier.padding(vertical = 12.dp)
        )

        val reqChips = rank.requiredChips
        val reqGames = rank.requiredGamesPlayed

        // Req credits
        BenefitItem(
            isUnlocked = userChips >= reqChips,
            text = "Required Credits: ${formatChips(reqChips)}"
        )

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp),
            color = MaterialTheme.colorScheme.onSurface.copy(0.05f)
        )

        // Req games
        BenefitItem(
            isUnlocked = userGamesPlayed >= reqGames,
            text = "Required Games: $reqGames"
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
private fun RankDetailCardPreview() {
    BlackJackTheme {
        RankDetailCard(
            rank = RankUtils.ranks[1],
            userChips = 1000,
            userGamesPlayed = 10,
            prevRank = RankUtils.ranks.getOrNull(0)
        )
    }
}
