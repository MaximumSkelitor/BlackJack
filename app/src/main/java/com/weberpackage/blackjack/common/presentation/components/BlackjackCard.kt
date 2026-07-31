package com.weberpackage.blackjack.common.presentation.components

import android.content.res.Configuration
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.weberpackage.blackjack.common.presentation.model.PlayCard
import com.weberpackage.blackjack.common.presentation.model.Rank
import com.weberpackage.blackjack.common.presentation.model.Suit
import com.weberpackage.blackjack.common.presentation.theme.BlackJackTheme


@Composable
fun BlackjackCard(
    card: PlayCard,
    modifier: Modifier = Modifier,
    packId: Int = 1
) {
    // Automatically determine suit color and display symbol
    val (suitSymbol, defaultColor) = when (card.suit) {
        Suit.CLUBS -> "♣" to Color.Black
        Suit.SPADES -> "♠" to Color.Black
        Suit.HEARTS -> "♥" to Color(0xFFD32F2F)
        Suit.DIAMONDS -> "♦" to Color(0xFFD32F2F)
    }

    // Pack-specific card styling
    val cardBackgroundBrush = when (packId) {
        2 -> Brush.verticalGradient(listOf(Color(0xFFE3F2FD), Color(0xFFBBDEFB))) // Blue tier
        3 -> Brush.verticalGradient(listOf(Color(0xFFF3E5F5), Color(0xFFE1BEE7))) // Purple tier
        4 -> Brush.verticalGradient(listOf(Color(0xFFFFF8E1), Color(0xFFFFECB3))) // Gold tier
        5 -> Brush.verticalGradient(listOf(Color(0xFFFFEBEE), Color(0xFFFFCDD2))) // Red tier
        101 -> Brush.verticalGradient(listOf(Color(0xFFE8F5E9), Color(0xFFC8E6C9))) // Emerald Ace
        102 -> Brush.verticalGradient(listOf(Color(0xFFFFEBEE), Color(0xFFFFCDD2))) // Ruby Dealer
        103 -> Brush.verticalGradient(listOf(Color(0xFFFFF8E1), Color(0xFFFFECB3))) // Gold Rush
        104 -> Brush.verticalGradient(listOf(Color(0xFFF3E5F5), Color(0xFFE1BEE7))) // Royal Flush
        else -> Brush.verticalGradient(listOf(Color.White, Color.White))
    }

    val cardBorderColor = when (packId) {
        1 -> Color.LightGray
        2 -> Color(0xFF2196F3)
        3 -> Color(0xFF9C27B0)
        4 -> Color(0xFFFFC107)
        5 -> Color(0xFFE91E63)
        101 -> Color(0xFF2E7D32)
        102 -> Color(0xFFC62828)
        103 -> Color(0xFFFFB300)
        104 -> Color(0xFF6A1B9A)
        else -> Color.Transparent
    }

    val cardColor =
        if (packId >= 4 && defaultColor == Color.Black) Color(0xFF212121) else defaultColor

    // Convert enum Rank to string presentation (e.g., ACE -> "A", TWO -> "2")
    val displayRank = when (card.rank) {
        Rank.ACE -> "A"
        Rank.KING -> "K"
        Rank.QUEEN -> "Q"
        Rank.JACK -> "J"
        else -> card.rank.value.toString()
    }

    Box(
        modifier = modifier
            .size(width = 100.dp, height = 145.dp)
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(16.dp),
                clip = false,
                ambientColor = cardBorderColor,
                spotColor = Color.Black
            )
            .background(
                brush = cardBackgroundBrush,
                shape = RoundedCornerShape(16.dp)
            )
            .then(
                if (packId == 1) {
                    Modifier.border(
                        3.dp,
                        cardBorderColor,
                        RoundedCornerShape(16.dp)
                    )
                } else {
                    Modifier.border(
                        2.dp,
                        cardBorderColor.copy(alpha = 0.8f),
                        RoundedCornerShape(16.dp)
                    )
                }
            )
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = displayRank,
                fontSize = 42.sp,
                fontWeight = FontWeight.Bold,
                color = cardColor,
                lineHeight = 42.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = suitSymbol,
                fontSize = 28.sp,
                color = cardColor.copy(.9f)
            )
        }
    }
}


