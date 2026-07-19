package com.weberpackage.blackjack.practice.presentation.screens

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
import com.weberpackage.blackjack.common.presentation.components.calculateHandValue
import com.weberpackage.blackjack.common.presentation.theme.BlackJackTheme
import com.weberpackage.blackjack.common.presentation.utils.UiText
import com.weberpackage.blackjack.common.presentation.utils.asString
import com.weberpackage.blackjack.practice.presentation.contract.PracticeContract
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun PracticeScreenDest(
    contentPadding: PaddingValues,
    navController: NavHostController,
    viewModel: PracticeViewModel = hiltViewModel(),
) {
    PracticeScreen(
        contentPadding = contentPadding,
        state = viewModel.viewState.value,
        effectFlow = viewModel.effect,
        onEventSent = { event -> viewModel.setEvent(event) },
        onNavigationRequested = { navigationEffect ->
            when (navigationEffect) {
                is PracticeContract.Effect.Navigation.Back -> navController.popBackStack()
                is PracticeContract.Effect.Navigation.NavRoute -> {
                }

                is PracticeContract.Effect.Navigation.NavDest -> {
                    navController.navigate(navigationEffect.route)
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PracticeScreen(
    contentPadding: PaddingValues,
    state: PracticeContract.State,
    effectFlow: Flow<PracticeContract.Effect>?,
    onEventSent: (event: PracticeContract.Event) -> Unit,
    onNavigationRequested: (PracticeContract.Effect.Navigation) -> Unit
) {
    val dealerHand = state.dealerHand
    val isGameOver = state.isGameOver

    HandleSideEffects(
        effectFlow = effectFlow,
        onNavigationRequested = onNavigationRequested
    )

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
            if (!isGameOver && dealerHand.size >= 2) dealerHand.drop(1) else dealerHand

        HandDisplay(
            title = stringResource(R.string.dealers_hand),
            currentCards = dealerHand,
            totalLabel = stringResource(R.string.dealer_total),
            totalCardLabel = stringResource(
                R.string.dealer_total_number,
                if (!isGameOver && dealerHand.size >= 2) ">${calculateHandValue(visibleDealerCards)}" else calculateHandValue(
                    dealerHand
                )
            ),
            packId = state.equippedPack,
            hideFirstCard = !isGameOver && dealerHand.size >= 2
        )

        // Status Message
        Text(
            text = state.statusMessage.asString(),
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        // Player's Hand
        HandDisplay(
            title = stringResource(R.string.your_hand),
            currentCards = state.playerHand,
            totalLabel = stringResource(R.string.player_total),
            totalCardLabel = stringResource(
                R.string.player_total_number,
                calculateHandValue(state.playerHand)
            ),
            packId = state.equippedPack
        )

        // Controls
        Row {
            BlackjackButton(
                onClick = {
                    onEventSent(
                        PracticeContract.Event.Hit
                    )
                },
                enabled = !isGameOver,
                text = stringResource(R.string.hit)
            )
            Spacer(modifier = Modifier.width(16.dp))
            BlackjackButton(
                onClick = {
                    onEventSent(
                        PracticeContract.Event.Stand
                    )
                },
                enabled = !isGameOver,
                text = stringResource(R.string.stand)
            )
            if (isGameOver) {
                Spacer(modifier = Modifier.width(16.dp))
                BlackjackButton(
                    onClick = {
                        onEventSent(
                            PracticeContract.Event.ResetGame
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
    effectFlow: Flow<PracticeContract.Effect>?,
    onNavigationRequested: (PracticeContract.Effect.Navigation) -> Unit
) {
    LaunchedEffect(SIDE_EFFECTS_KEY) {
        effectFlow?.onEach { effect ->
            when (effect) {
                is PracticeContract.Effect.Navigation -> {
                    onNavigationRequested(effect)
                }

                is PracticeContract.Effect.Notification -> {
                }
            }
        }?.collect()
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
fun PracticeScreenPreview() {
    BlackJackTheme {
        PracticeScreen(
            contentPadding = PaddingValues(),
            state = PracticeContract.State(
                equippedPack = 1,
                playerHand = emptyList(),
                dealerHand = emptyList(),
                statusMessage = UiText(R.string.welcome_blackjack),
                isGameOver = false
            ),
            effectFlow = null,
            onNavigationRequested = {},
            onEventSent = {}
        )
    }
}
