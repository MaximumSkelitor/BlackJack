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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Style
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.weberpackage.blackjack.R
import com.weberpackage.blackjack.coredata.PlayCard
import com.weberpackage.blackjack.coredata.Rank
import com.weberpackage.blackjack.coredata.Suit
import com.weberpackage.blackjack.screens.structure.BlackjackCard
import com.weberpackage.blackjack.screens.structure.cardColorStops
import com.weberpackage.blackjack.screens.structure.formatChips
import com.weberpackage.blackjack.ui.theme.BlackJackTheme
import kotlin.math.abs
import kotlin.math.min

data class CardPackItem(
    val id: Int,
    val nameResId: Int,
    val price: Int,
    val icon: ImageVector,
    val color: Color,
    val sampleCards: List<PlayCard>
)

val cardPacks = listOf(
    CardPackItem(
        id = 1,
        nameResId = R.string.card_pack_1,
        price = 500,
        icon = Icons.Default.Style,
        color = Color(0xFF4CAF50), // Green
        sampleCards = listOf(
            PlayCard(Suit.SPADES, Rank.ACE),
            PlayCard(Suit.HEARTS, Rank.KING)
        )
    ),
    CardPackItem(
        id = 2,
        nameResId = R.string.card_pack_2,
        price = 1500,
        icon = Icons.Default.AutoAwesome,
        color = Color(0xFF2196F3), // Blue
        sampleCards = listOf(
            PlayCard(Suit.CLUBS, Rank.ACE),
            PlayCard(Suit.DIAMONDS, Rank.TEN)
        )
    ),
    CardPackItem(
        id = 3,
        nameResId = R.string.card_pack_3,
        price = 5000,
        icon = Icons.Default.Diamond,
        color = Color(0xFF9C27B0), // Purple
        sampleCards = listOf(
            PlayCard(Suit.HEARTS, Rank.JACK),
            PlayCard(Suit.SPADES, Rank.QUEEN)
        )
    ),
    CardPackItem(
        id = 4,
        nameResId = R.string.card_pack_4,
        price = 15000,
        icon = Icons.Default.Verified,
        color = Color(0xFFFFC107), // Amber
        sampleCards = listOf(
            PlayCard(Suit.DIAMONDS, Rank.ACE),
            PlayCard(Suit.CLUBS, Rank.KING)
        )
    ),
    CardPackItem(
        id = 5,
        nameResId = R.string.card_pack_5,
        price = 50000,
        icon = Icons.Default.WorkspacePremium,
        color = Color(0xFFE91E63), // Pink/Red
        sampleCards = listOf(
            PlayCard(Suit.SPADES, Rank.ACE),
            PlayCard(Suit.HEARTS, Rank.ACE)
        )
    )
)

@Composable
fun CardPackSection(
    modifier: Modifier = Modifier,
    currentChips: Int,
    ownedPacks: List<Int>,
    onPurchase: (CardPackItem) -> Unit
) {
    val listState = rememberLazyListState()

    LazyRow(
        modifier = modifier.fillMaxWidth(),
        state = listState,
        flingBehavior = rememberSnapFlingBehavior(lazyListState = listState),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        itemsIndexed(cardPacks) { index, item ->
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

            val isOwned = ownedPacks.contains(item.id)

            CardPackPurchaseCard(
                item = item,
                canAfford = currentChips >= item.price,
                isOwned = isOwned,
                onClick = { onPurchase(item) },
                modifier = Modifier.graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                }
            )
        }
    }
}

@Composable
fun CardPackPurchaseCard(
    item: CardPackItem,
    canAfford: Boolean,
    isOwned: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cornerShape = RoundedCornerShape(24.dp)
    val cardColors = cardColorStops()

    Box(
        modifier = modifier
            .size(width = 240.dp, height = 240.dp)
            .clip(cornerShape)
            .background(Brush.verticalGradient(colorStops = cardColors))
            .clickable(enabled = canAfford && !isOwned) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(item.nameResId),
                color = MaterialTheme.colorScheme.tertiary,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Box(
                modifier = Modifier
                    .height(130.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                item.sampleCards.forEachIndexed { index, card ->
                    val rotation = if (index == 0) -12f else 12f
                    val xOffset = if (index == 0) (-25).dp else 25.dp
                    
                    BlackjackCard(
                        card = card,
                        packId = item.id,
                        modifier = Modifier
                            .graphicsLayer {
                                rotationZ = rotation
                                translationX = xOffset.toPx()
                                alpha = 1f
                                scaleX = 0.7f
                                scaleY = 0.7f
                            }
                            .zIndex(if (index == 0) 0f else 1f)
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (!isOwned) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "${formatChips(item.price)} ${stringResource(R.string.credits)}",
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }

                OutlinedButton(
                    onClick = onClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.dp),
                    enabled = canAfford && !isOwned,
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
                    Text(
                        text = when {
                            isOwned -> stringResource(R.string.owned)
                            canAfford -> stringResource(R.string.buy)
                            else -> stringResource(R.string.locked)
                        },
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = when {
                            isOwned -> MaterialTheme.colorScheme.onSurface
                            canAfford -> MaterialTheme.colorScheme.tertiary
                            else -> MaterialTheme.colorScheme.error.copy(.7f)
                        }
                    )
                }
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun CardPackSectionPreview() {
    BlackJackTheme {
        CardPackSection(currentChips = 1000, ownedPacks = listOf(1), onPurchase = {})
    }
}
