package com.weberpackage.blackjack.shop.presentation.components

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.weberpackage.blackjack.R
import com.weberpackage.blackjack.common.presentation.base.cardColorStops
import com.weberpackage.blackjack.common.presentation.base.formatChips
import com.weberpackage.blackjack.common.presentation.theme.BlackJackTheme
import com.weberpackage.blackjack.common.presentation.utils.AutoScaleText
import com.weberpackage.blackjack.shop.utils.DailyCreditsUtils
import com.weberpackage.blackjack.shop.presentation.contract.ShopContract
import kotlin.math.abs
import kotlin.math.min

data class CardSelectionItem(
    val id: Int,
    val titleResId: Int,
    val descResId: Int,
    val credits: Int,
    val image: ImageVector
)

@Composable
fun ShopSelectionRowContainer(
    state: ShopContract.State,
    onEventSent: (ShopContract.Event) -> Unit,
    modifier: Modifier = Modifier,
) {
    val itemsList = remember {
        DailyCreditsUtils.creditsPacks.map {
            CardSelectionItem(
                id = it.id,
                titleResId = it.nameRes,
                descResId = it.descRes,
                credits = it.credits,
                image = it.icon
            )
        }
    }

    val listState = rememberLazyListState()

    LazyRow(
        modifier = modifier.fillMaxWidth(),
        state = listState,
        flingBehavior = rememberSnapFlingBehavior(lazyListState = listState),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
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

            val canClaim = state.timeRemaining == 0L

            DailySelectionCardBuild(
                item = item,
                onClick = {
                    onEventSent(ShopContract.Event.OnClaimDailyCredits(item.credits))
                },
                modifier = Modifier.graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                },
                canClaim = canClaim,
                timeRemainingStr = DailyCreditsUtils.formatTimeRemaining(state.timeRemaining)
            )
        }
    }
}

@Composable
fun DailySelectionCardBuild(
    item: CardSelectionItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    canClaim: Boolean,
    timeRemainingStr: String
) {
    DailySelectionCard(
        title = stringResource(item.titleResId),
        desc = formatChips(item.credits),
        image = item.image,
        onClick = onClick,
        modifier = modifier,
        canClaim = canClaim,
        timeRemainingStr = timeRemainingStr
    )
}

@Composable
fun DailySelectionCard(
    modifier: Modifier = Modifier,
    title: String,
    desc: String,
    image: ImageVector,
    onClick: () -> Unit,
    showImage: Boolean = true,
    canClaim: Boolean,
    timeRemainingStr: String
) {
    val cornerShape = RoundedCornerShape(24.dp)
    val cardColors = cardColorStops()

    Box(
        modifier = modifier
            .shadow(
                elevation = 8.dp,
                shape = cornerShape
            )
            .size(width = 220.dp, height = 220.dp)
            .clip(cornerShape)
            .background(Brush.verticalGradient(colorStops = cardColors))
            .clickable(enabled = canClaim) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Title, Icon, Desc
            AutoScaleText(
                text = title,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
            if (showImage) {
                Icon(
                    modifier = Modifier
                        .height(80.dp)
                        .fillMaxWidth(),
                    imageVector = image,
                    contentDescription = null,
                    tint =
                        if (canClaim) {
                            MaterialTheme.colorScheme.onSurface
                        } else {
                            MaterialTheme.colorScheme.onSurface.copy(
                                0.4f
                            )
                        }
                )
            }
            // Credits text (e.g: 150 Credits)
            AutoScaleText(
                text = "$desc ${stringResource(R.string.credits)}",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.titleLarge
            )

            // Claim button
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                if (canClaim) {
                    Box {
                        OutlinedButton(
                            onClick = onClick,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(36.dp),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(0.dp),
                            border = BorderStroke(
                                width = 2.dp,
                                color = MaterialTheme.colorScheme.outline
                            ),
                            colors = ButtonDefaults.buttonColors(
                                containerColor =
                                    MaterialTheme.colorScheme.inverseOnSurface.copy(0.7f)
                            )
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                AutoScaleText(
                                    text = stringResource(R.string.claim),
                                    color = MaterialTheme.colorScheme.onSurface,
                                    style = MaterialTheme.typography.labelLarge.copy(fontSize = 18.sp),
                                    fontWeight = FontWeight.ExtraBold,
                                )
                            }
                        }
                    }
                } else {
                    // If the user can't claim the daily credits, it shows the remaining time
                    Box {
                        AutoScaleText(
                            text = timeRemainingStr,
                            color = MaterialTheme.colorScheme.onSurface.copy(0.8f),
                            fontWeight = FontWeight.Medium,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }
            }
        }
    }
}


@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
fun CardSectionPreview() {
    BlackJackTheme {
        ShopSelectionRowContainer(
            state = ShopContract.State(
                totalChips = 1000,
                ownedPacks = listOf(1),
                lastClaimTime = 0L,
                timeRemaining = 0L,
                isInitialLoading = false
            ),
            onEventSent = {}
        )
    }
}
