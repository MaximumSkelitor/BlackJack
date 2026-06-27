package com.weberpackage.blackjack.screens.practice

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.weberpackage.blackjack.R
import com.weberpackage.blackjack.screens.structure.BlackjackButton
import com.weberpackage.blackjack.screens.structure.GameplayTopBar
import com.weberpackage.blackjack.screens.structure.HandDisplay
import com.weberpackage.blackjack.screens.structure.SimpleTopBar
import com.weberpackage.blackjack.screens.structure.calculateHandValue
import com.weberpackage.blackjack.ui.theme.BlackJackTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PracticeScreen(
    onBack: () -> Unit,
    viewModel: PracticeViewModel
) {
    val playerHand = viewModel.playerHand
    val dealerHand = viewModel.dealerHand
    val statusMessageResId = viewModel.statusMessageResId
    val isGameOver = viewModel.isGameOver

    SimpleTopBar(
        screenTitle = R.string.practice,
        onBack = onBack,
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(MaterialTheme.colorScheme.background),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                // Dealer's Hand
                HandDisplay(
                    title = stringResource(R.string.dealers_hand),
                    currentCards = dealerHand,
                    totalLabel = stringResource(R.string.dealer_total),
                    totalCardLabel = stringResource(
                        R.string.dealer_total_number,
                        calculateHandValue(dealerHand)
                    )
                )

                // Status Message
                Text(
                    text = stringResource(statusMessageResId),
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                // Player's Hand
                HandDisplay(
                    title = stringResource(R.string.your_hand),
                    currentCards = playerHand,
                    totalLabel = stringResource(R.string.player_total),
                    totalCardLabel = stringResource(
                        R.string.player_total_number,
                        calculateHandValue(playerHand)
                    ),
                )

                // Controls
                Row {
                    BlackjackButton(
                        onClick = { viewModel.hit() },
                        enabled = !isGameOver,
                        text = stringResource(R.string.hit)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    BlackjackButton(
                        onClick = { viewModel.stand() },
                        enabled = !isGameOver,
                        text = stringResource(R.string.stand)
                    )
                    if (isGameOver) {
                        Spacer(modifier = Modifier.width(16.dp))
                        BlackjackButton(
                            onClick = { viewModel.resetGame() },
                            text = stringResource(R.string.new_deal)
                        )
                    }
                }
            }
        }
    )
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
fun PracticeScreenPreview() {
    BlackJackTheme {
        // Preview without real ViewModel to avoid crash
    }
}
