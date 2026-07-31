package com.weberpackage.blackjack.profile.presentation.screens.achievements.components

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
import com.weberpackage.blackjack.profile.presentation.screens.achievements.model.Achievement
import com.weberpackage.blackjack.profile.presentation.screens.achievements.utils.AchievementUtils
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AchievementDetailBottomSheet(
    achievements: List<Achievement>,
    initialIndex: Int,
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
        containerColor = MaterialTheme.colorScheme.background,
        scrimColor = MaterialTheme.colorScheme.scrim.copy(.63f)
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
        AchievementDetailContent(
            achievements = achievements,
            initialIndex = initialIndex
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
fun AchievementDetailContent(
    achievements: List<Achievement>,
    initialIndex: Int,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val listState = rememberLazyListState()
        val snapFlingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

        LaunchedEffect(initialIndex) {
            listState.scrollToItem(initialIndex)
        }

        LazyRow(
            state = listState,
            flingBehavior = snapFlingBehavior,
            contentPadding = PaddingValues(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            itemsIndexed(achievements) { index, achievement ->
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

                AchievementDetailCard(
                    achievement = achievement,
                    modifier = Modifier
                        .width(340.dp)
                        .height(380.dp)
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
            achievements.forEachIndexed { index, _ ->
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
                        durationMillis = 300,
                        easing = FastOutSlowInEasing
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

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun AchievementDetailSheetPreview() {
    BlackJackTheme {
        AchievementDetailContent(
            achievements = AchievementUtils.initialAchievements,
            initialIndex = 0
        )
    }
}
