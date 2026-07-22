package com.weberpackage.blackjack.dashboard.presentation.components

import android.app.Activity
import android.content.ContextWrapper
import android.content.res.Configuration
import android.os.Build
import android.view.View
import android.view.Window
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogWindowProvider
import com.weberpackage.blackjack.common.presentation.theme.BlackJackTheme
import com.weberpackage.blackjack.dashboard.presentation.utils.RankUtils
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RankDetailBottomSheet(
    initialRankIndex: Int,
    userChips: Int,
    userGamesPlayed: Long,
    onDismissRequest: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        dragHandle = {
            Box(
                Modifier
                    .padding(vertical = 12.dp)
                    .size(width = 32.dp, height = 4.dp)
                    .background(
                        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                        CircleShape
                    )
            )
        },
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        val view = LocalView.current
        DisposableEffect(Unit) {
            val window = findWindow(view)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                window?.isNavigationBarContrastEnforced = false
            }

            onDispose {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    window?.isNavigationBarContrastEnforced = false
                }
            }
        }
        RankDetailContent(
            initialRankIndex = initialRankIndex,
            userChips = userChips,
            userGamesPlayed = userGamesPlayed
        )
    }
}

private fun findWindow(view: View): Window? {
    var currentView: View? = view
    while (currentView != null) {
        if (currentView is DialogWindowProvider) return currentView.window
        currentView = currentView.parent as? View
    }

    var currentContext = view.context
    while (currentContext is ContextWrapper) {
        if (currentContext is Activity) return currentContext.window
        currentContext = currentContext.baseContext
    }
    return null
}

@Composable
fun RankDetailContent(
    initialRankIndex: Int,
    userChips: Int,
    userGamesPlayed: Long,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val ranks = RankUtils.ranks
        val listState = rememberLazyListState()
        val snapFlingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

        LaunchedEffect(initialRankIndex) {
            listState.scrollToItem(initialRankIndex)
        }

        LazyRow(
            state = listState,
            flingBehavior = snapFlingBehavior,
            contentPadding = PaddingValues(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            itemsIndexed(ranks) { index, rank ->
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
                            val progress = (distanceFromCenter / maxDistance).coerceIn(0f, 1f)
                            1f - (progress * 0.1f)
                        } else {
                            0.9f
                        }
                    }
                }

                RankDetailCard(
                    rank = rank,
                    prevRank = ranks.getOrNull(index - 1),
                    userChips = userChips,
                    userGamesPlayed = userGamesPlayed,
                    modifier = Modifier
                        .width(320.dp)
                        .height(IntrinsicSize.Max)
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                        }
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        // Page Indicator
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ranks.forEachIndexed { index, _ ->
                val isSelected by remember {
                    derivedStateOf {
                        val layoutInfo = listState.layoutInfo
                        val visibleItemsInfo = layoutInfo.visibleItemsInfo
                        val center =
                            (layoutInfo.viewportEndOffset + layoutInfo.viewportStartOffset) / 2f

                        val closest = visibleItemsInfo.minByOrNull {
                            abs(it.offset + it.size / 2f - center)
                        }
                        closest?.index == index
                    }
                }

                val animatedColor by animateColorAsState(
                    targetValue = if (isSelected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant.copy(.1f),
                    animationSpec = tween(
                        durationMillis = 300, // Duration of the fade
                        easing = FastOutSlowInEasing // Smooth acceleration/deceleration
                    ),
                    label = "ColorFadeAnimation"
                )

                Box(
                    modifier = Modifier
                        .size(if (isSelected) 8.dp else 6.dp)
                        .clip(CircleShape)
                        .background(animatedColor)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
private fun RankDetailSheetPreview() {
    BlackJackTheme {
        RankDetailContent(
            initialRankIndex = 1,
            userChips = 1000,
            userGamesPlayed = 1
        )
    }
}