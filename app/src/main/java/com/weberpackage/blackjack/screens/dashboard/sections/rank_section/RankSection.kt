package com.weberpackage.blackjack.screens.dashboard.sections.rank_section

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.weberpackage.blackjack.R
import com.weberpackage.blackjack.coredata.PreferenceManager
import com.weberpackage.blackjack.screens.structure.cardColorStops
import com.weberpackage.blackjack.screens.structure.formatChips
import com.weberpackage.blackjack.ui.theme.BlackJackTheme

@Composable
fun RankRow(
    nameRes: Int,
    descRes: Int,
    icon: ImageVector,
    isLocked: Boolean,
    requiredChips: Int,
    multiplier: Float,
    isExpanded: Boolean,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }

    val arrowRotationDegree by animateFloatAsState(
        targetValue = if (isExpanded) 0f else -90f,
        animationSpec = tween(durationMillis = 200), // Speed of rotation
        label = "ArrowRotation"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(vertical = 8.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon with Lock Overlay
            Box(
                modifier = Modifier.size(60.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(if (isLocked) 0.5f else 1f),
                    tint = MaterialTheme.colorScheme.onSurface
                )
                if (isLocked) {
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
                    .alpha(if (isLocked) 0.5f else 1f)
            ) {
                Text(
                    text = stringResource(nameRes),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(descRes),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(0.7f)
                )
            }

            // Expand/Action Icon
            Icon(
                modifier = Modifier
                    .alpha(if (isLocked) 0.5f else 1f)
                    .rotate(arrowRotationDegree),
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        AnimatedVisibility(
            visible = isExpanded,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 76.dp, top = 8.dp)
            ) {
                Row {
                    Text(
                        text = stringResource(R.string.benefits),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = stringResource(
                            R.string.rank_benefit_multiplier,
                            multiplier,
                            stringResource(R.string.credits)
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Normal
                    )
                }
                Row {
                    Text(
                        text = stringResource(R.string.req_credits),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = formatChips(requiredChips),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        }
    }
    HorizontalDivider(
        Modifier.padding(top = 5.dp),
        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
    )
}

@Composable
fun RankSection(
    userChips: Int,
    modifier: Modifier = Modifier,
    onRankClick: () -> Unit = {}
) {
    var expandedRankIndex by remember { mutableStateOf<Int?>(null) }
    val ranks = RankUtils.ranks

    Card(
        shape = RoundedCornerShape(24.dp),
        modifier = modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent, // Allow gradient to show
            contentColor = contentColorFor(MaterialTheme.colorScheme.background)
        ),

        ) {
        Box(
            modifier = Modifier
                .background(Brush.horizontalGradient(colorStops = cardColorStops()))
                .animateContentSize()
                .padding(16.dp)
        ) {
            Column {
                Text(
                    text = stringResource(R.string.your_rank),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.padding(bottom = 12.dp),
                    color = MaterialTheme.colorScheme.tertiary
                )

                ranks.forEachIndexed { index, rank ->
                    // Determine lock status based on user chips
                    val isUnlocked = userChips >= rank.requiredChips
                    RankRow(
                        nameRes = rank.nameRes,
                        descRes = rank.descRes,
                        icon = rank.icon,
                        isLocked = !isUnlocked,
                        requiredChips = rank.requiredChips,
                        multiplier = rank.multiplier,
                        isExpanded = expandedRankIndex == index,
                        onClick = {
                            expandedRankIndex = if (expandedRankIndex == index) null else index
                            onRankClick()
                        }
                    )
                }
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
private fun RankSectionPreview() {
    val context = LocalContext.current
    val preferenceManager = remember { PreferenceManager(context) }
    val totalChips = preferenceManager.getChips()
    BlackJackTheme {
        RankSection(userChips = totalChips)
    }
}