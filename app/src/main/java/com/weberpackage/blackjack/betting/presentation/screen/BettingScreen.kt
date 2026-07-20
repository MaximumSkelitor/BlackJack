package com.weberpackage.blackjack.betting.presentation.screen

import android.content.res.Configuration
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.composeunstyled.rememberDialogState
import com.weberpackage.blackjack.R
import com.weberpackage.blackjack.betting.presentation.components.CustomBestDialog
import com.weberpackage.blackjack.betting.presentation.contract.BetContract
import com.weberpackage.blackjack.betting.presentation.model.BetState
import com.weberpackage.blackjack.common.presentation.base.SIDE_EFFECTS_KEY
import com.weberpackage.blackjack.common.presentation.base.formatChips
import com.weberpackage.blackjack.common.presentation.components.StandardScaffold
import com.weberpackage.blackjack.common.presentation.components.StaticBetButtons
import com.weberpackage.blackjack.common.presentation.components.buildStaticBetButtons
import com.weberpackage.blackjack.common.presentation.navigation.NavRoutes
import com.weberpackage.blackjack.common.presentation.theme.BlackJackTheme
import com.weberpackage.blackjack.common.presentation.utils.AutoScaleText
import com.weberpackage.blackjack.common.presentation.utils.safeNavigate
import com.weberpackage.blackjack.common.presentation.utils.showAlerter
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun BettingScreenDest(
    navController: NavHostController,
    viewModel: BettingViewModel = hiltViewModel(),
) {
    BettingScreen(
        state = viewModel.viewState.value,
        effectFlow = viewModel.effect,
        onEventSent = { event -> viewModel.setEvent(event) },
        onNavigationRequested = { navigationEffect ->
            when (navigationEffect) {
                is BetContract.Effect.Navigation.Back -> navController.popBackStack()
                is BetContract.Effect.Navigation.NavRoute -> {
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

@Composable
private fun BettingScreen(
    state: BetContract.State,
    effectFlow: Flow<BetContract.Effect>?,
    onEventSent: (event: BetContract.Event) -> Unit,
    onNavigationRequested: (BetContract.Effect.Navigation) -> Unit
) {
    val bettingItems = remember { buildStaticBetButtons() }
    val dialogState = rememberDialogState()
    val hazeState = rememberHazeState()


    HandleSideEffects(
        effectFlow = effectFlow,
        onNavigationRequested = onNavigationRequested
    )

    CustomBestDialog(
        dialogState = dialogState,
        customBet = state.betState.customBet,
        onSelectCustomBet = { amount ->
            onEventSent(
                BetContract.Event.OnSelectCustomBet(amount)
            )
            onEventSent(
                BetContract.Event.OnCustomBetChange(amount)
            )
        }
    )

    StandardScaffold(
        title = stringResource(R.string.play_now_mode),
        hazeState = hazeState,
        totalChips = state.betState.totalChips,
        showNavigationIcon = true,
        navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
        onNavigate = {
            onNavigationRequested(
                BetContract.Effect.Navigation.NavRoute(
                    route = NavRoutes.HomeGraph,
                    popUpToRoute = NavRoutes.PlayGraph,
                    inclusive = true
                )
            )
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .hazeSource(hazeState)
                .fillMaxSize()
                .padding(contentPadding)
                .padding(vertical = 12.dp, horizontal = 16.dp)
                .padding(bottom = 30.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Title
            Text(
                text = stringResource(R.string.place_your_bet),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(30.dp))

            // Current bet card
            OutlinedCard(
                modifier = Modifier
                    .width(160.dp)
                    .height(110.dp),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.outline
                ),
                colors = CardDefaults.outlinedCardColors(
                    containerColor = MaterialTheme.colorScheme.background.copy(.4f)
                ),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(1f)
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(R.string.current_bet),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.tertiary,
                        style = MaterialTheme.typography.titleMedium
                    )
                    // Current bet value
                    AutoScaleText(
                        text = formatChips(state.betState.currentBet),
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.displayLarge
                    )
                }
            }

            Spacer(Modifier.weight(1f))

            StaticBetButtons(
                staticBets = bettingItems,
                onBetClick = { onEventSent(BetContract.Event.OnAdjustBet(it.amount)) }
            )

            // Custom Bet
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(5.dp, Alignment.Top)
            ) {
                Box(
                    modifier = Modifier
                        .combinedClickable(
                            onClick = {
                                if (state.betState.isCustomBetEditEnabled) {
                                    dialogState.visible = true
                                } else {
                                    onEventSent(
                                        BetContract.Event.OnSelectCustomBet(
                                            state.betState.customBet
                                        )
                                    )
                                }
                            },
                            onLongClick = {
                                if (state.betState.isCustomBetEditEnabled) {
                                    onEventSent(
                                        BetContract.Event.OnSelectCustomBet(
                                            state.betState.customBet
                                        )
                                    )
                                } else {
                                    dialogState.visible = true
                                }
                            },
                        )
                        .background(
                            MaterialTheme.colorScheme.onSurface,
                            RoundedCornerShape(24.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        // Title
                        Text(
                            text = stringResource(R.string.custom_bet),
                            color = MaterialTheme.colorScheme.inverseOnSurface,
                            fontWeight = FontWeight.SemiBold
                        )

                        AutoScaleText(
                            text = formatChips(state.betState.customBet),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.inverseOnSurface
                        )
                        Spacer(Modifier.width(1.dp))
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = MaterialTheme.colorScheme.inverseOnSurface
                        )
                    }
                }
                Text(
                    text = stringResource(state.betState.longPressString),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(.4f)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Place Bet
            Button(
                onClick = { onEventSent(BetContract.Event.OnConfirmBet) },
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                Text(
                    text = "Place Bet",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.inverseOnSurface
                )
            }
        }
    }
}

@Composable
private fun HandleSideEffects(
    effectFlow: Flow<BetContract.Effect>?,
    onNavigationRequested: (BetContract.Effect.Navigation) -> Unit
) {
    val activity = LocalActivity.current
    LaunchedEffect(SIDE_EFFECTS_KEY) {
        effectFlow?.onEach { effect ->
            when (effect) {
                is BetContract.Effect.Navigation -> {
                    onNavigationRequested(effect)
                }

                is BetContract.Effect.Notification -> {
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
private fun BettingSectionPreview() {
    BlackJackTheme {
        Surface {
            BettingScreen(
                state = BetContract.State(
                    betState = BetState(
                        totalChips = 1000,
                    ),
                    isInitialLoading = false
                ),
                effectFlow = null,
                onEventSent = {},
                onNavigationRequested = {}
            )
        }
    }
}
