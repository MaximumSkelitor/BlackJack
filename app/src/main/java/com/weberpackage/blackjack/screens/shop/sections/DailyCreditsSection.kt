package com.weberpackage.blackjack.screens.shop.sections

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.weberpackage.blackjack.R
import com.weberpackage.blackjack.coredata.DailyCreditsUtils
import com.weberpackage.blackjack.coredata.PreferenceManager
import com.weberpackage.blackjack.screens.structure.cardColorStops
import com.weberpackage.blackjack.screens.structure.formatChips
import com.weberpackage.blackjack.ui.theme.BlackJackTheme
import kotlinx.coroutines.delay
import kotlin.math.abs
import kotlin.math.min
import kotlin.time.Duration.Companion.milliseconds

data class CardSelectionItem(
    val id: Int,
    val titleResId: Int,
    val descResId: Int,
    val credits: Int,
    val image: ImageVector
)

@Composable
fun ShopSelectionRowContainer(
    modifier: Modifier = Modifier,
    onClaimed: () -> Unit = {}
) {
    val context = LocalContext.current
    val preferenceManager = remember { PreferenceManager(context) }
    var lastClaimTime by remember { mutableLongStateOf(preferenceManager.getLastClaimTime()) }
    var timeRemaining by remember {
        mutableLongStateOf(
            DailyCreditsUtils.getTimeRemaining(
                lastClaimTime
            )
        )
    }

    LaunchedEffect(lastClaimTime) {
        while (true) {
            timeRemaining = DailyCreditsUtils.getTimeRemaining(lastClaimTime)
            delay(1000.milliseconds)
        }
    }

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

            val canClaim = timeRemaining == 0L

            DailySelectionCardBuild(
                item = item,
                onClick = {
                    if (canClaim) {
                        val currentTime = System.currentTimeMillis()
                        preferenceManager.saveLastClaimTime(currentTime)
                        lastClaimTime = currentTime
                        val currentChips = preferenceManager.getChips()
                        preferenceManager.saveChips(currentChips + item.credits)
                        onClaimed()
                    }
                },
                modifier = Modifier.graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                },
                canClaim = canClaim,
                timeRemainingStr = DailyCreditsUtils.formatTimeRemaining(timeRemaining)
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
    Box(
        modifier = modifier
            .size(width = 170.dp, height = 170.dp)
            .background(Brush.horizontalGradient(colorStops = cardColorStops()), cornerShape)
            .clickable(enabled = canClaim) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 5.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Title, Icon, Desc
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )
            if (showImage) {
                Icon(
                    modifier = Modifier.size(60.dp),
                    imageVector = image,
                    contentDescription = null,
                    tint = if (canClaim) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(
                        0.4f
                    )
                )
            }
            Text(
                text = "$desc ${stringResource(R.string.credits)}",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.SemiBold
            )

            // Claim button
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                if (canClaim) {
                    Box {
                        OutlinedCard(
                            modifier = Modifier
                                .width(100.dp)
                                .height(30.dp),
                            shape = RoundedCornerShape(24.dp),
                            border = BorderStroke(
                                width = 2.dp,
                                color = MaterialTheme.colorScheme.outline
                            ),
                            colors = CardDefaults.outlinedCardColors(
                                containerColor = MaterialTheme.colorScheme.inverseOnSurface.copy(
                                    0.7f
                                )
                            )
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Text(
                                    text = stringResource(R.string.claim),
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    modifier = Modifier
                                )
                            }

                        }
                    }
                } else {
                    // If the user can't claim the daily credits, it shows the remaining time
                    Box {
                        Text(
                            text = timeRemainingStr,
                            color = MaterialTheme.colorScheme.onSurface.copy(0.8f),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
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
        ShopSelectionRowContainer()
    }
}