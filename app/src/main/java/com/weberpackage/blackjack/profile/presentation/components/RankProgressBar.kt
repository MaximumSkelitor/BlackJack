package com.weberpackage.blackjack.profile.presentation.components

import android.content.res.Configuration
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.weberpackage.blackjack.R
import com.weberpackage.blackjack.common.presentation.theme.BlackJackTheme
import com.weberpackage.blackjack.dashboard.presentation.model.RankData
import com.weberpackage.blackjack.dashboard.presentation.utils.RankUtils

@Composable
fun RankProgressBar(
    rank: RankData?,
    progress: Float,
    modifier: Modifier = Modifier,
    isExpanded: Boolean = false,
    onClick: () -> Unit = {},
) {
    if (rank == null) return

    val arrowRotationDegree by animateFloatAsState(
        targetValue = if (isExpanded) 90f else 0f,
        animationSpec = tween(durationMillis = 200), // Speed of rotation
        label = "ArrowRotation"
    )

    Row(
        modifier = modifier
            .fillMaxWidth(0.8f)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
                shape = RoundedCornerShape(24.dp)
            )
            .padding(vertical = 8.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Start Icon
        Box(Modifier.size(40.dp)) {
            Icon(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer(alpha = 0.99f)
                    .drawWithContent {
                        drawContent()
                        drawRect(brush = rank.gradient, blendMode = BlendMode.SrcAtop)
                    },
                imageVector = rank.icon,
                contentDescription = "Rank icon",
                tint = Color.Unspecified
            )
        }

        // Rank Info Text
        Column(
            modifier = Modifier.padding(start = 8.dp)
        ) {
            Text(
                text = stringResource(rank.nameRes),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = stringResource(R.string.rank_progress),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Progress Indicator (Fills available space)
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .weight(1f)
                .height(7.dp)
                .padding(horizontal = 4.dp),
            trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
            strokeCap = StrokeCap.Round,
            gapSize = (-4).dp,
            drawStopIndicator = {}
        )

        Box(Modifier.size(25.dp)) {
            Icon(
                modifier = Modifier
                    .fillMaxSize()
                    .rotate(arrowRotationDegree),
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Rank icon",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
private fun RankProgressBarPreview() {
    BlackJackTheme {
        RankProgressBar(
            rank = RankUtils.ranks[1],
            progress = 0.25f
        )
    }
}