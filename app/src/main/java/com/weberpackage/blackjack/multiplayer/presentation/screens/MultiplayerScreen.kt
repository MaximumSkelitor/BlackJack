package com.weberpackage.blackjack.multiplayer.presentation.screens

import android.content.res.Configuration
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.weberpackage.blackjack.R
import com.weberpackage.blackjack.common.presentation.base.SIDE_EFFECTS_KEY
import com.weberpackage.blackjack.common.presentation.components.BlackjackButton
import com.weberpackage.blackjack.common.presentation.components.calculateHandValue
import com.weberpackage.blackjack.common.presentation.theme.BlackJackTheme
import com.weberpackage.blackjack.common.presentation.utils.UiText
import com.weberpackage.blackjack.common.presentation.utils.asString
import com.weberpackage.blackjack.multiplayer.presentation.components.PlayerArea
import com.weberpackage.blackjack.multiplayer.presentation.contract.MultiContract
import com.weberpackage.blackjack.multiplayer.presentation.model.MultiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun MultiplayerScreenDest(
    contentPadding: PaddingValues,
    navController: NavHostController,
    viewModel: MultiplayerViewModel = hiltViewModel(),
) {
    MultiplayerScreen(
        viewModel = viewModel,
        contentPadding = contentPadding,
        onBack = { navController.popBackStack() }
    )
}

@Composable
fun MultiplayerScreen(
    viewModel: MultiplayerViewModel,
    contentPadding: PaddingValues = PaddingValues(),
    onBack: () -> Unit = {}
) {
    MultiplayerScreen(
        contentPadding = contentPadding,
        state = viewModel.viewState.value,
        effectFlow = viewModel.effect,
        onEventSent = { event -> viewModel.setEvent(event) },
        onNavigationRequested = { navigationEffect ->
            when (navigationEffect) {
                is MultiContract.Effect.Navigation.Back -> onBack()
                is MultiContract.Effect.Navigation.NavRoute -> {
                }

                is MultiContract.Effect.Navigation.NavDest -> {
                    // navController.navigate(navigationEffect.route)
                }
            }
        }
    )
}

@Composable
fun MultiplayerScreen(
    contentPadding: PaddingValues = PaddingValues(),
    state: MultiContract.State,
    effectFlow: Flow<MultiContract.Effect>?,
    onEventSent: (event: MultiContract.Event) -> Unit,
    onNavigationRequested: (MultiContract.Effect.Navigation) -> Unit
) {
    HandleSideEffects(
        effectFlow = effectFlow,
        onNavigationRequested = onNavigationRequested
    )

    val multiState = state.multiState

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        // Player 2 Section (Top) - Flipped for head-to-head local play
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(.5f)
                .graphicsLayer { rotationZ = 180f },
            contentAlignment = Alignment.Center
        ) {
            PlayerArea(
                title = stringResource(R.string.player2_hand),
                currentCards = multiState.player2Hand,
                totalLabel = stringResource(R.string.player_total),
                totalCardLabel = stringResource(
                    R.string.player_total_number,
                    calculateHandValue(multiState.player2Hand)
                ),
                isTurn = multiState.currentPlayer == 2 && !multiState.isGameOver,
                onHit = { onEventSent(MultiContract.Event.Hit) },
                onStand = { onEventSent(MultiContract.Event.Stand) },
                packId = multiState.equippedPack
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
                        calculateHandValue(multiState.dealerHand)
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = multiState.statusMessage.asString(),
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            if (multiState.isGameOver) {
                Spacer(modifier = Modifier.height(8.dp))
                BlackjackButton(
                    onClick = { onEventSent(MultiContract.Event.StartNewGame) },
                    text = stringResource(R.string.new_deal)
                )
            }
        }

        // Player 1 Section (Bottom)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(.5f),
            contentAlignment = Alignment.Center
        ) {
            PlayerArea(
                title = stringResource(R.string.player1_hand),
                currentCards = multiState.player1Hand,
                totalLabel = stringResource(R.string.player_total),
                totalCardLabel = stringResource(
                    R.string.player_total_number,
                    calculateHandValue(multiState.player1Hand)
                ),
                isTurn = multiState.currentPlayer == 1 && !multiState.isGameOver,
                onHit = { onEventSent(MultiContract.Event.Hit) },
                onStand = { onEventSent(MultiContract.Event.Stand) },
                packId = multiState.equippedPack
            )
        }
    }
}

@Composable
private fun HandleSideEffects(
    effectFlow: Flow<MultiContract.Effect>?,
    onNavigationRequested: (MultiContract.Effect.Navigation) -> Unit
) {
    LaunchedEffect(SIDE_EFFECTS_KEY) {
        effectFlow?.onEach { effect ->
            when (effect) {
                is MultiContract.Effect.Navigation -> {
                    onNavigationRequested(effect)
                }

                is MultiContract.Effect.Notification -> {
                }
            }
        }?.collect()
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
fun MultiplayerScreenPreview() {
    BlackJackTheme {
        MultiplayerScreen(
            state = MultiContract.State(
                multiState = MultiState(
                    player1Hand = emptyList(),
                    player2Hand = emptyList(),
                    dealerHand = emptyList(),
                    statusMessage = UiText("Welcome"),
                    isGameOver = false,
                    currentPlayer = 1,
                    equippedPack = 1,
                    isPlayer1Done = false,
                    isPlayer2Done = false
                )
            ),
            effectFlow = null,
            onEventSent = {},
            onNavigationRequested = {}
        )
    }
}
