package com.weberpackage.blackjack.play_now.presentation.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.weberpackage.blackjack.common.presentation.components.HandDisplay
import com.weberpackage.blackjack.common.presentation.components.calculateHandValue
import com.weberpackage.blackjack.common.presentation.theme.BlackJackTheme
import com.weberpackage.blackjack.common.presentation.utils.UiText
import com.weberpackage.blackjack.common.presentation.utils.asString
import com.weberpackage.blackjack.navigation.NavigationItem
import com.weberpackage.blackjack.play_now.presentation.contract.PlayNowContract
import com.weberpackage.blackjack.play_now.presentation.model.PlayNowState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun PlayNowScreenDest(
    contentPadding: PaddingValues,
    navController: NavHostController,
    onBackOverride: MutableState<(() -> Unit)?>,
    viewModel: PlayNowViewModel = hiltViewModel(),
) {
    PlayNowScreen(
        contentPadding = contentPadding,
        state = viewModel.viewState.value,
        effectFlow = viewModel.effect,
        onEventSent = { event -> viewModel.setEvent(event) },
        onNavigationRequested = { navigationEffect ->
            when (navigationEffect) {
                is PlayNowContract.Effect.Navigation.Back -> navController.popBackStack()
                is PlayNowContract.Effect.Navigation.NavRoute -> {}

                is PlayNowContract.Effect.Navigation.NavDest -> {
                    navController.navigate(navigationEffect.route) {
                        popUpTo(NavigationItem.PlayNowScreen.name) {
                            inclusive = true
                        }
                    }
                }
            }
        },
        onBackOverride = onBackOverride,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlayNowScreen(
    contentPadding: PaddingValues = PaddingValues(),
    state: PlayNowContract.State,
    effectFlow: Flow<PlayNowContract.Effect>?,
    onEventSent: (event: PlayNowContract.Event) -> Unit,
    onNavigationRequested: (PlayNowContract.Effect.Navigation) -> Unit,
    onBackOverride: MutableState<(() -> Unit)?>,
) {
    HandleSideEffects(
        effectFlow = effectFlow,
        onNavigationRequested = onNavigationRequested
    )

    // Intercept back button if game is running
    LaunchedEffect(state.playNowState.isGameOver) {
        if (!state.playNowState.isGameOver) {
            onBackOverride.value = {
                onEventSent(
                    PlayNowContract.Event.ShowExitDialog
                )
            }
        } else {
            onBackOverride.value = null
        }
    }

    // Cleanup on dispose
    DisposableEffect(Unit) {
        onDispose {
            onBackOverride.value = null
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
            .padding(bottom = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        // Dealer's Hand
        val visibleDealerCards =
            if (!state.playNowState.isGameOver && state.playNowState.dealerHand.size >= 2) {
                state.playNowState.dealerHand.drop(1)
            } else state.playNowState.dealerHand

        HandDisplay(
            title = stringResource(R.string.dealers_hand),
            currentCards = state.playNowState.dealerHand,
            totalLabel = stringResource(R.string.dealer_total),
            totalCardLabel = stringResource(
                R.string.dealer_total_number,
                if (!state.playNowState.isGameOver && state.playNowState.dealerHand.size >= 2)
                    ">${
                        calculateHandValue(
                            visibleDealerCards
                        )
                    }"
                else calculateHandValue(state.playNowState.dealerHand)
            ),
            packId = state.playNowState.equippedPack,
            hideFirstCard = !state.playNowState.isGameOver && state.playNowState.dealerHand.size >= 2
        )

        // Status Message
        Text(
            text = state.playNowState.statusMessage.asString(),
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        // Player's Hand
        HandDisplay(
            title = stringResource(R.string.your_hand),
            currentCards = state.playNowState.playerHand,
            totalLabel = stringResource(R.string.player_total),
            totalCardLabel = stringResource(
                R.string.player_total_number,
                calculateHandValue(state.playNowState.playerHand)
            ),
            packId = state.playNowState.equippedPack
        )

        // Controls
        Row {
            BlackjackButton(
                onClick = {
                    onEventSent(
                        PlayNowContract.Event.Hit
                    )
                },
                enabled = !state.playNowState.isGameOver,
                text = stringResource(R.string.hit)
            )
            Spacer(modifier = Modifier.width(16.dp))
            BlackjackButton(
                onClick = {
                    onEventSent(
                        PlayNowContract.Event.Stand
                    )
                },
                enabled = !state.playNowState.isGameOver,
                text = stringResource(R.string.stand)
            )
            if (state.playNowState.isGameOver) {
                Spacer(modifier = Modifier.width(16.dp))
                BlackjackButton(
                    onClick = {
                        onEventSent(
                            PlayNowContract.Event.ResetGame
                        )
                    },
                    text = stringResource(R.string.new_deal)
                )
            }
        }
    }
}


@Composable
private fun HandleSideEffects(
    effectFlow: Flow<PlayNowContract.Effect>?,
    onNavigationRequested: (PlayNowContract.Effect.Navigation) -> Unit
) {
    LaunchedEffect(SIDE_EFFECTS_KEY) {
        effectFlow?.onEach { effect ->
            when (effect) {
                is PlayNowContract.Effect.Navigation -> {
                    onNavigationRequested(effect)
                }

                is PlayNowContract.Effect.Notification -> {
                }
            }
        }?.collect()
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
fun PlayNowPreview() {
    BlackJackTheme {
        PlayNowScreen(
            contentPadding = PaddingValues(),
            state = PlayNowContract.State(
                playNowState = PlayNowState(
                    equippedPack = 1,
                    playerHand = emptyList(),
                    dealerHand = emptyList(),
                    statusMessage = UiText(R.string.welcome_blackjack),
                    totalChips = 1000,
                    isGameOver = true,
                ),
            ),
            effectFlow = null,
            onNavigationRequested = {},
            onEventSent = {},
            onBackOverride = remember { mutableStateOf(null) },
        )
    }
}
