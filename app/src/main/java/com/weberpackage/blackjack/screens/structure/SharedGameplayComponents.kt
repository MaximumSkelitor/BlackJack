package com.weberpackage.blackjack.screens.structure

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.weberpackage.blackjack.coredata.PlayCard
import com.weberpackage.blackjack.coredata.Rank
import com.weberpackage.blackjack.coredata.Suit

//@Composable
//fun BlackjackCard(
//    card: Card,
//    modifier: Modifier = Modifier
//) {
//    val (suitSymbol, cardColor) = when (card.suit) {
//        Suit.CLUBS -> "♣" to Color.Black
//        Suit.SPADES -> "♠" to Color.Black
//        Suit.HEARTS -> "♥" to Color(0xFFD32F2F)
//        Suit.DIAMONDS -> "♦" to Color(0xFFD32F2F)
//    }
//
//    val displayRank = when (card.rank) {
//        Rank.ACE -> "A"
//        Rank.KING -> "K"
//        Rank.QUEEN -> "Q"
//        Rank.JACK -> "J"
//        else -> card.rank.value.toString()
//    }
//
//    Box(
//        modifier = modifier
//            .size(width = 80.dp, height = 120.dp) // Slightly smaller for better fit
//            .shadow(
//                elevation = 8.dp,
//                shape = RoundedCornerShape(12.dp),
//                clip = false
//            )
//            .background(
//                color = Color.White,
//                shape = RoundedCornerShape(12.dp)
//            ),
//    ) {
//        Column(horizontalAlignment = Alignment.CenterHorizontally) {
//            Text(
//                text = displayRank,
//                fontSize = 36.sp,
//                fontWeight = FontWeight.Bold,
//                color = cardColor
//            )
//            Spacer(modifier = Modifier.height(8.dp))
//            Text(
//                text = suitSymbol,
//                fontSize = 20.sp,
//                color = cardColor.copy(.8f)
//            )
//        }
//    }
//}
@Composable
fun BlackjackCard(
    card: PlayCard
) {
    // Automatically determine suit color and display symbol
    val (suitSymbol, cardColor) = when (card.suit) {
        Suit.CLUBS -> "♣" to Color.Black
        Suit.SPADES -> "♠" to Color.Black
        Suit.HEARTS -> "♥" to Color(0xFFD32F2F)
        Suit.DIAMONDS -> "♦" to Color(0xFFD32F2F)
    }

    // Convert enum Rank to string presentation (e.g., ACE -> "A", TWO -> "2")
    val displayRank = when (card.rank) {
        Rank.ACE -> "A"
        Rank.KING -> "K"
        Rank.QUEEN -> "Q"
        Rank.JACK -> "J"
        else -> card.rank.value.toString()
    }

    Box(
        modifier = Modifier
            .size(width = 100.dp, height = 145.dp)
            .shadow(
                elevation = 15.dp,
                shape = RoundedCornerShape(16.dp),
                clip = false,
                ambientColor = Color.Black,
                spotColor = Color.Black
            )
            .background(
                color = Color.White,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(12.dp) // Card inner padding
    ) {
        // Top Left: Rank text
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Displays the value, saying
            Text(
                text = displayRank,
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = cardColor
            )
            Spacer(modifier = Modifier.height(35.dp))
            // Suit Symbol
            Text(
                text = suitSymbol,
                fontSize = 24.sp,
                color = cardColor.copy(.9f)
            )
        }
    }
}

@Composable
fun BlackjackButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.9f else 1f,
        label = "buttonScale"
    )

    Button(
        onClick = onClick,
        enabled = enabled,
        interactionSource = interactionSource,
        modifier = modifier.graphicsLayer {
            scaleX = scale
            scaleY = scale
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.onSurface,
            contentColor = MaterialTheme.colorScheme.inverseOnSurface
        )
    ) {
        Text(text, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun HandDisplay(
    title: String,
    currentCards: List<PlayCard>,
    totalLabel: String,
    totalCardLabel: String,
    isTurn: Boolean = false,
    titleColor: Color = MaterialTheme.colorScheme.onSurface
) {
    val scrollState = rememberScrollState()

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = if (isTurn) "★ $title ★" else title,
            color = if (isTurn) Color.Red else titleColor,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy((-20).dp, Alignment.CenterHorizontally)
        ) {
            currentCards.forEach { card ->
                key(card) {
                    var isVisible by remember { mutableStateOf(false) }
                    LaunchedEffect(Unit) { isVisible = true }
                    AnimatedVisibility(
                        visible = isVisible,
                        enter = slideInVertically(
                            initialOffsetY = { -it },
                            animationSpec = tween(durationMillis = 400)
                        ) + fadeIn(animationSpec = tween(durationMillis = 400))
                    ) {
                        BlackjackCard(card = card)
                    }
                }
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = totalLabel,
                modifier = Modifier.padding(top = 8.dp),
                color = MaterialTheme.colorScheme.onSurface.copy(0.7F),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = totalCardLabel,
                modifier = Modifier.padding(top = 8.dp),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

fun calculateHandValue(cards: List<PlayCard>): Int {
    var total = cards.sumOf { it.rank.value }
    var acesCount = cards.count { it.rank == Rank.ACE }
    while (total > 21 && acesCount > 0) {
        total -= 10
        acesCount--
    }
    return total
}