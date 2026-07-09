package com.weberpackage.blackjack.screens.gameplay.multiplayer

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.weberpackage.blackjack.R
import com.weberpackage.blackjack.coredata.PlayCard
import com.weberpackage.blackjack.coredata.PreferenceManager
import com.weberpackage.blackjack.screens.structure.BlackjackButton
import com.weberpackage.blackjack.screens.structure.HandDisplay
import com.weberpackage.blackjack.screens.structure.calculateHandValue
import com.weberpackage.blackjack.screens.structure.gradientBackground
import com.weberpackage.blackjack.ui.theme.BlackJackTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MultiplayerScreen(
    onBack: () -> Unit,
    viewModel: MultiplayerViewModel,
    preferenceManager: PreferenceManager,
    contentPadding: PaddingValues = PaddingValues()
) {
    val player1Hand = viewModel.player1Hand
    val player2Hand = viewModel.player2Hand
    val dealerHand = viewModel.dealerHand
    val statusMessageResId = viewModel.statusMessageResId
    val isGameOver = viewModel.isGameOver
    val currentPlayer = viewModel.currentPlayer
    val equippedPackId = androidx.compose.runtime.remember { preferenceManager.getEquippedPack() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBackground())
            .padding(contentPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        // Player 2 Section (Top) - Flipped for head-to-head local play
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .graphicsLayer { rotationZ = 180f },
            contentAlignment = Alignment.Center
        ) {
            PlayerArea(
                title = stringResource(R.string.player2_hand),
                currentCards = player2Hand,
                totalLabel = stringResource(R.string.player_total),
                totalCardLabel = stringResource(
                    R.string.player_total_number,
                    calculateHandValue(player2Hand)
                ),
                isTurn = currentPlayer == 2 && !isGameOver,
                onHit = { viewModel.hit() },
                onStand = { viewModel.stand() },
                packId = equippedPackId
            )
        }

        // Middle Section (Status and Dealer Info)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.dealer_total),
                    color = MaterialTheme.colorScheme.onSurface.copy(0.7F),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = stringResource(
                        R.string.dealer_total_number,
                        calculateHandValue(dealerHand)
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = stringResource(statusMessageResId),
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            if (isGameOver) {
                Spacer(modifier = Modifier.height(8.dp))
                BlackjackButton(
                    onClick = { viewModel.startNewGame() },
                    text = stringResource(R.string.new_deal)
                )
            }
        }

        // Player 1 Section (Bottom)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            PlayerArea(
                title = stringResource(R.string.player1_hand),
                currentCards = player1Hand,
                totalLabel = stringResource(R.string.player_total),
                totalCardLabel = stringResource(
                    R.string.player_total_number,
                    calculateHandValue(player1Hand)
                ),
                isTurn = currentPlayer == 1 && !isGameOver,
                onHit = { viewModel.hit() },
                onStand = { viewModel.stand() },
                packId = equippedPackId
            )
        }
    }
}



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
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        HandDisplay(
            title = title,
            currentCards = currentCards,
            totalLabel = totalLabel,
            totalCardLabel = totalCardLabel,
            isTurn = isTurn,
            packId = packId
        )
        Spacer(modifier = Modifier.height(12.dp))
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

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
fun MultiplayerScreenPreview() {
    BlackJackTheme {
        // Preview without real ViewModel to avoid crash
    }
}
