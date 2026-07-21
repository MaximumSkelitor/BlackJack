package com.weberpackage.blackjack.common.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.weberpackage.blackjack.common.presentation.model.PlayCard
import com.weberpackage.blackjack.common.presentation.model.Rank


@Composable
fun HandDisplay(
    title: String,
    currentCards: List<PlayCard>,
    totalLabel: String,
    totalCardLabel: String,
    isTurn: Boolean = false,
    titleColor: Color = MaterialTheme.colorScheme.onSurface,
    packId: Int = 1,
    hideFirstCard: Boolean = false
) {
    val scrollState = rememberScrollState()

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = if (isTurn) "★ $title ★" else title,
            color = if (isTurn) Color.Red else titleColor,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(3.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy((-20).dp, Alignment.CenterHorizontally)
        ) {
            currentCards.forEachIndexed { index, card ->
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
                        if (hideFirstCard && index == 0) {
                            DownBlackJackCard(packId = packId)
                        } else {
                            BlackjackCard(card = card, packId = packId)
                        }
                    }
                }
            }
        }
        if (totalLabel.isNotEmpty() || totalCardLabel.isNotEmpty()) {
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