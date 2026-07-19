package com.weberpackage.blackjack.multiplayer.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.weberpackage.blackjack.R
import com.weberpackage.blackjack.common.presentation.components.BlackjackButton
import com.weberpackage.blackjack.common.presentation.components.HandDisplay
import com.weberpackage.blackjack.common.presentation.model.PlayCard

@Composable
fun PlayerArea(
    title: String,
    currentCards: List<PlayCard>,
    totalLabel: String,
    totalCardLabel: String,
    isTurn: Boolean,
    onHit: () -> Unit,
    onStand: () -> Unit,
    packId: Int = 1
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HandDisplay(
            title = title,
            currentCards = currentCards,
            totalLabel = totalLabel,
            totalCardLabel = totalCardLabel,
            isTurn = isTurn,
            packId = packId
        )
        Row {
            BlackjackButton(
                onClick = onHit,
                enabled = isTurn,
                text = stringResource(R.string.hit)
            )
            Spacer(modifier = Modifier.width(16.dp))
            BlackjackButton(
                onClick = onStand,
                enabled = isTurn,
                text = stringResource(R.string.stand)
            )
        }
    }
}