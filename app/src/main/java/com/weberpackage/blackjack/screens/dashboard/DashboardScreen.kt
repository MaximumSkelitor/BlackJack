package com.weberpackage.blackjack.screens.dashboard

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.weberpackage.blackjack.R
import com.weberpackage.blackjack.screens.dashboard.sections.rank_section.RankSection
import com.weberpackage.blackjack.screens.structure.ChipCounter
import com.weberpackage.blackjack.screens.structure.cardColorStops
import com.weberpackage.blackjack.ui.theme.BlackJackTheme
import com.weberpackage.blackjack.ui.theme.spacing
import kotlin.math.abs
import kotlin.math.min

// 1. Data model representation
data class SelectionItem(
    val id: Int,
    val titleResId: Int,
    val subtitleResId: Int,
    val image: ImageVector,
)

@Composable
private fun SelectionRowContainer(
    onNavigateToPlayNow: () -> Unit,
    onNavigateToPractice: () -> Unit,
    onNavigateToMultiplayer: () -> Unit,
    modifier: Modifier = Modifier
) {
    val itemsList = remember {
        listOf(
            SelectionItem(
                id = 1,
                titleResId = R.string.play_now_mode,
                subtitleResId = R.string.play_now_subtitle,
                image = Icons.Filled.Casino
            ),
            SelectionItem(
                id = 2,
                titleResId = R.string.practice_mode,
                subtitleResId = R.string.practice_subtitle,
                image = Icons.AutoMirrored.Filled.MenuBook,
            ),
            SelectionItem(
                id = 3,
                titleResId = R.string.multiplayer,
                subtitleResId = R.string.multiplayer_subtitle,
                image = Icons.Filled.People,
            ),
        )
    }

    val listState = rememberLazyListState()

    LazyRow(
        modifier = modifier.fillMaxWidth(),
        state = listState,
        flingBehavior = rememberSnapFlingBehavior(lazyListState = listState),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        itemsIndexed(itemsList) { index, item ->
            val scale by remember {
                derivedStateOf {
                    val layoutInfo = listState.layoutInfo
                    val visibleItemsInfo = layoutInfo.visibleItemsInfo
                    val itemInfo = visibleItemsInfo.firstOrNull { it.index == index }

                    if (itemInfo != null) {
                        val viewportCenter =
                            (layoutInfo.viewportEndOffset + layoutInfo.viewportStartOffset) / 2f
                        val itemCenter = itemInfo.offset + (itemInfo.size / 2f)
                        val distanceFromCenter = abs(viewportCenter - itemCenter)
                        val maxDistance = layoutInfo.viewportEndOffset / 2f
                        val progress = min(1f, distanceFromCenter / maxDistance)
                        1f - (progress * 0.15f)
                    } else {
                        0.85f
                    }
                }
            }

            SelectionCard(
                item = item,
                onClick = {
                    when (item.id) {
                        1 -> onNavigateToPlayNow()
                        2 -> onNavigateToPractice()
                        3 -> onNavigateToMultiplayer()
                    }
                },
                modifier = Modifier.graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                }
            )
        }
    }
}

@Composable
private fun SelectionCard(
    item: SelectionItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cornerShape = RoundedCornerShape(24.dp)
    Card(
        shape = cornerShape,
        modifier = modifier
            .background(color = Color.Transparent)
            .size(width = 300.dp, height = 225.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.inverseOnSurface
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(1f)
                .background(
                    Brush.horizontalGradient(
                        colorStops = cardColorStops()
                    ),
                    cornerShape
                )
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    modifier = Modifier.size(80.dp),
                    imageVector = item.image,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
                Column {
                    Text(
                        text = stringResource(item.titleResId),
                        color = MaterialTheme.colorScheme.tertiary,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 28.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stringResource(item.subtitleResId),
                        color = MaterialTheme.colorScheme.onSurface.copy(0.8f),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        lineHeight = 22.sp
                    )
                }
            }
        }
    }
}

@Composable
fun DashboardScreen(
    contentPadding: PaddingValues = PaddingValues(),
    onNavigateToPlayNow: () -> Unit,
    onNavigateToPractice: () -> Unit,
    onNavigateToMultiplayer: () -> Unit,
    userUsername: String,
    totalChips: Int
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = contentPadding,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Spacer(Modifier.height(MaterialTheme.spacing.smallTwo))
        }
        item {
            Text(
                text = stringResource(R.string.welcome_user, userUsername),
                style = MaterialTheme.typography.headlineMedium
            )
        }
        item {
            ChipCounter(count = totalChips, fontSize = 57)
        }
        item {
            SelectionRowContainer(
                onNavigateToPlayNow = onNavigateToPlayNow,
                onNavigateToPractice = onNavigateToPractice,
                onNavigateToMultiplayer = onNavigateToMultiplayer
            )
        }
        item {
            Spacer(Modifier.height(MaterialTheme.spacing.medium))
        }
        item {
            RankSection(
                userChips = totalChips,
                modifier = Modifier.padding(horizontal = MaterialTheme.spacing.smallThree)
            )
        }
        item {
            Spacer(Modifier.height(MaterialTheme.spacing.smallTwo))
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
private fun DashboardScreenPreview() {
    BlackJackTheme {
        DashboardScreen(
            onNavigateToPlayNow = {},
            onNavigateToPractice = {},
            onNavigateToMultiplayer = {},
            userUsername = "Player",
            totalChips = 1000
        )
    }
}