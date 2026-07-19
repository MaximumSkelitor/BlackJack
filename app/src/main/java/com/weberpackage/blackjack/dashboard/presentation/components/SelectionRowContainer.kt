package com.weberpackage.blackjack.dashboard.presentation.components

import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.People
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.weberpackage.blackjack.R
import com.weberpackage.blackjack.dashboard.presentation.model.SelectionItem
import kotlin.math.abs
import kotlin.math.min

@Composable
internal fun SelectionRowContainer(
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
                image = Icons.Filled.Casino,
                tint = Color(0xFF74B765)
            ),
            SelectionItem(
                id = 2,
                titleResId = R.string.practice_mode,
                subtitleResId = R.string.practice_subtitle,
                image = Icons.AutoMirrored.Filled.MenuBook,
                tint = Color(0xFF9979CE)
            ),
            SelectionItem(
                id = 3,
                titleResId = R.string.multiplayer,
                subtitleResId = R.string.multiplayer_subtitle,
                image = Icons.Filled.People,
                tint = Color(0xFF66799B)
            ),
        )
    }

    val listState = rememberLazyListState()

    LazyRow(
        modifier = modifier.fillMaxWidth(),
        state = listState,
        flingBehavior = rememberSnapFlingBehavior(lazyListState = listState),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 20.dp),
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