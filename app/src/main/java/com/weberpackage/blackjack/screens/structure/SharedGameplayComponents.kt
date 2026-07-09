package com.weberpackage.blackjack.screens.structure

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.weberpackage.blackjack.R
import com.weberpackage.blackjack.coredata.PlayCard
import com.weberpackage.blackjack.coredata.Rank
import com.weberpackage.blackjack.coredata.Suit
import com.weberpackage.blackjack.ui.theme.BlackJackTheme

@Composable
fun ChipCounter(
    count: Int,
    modifier: Modifier = Modifier,
    fontSize: Int,
    showText: Boolean = false
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        formatChips(count).forEach { char ->
            AnimatedContent(
                targetState = char,
                transitionSpec = {
                    if (targetState > initialState) {
                        slideInVertically { it } + fadeIn() togetherWith slideOutVertically { -it } + fadeOut()
                    } else {
                        slideInVertically { -it } + fadeIn() togetherWith slideOutVertically { it } + fadeOut()
                    }.using(SizeTransform(clip = false))
                },
                label = "DigitAnimation"
            ) { targetDigit ->
                Text(
                    text = targetDigit.toString(),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = fontSize.sp
                )
            }
        }
        if (showText) {
            Text(
                text = " ${stringResource(R.string.credits)}",
                fontWeight = FontWeight.SemiBold,
                fontSize = fontSize.sp
            )
        }
    }
}

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
        else -> Brush.verticalGradient(listOf(Color.White, Color.White))
    }

    val cardBorderColor = when (packId) {
        2 -> Color(0xFF2196F3)
        3 -> Color(0xFF9C27B0)
        4 -> Color(0xFFFFC107)
        5 -> Color(0xFFE91E63)
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
                elevation = if (packId > 1) 12.dp else 8.dp,
                shape = RoundedCornerShape(16.dp),
                clip = false,
                ambientColor = cardBorderColor.copy(alpha = 0.5f),
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
                else Modifier
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
fun BlackjackButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val haptic = LocalHapticFeedback.current

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.9f else 1f,
        label = "buttonScale"
    )

    LaunchedEffect(isPressed) {
        if (isPressed) {
            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        }
    }

    Button(
        onClick = {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            onClick()
        },
        enabled = enabled,
        interactionSource = interactionSource,
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            },
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.onSurface,
            contentColor = MaterialTheme.colorScheme.inverseOnSurface
        )
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun HandDisplay(
    title: String,
    currentCards: List<PlayCard>,
    totalLabel: String,
    totalCardLabel: String,
    isTurn: Boolean = false,
    titleColor: Color = MaterialTheme.colorScheme.onSurface,
    packId: Int = 1
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
                        BlackjackCard(card = card, packId = packId)
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
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                BlackjackCard(PlayCard(Suit.SPADES, Rank.ACE), packId = 1)
                BlackjackCard(PlayCard(Suit.HEARTS, Rank.TEN), packId = 2)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                BlackjackCard(PlayCard(Suit.DIAMONDS, Rank.KING), packId = 3)
                BlackjackCard(PlayCard(Suit.CLUBS, Rank.QUEEN), packId = 4)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                BlackjackCard(PlayCard(Suit.SPADES, Rank.JACK), packId = 5)
            }
        }
    }
}