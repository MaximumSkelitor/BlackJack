package com.weberpackage.blackjack.play_now.presentation.screens

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.weberpackage.blackjack.common.presentation.components.StandardScaffold
import com.weberpackage.blackjack.common.presentation.components.calculateHandValue
import com.weberpackage.blackjack.common.presentation.navigation.NavRoutes
import com.weberpackage.blackjack.common.presentation.theme.BlackJackTheme
import com.weberpackage.blackjack.common.presentation.utils.UiText
import com.weberpackage.blackjack.common.presentation.utils.asString
import com.weberpackage.blackjack.common.presentation.utils.safeNavigate
import com.weberpackage.blackjack.common.presentation.utils.showAlerter
import com.weberpackage.blackjack.play_now.presentation.contract.PlayNowContract
import com.weberpackage.blackjack.play_now.presentation.model.PlayNowState
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun PlayNowScreenDest(
    navController: NavHostController,
    viewModel: PlayNowViewModel = hiltViewModel(),
) {
    PlayNowScreen(
        state = viewModel.viewState.value,
        effectFlow = viewModel.effect,
        onEventSent = { event -> viewModel.setEvent(event) },
        onNavigationRequested = { navigationEffect ->
            when (navigationEffect) {
                is PlayNowContract.Effect.Navigation.Back -> navController.popBackStack()
                is PlayNowContract.Effect.Navigation.NavRoute -> {
                    navController.safeNavigate(
                        route = navigationEffect.route,
                        popUpToRoute = navigationEffect.popUpToRoute,
                        inclusive = navigationEffect.inclusive
                    )
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlayNowScreen(
    state: PlayNowContract.State,
    effectFlow: Flow<PlayNowContract.Effect>?,
    onEventSent: (event: PlayNowContract.Event) -> Unit,
    onNavigationRequested: (PlayNowContract.Effect.Navigation) -> Unit
) {
    val hazeState = rememberHazeState()

    HandleSideEffects(
        effectFlow = effectFlow,
        onNavigationRequested = onNavigationRequested
    )

    BackHandler(enabled = true) {
        handleOnBack(
            isGameOver = state.playNowState.isGameOver,
            onEventSent = onEventSent,
            onNavigationRequested = onNavigationRequested
        )
    }

    StandardScaffold(
        title = stringResource(R.string.play_now_mode),
        hazeState = hazeState,
        totalChips = state.playNowState.totalChips,
        showNavigationIcon = true,
        navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
        showChipIcon = true,
        showText = false,
        onNavigate = {
            handleOnBack(
                isGameOver = state.playNowState.isGameOver,
                onEventSent = onEventSent,
                onNavigationRequested = onNavigationRequested
            )
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .hazeSource(hazeState)
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
                    if (!state.playNowState.isGameOver &&
                        state.playNowState.dealerHand.size >= 2
                    )
                        ">${
                            calculateHandValue(
                                visibleDealerCards
                            )
                        }"
                    else calculateHandValue(state.playNowState.dealerHand)
                ),
                packId = state.playNowState.equippedPack,
                hideFirstCard = !state.playNowState.isGameOver &&
                        state.playNowState.dealerHand.size >= 2
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
}

private fun handleOnBack(
    isGameOver: Boolean,
    onEventSent: (event: PlayNowContract.Event) -> Unit,
    onNavigationRequested: (PlayNowContract.Effect.Navigation) -> Unit
) {
    if (isGameOver) {
        onNavigationRequested(
            PlayNowContract.Effect.Navigation.NavRoute(
                route = NavRoutes.HomeGraph,
                popUpToRoute = NavRoutes.PlayGraph,
                inclusive = true,
            )
        )
    } else {
        onEventSent(
            PlayNowContract.Event.ShowExitDialog
        )
    }
}

@Composable
private fun HandleSideEffects(
    effectFlow: Flow<PlayNowContract.Effect>?,
    onNavigationRequested: (PlayNowContract.Effect.Navigation) -> Unit
) {
    val activity = LocalActivity.current
    LaunchedEffect(SIDE_EFFECTS_KEY) {
        effectFlow?.onEach { effect ->
            when (effect) {
                is PlayNowContract.Effect.Navigation -> {
                    onNavigationRequested(effect)
                }

                is PlayNowContract.Effect.Notification -> {
                    activity?.showAlerter(
                        message = effect.text,
                        isError = effect.error
                    )
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
            onEventSent = {}
        )
    }
}