@Composable
fun DownBlackJackCard(
    modifier: Modifier = Modifier,
    packId: Int = 1
) {
    // Pack-specific card styling for the back
    val cardBackgroundBrush = when (packId) {
        2 -> Brush.verticalGradient(listOf(Color(0xFFE3F2FD), Color(0xFFBBDEFB))) // Blue tier
        3 -> Brush.verticalGradient(listOf(Color(0xFFF3E5F5), Color(0xFFE1BEE7))) // Purple tier
        4 -> Brush.verticalGradient(listOf(Color(0xFFFFF8E1), Color(0xFFFFECB3))) // Gold tier
        5 -> Brush.verticalGradient(listOf(Color(0xFFFFEBEE), Color(0xFFFFCDD2))) // Red tier
        101 -> Brush.verticalGradient(listOf(Color(0xFFE8F5E9), Color(0xFFC8E6C9))) // Emerald Ace
        102 -> Brush.verticalGradient(listOf(Color(0xFFFFEBEE), Color(0xFFFFCDD2))) // Ruby Dealer
        103 -> Brush.verticalGradient(listOf(Color(0xFFFFF8E1), Color(0xFFFFECB3))) // Gold Rush
        104 -> Brush.verticalGradient(listOf(Color(0xFFF3E5F5), Color(0xFFE1BEE7))) // Royal Flush
        else -> Brush.verticalGradient(listOf(Color.White, Color.White))
    }

    val cardBorderColor = when (packId) {
        1 -> Color.LightGray
        2 -> Color(0xFF2196F3)
        3 -> Color(0xFF9C27B0)
        4 -> Color(0xFFFFC107)
        5 -> Color(0xFFE91E63)
        101 -> Color(0xFF2E7D32)
        102 -> Color(0xFFC62828)
        103 -> Color(0xFFFFB300)
        104 -> Color(0xFF6A1B9A)
        else -> Color.Transparent
    }

    val patternColor = when (packId) {
        2 -> Color(0xFF2196F3).copy(alpha = 0.2f)
        3 -> Color(0xFF9C27B0).copy(alpha = 0.2f)
        4 -> Color(0xFFFFC107).copy(alpha = 0.2f)
        5 -> Color(0xFFE91E63).copy(alpha = 0.2f)
        101 -> Color(0xFF2E7D32).copy(alpha = 0.2f)
        102 -> Color(0xFFC62828).copy(alpha = 0.2f)
        103 -> Color(0xFFFFB300).copy(alpha = 0.2f)
        104 -> Color(0xFF6A1B9A).copy(alpha = 0.2f)
        else -> Color.Gray.copy(alpha = 0.15f)
    }

    Box(
        modifier = modifier
            .size(width = 100.dp, height = 145.dp)
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(16.dp),
                clip = false,
                ambientColor = if (packId > 1) cardBorderColor else Color.Black,
                spotColor = cardBorderColor
            )
            .background(
                brush = cardBackgroundBrush,
                shape = RoundedCornerShape(16.dp)
            )
            .then(
                if (packId > 1) Modifier.border(
                    2.dp,
                    cardBorderColor.copy(alpha = 0.5f),
                    RoundedCornerShape(16.dp)
                )
                else Modifier.border(
                    3.dp,
                    cardBorderColor,
                    RoundedCornerShape(16.dp)
                )
            )
            .clip(RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val step = 9.dp.toPx()
            val lineWidth = 3.dp.toPx()

            // Draw first set of diagonal lines (top-left to bottom-right)
            var x = -size.height
            while (x < size.width) {
                drawLine(
                    color = patternColor,
                    start = Offset(x, 0f),
                    end = Offset(x + size.height, size.height),
                    strokeWidth = lineWidth
                )
                x += step
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
fun BlackjackCardsPreview() {
    BlackJackTheme {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            //Purchasable packs
//            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
//                BlackjackCard(PlayCard(Suit.SPADES, Rank.ACE), packId = 1)
//                DownBlackJackCard(packId = 1)
//            }
//            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
//                BlackjackCard(PlayCard(Suit.HEARTS, Rank.TEN), packId = 2)
//                DownBlackJackCard(packId = 2)
//            }
//            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
//                BlackjackCard(PlayCard(Suit.HEARTS, Rank.TEN), packId = 3)
//                DownBlackJackCard(packId = 3)
//            }
//            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
//                BlackjackCard(PlayCard(Suit.HEARTS, Rank.TEN), packId = 4)
//                DownBlackJackCard(packId = 4)
//            }
//            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
//                BlackjackCard(PlayCard(Suit.HEARTS, Rank.TEN), packId = 5)
//                DownBlackJackCard(packId = 5)
//            }


            // Non-purchasable packs
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                BlackjackCard(PlayCard(Suit.HEARTS, Rank.TEN), packId = 101)
                DownBlackJackCard(packId = 101)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                BlackjackCard(PlayCard(Suit.HEARTS, Rank.TEN), packId = 102)
                DownBlackJackCard(packId = 102)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                BlackjackCard(PlayCard(Suit.HEARTS, Rank.TEN), packId = 103)
                DownBlackJackCard(packId = 103)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                BlackjackCard(PlayCard(Suit.HEARTS, Rank.TEN), packId = 104)
                DownBlackJackCard(packId = 104)
            }
        }
    }
}