package com.weberpackage.blackjack.dashboard.presentation.screens

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.weberpackage.blackjack.R
import com.weberpackage.blackjack.common.presentation.base.SIDE_EFFECTS_KEY
import com.weberpackage.blackjack.common.presentation.components.ChipCounter
import com.weberpackage.blackjack.common.presentation.components.InitialLoadingProgress
import com.weberpackage.blackjack.common.presentation.theme.BlackJackTheme
import com.weberpackage.blackjack.common.presentation.theme.spacing
import com.weberpackage.blackjack.dashboard.presentation.components.RankSection
import com.weberpackage.blackjack.dashboard.presentation.components.SelectionRowContainer
import com.weberpackage.blackjack.dashboard.presentation.contract.DashContract
import com.weberpackage.blackjack.dashboard.presentation.model.DashState
import com.weberpackage.blackjack.navigation.NavigationItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun DashboardScreenDest(
    contentPadding: PaddingValues,
    navController: NavController,
    viewModel: DashboardViewModel = hiltViewModel(),
) {
    DashboardScreen(
        contentPadding = contentPadding,
        state = viewModel.viewState.value,
        effectFlow = viewModel.effect,
        onEventSent = { event -> viewModel.setEvent(event) },
        onNavigationRequested = { navigationEffect ->
            when (navigationEffect) {
                is DashContract.Effect.Navigation.Back -> navController.popBackStack()
                is DashContract.Effect.Navigation.NavRoute -> {
//                    navController.safeNavigate(
//                        route = navigationEffect.route,
//                        popUp = navigationEffect.popUp
//                    )
                }

                is DashContract.Effect.Navigation.NavDest -> {
                    navController.navigate(navigationEffect.route)
                }
            }
        }
    )
}

@Composable
private fun DashboardScreen(
    contentPadding: PaddingValues,
    state: DashContract.State,
    effectFlow: Flow<DashContract.Effect>?,
    onEventSent: (event: DashContract.Event) -> Unit,
    onNavigationRequested: (DashContract.Effect.Navigation) -> Unit
) {
    HandleSideEffects(
        effectFlow = effectFlow,
        onNavigationRequested = onNavigationRequested
    )

    AnimatedContent(
        targetState = state,
        contentKey = { it.isInitialLoading }
    ) { state ->
        when {
            state.isInitialLoading -> InitialLoadingProgress()

            else -> {
                DashboardScreenContent(
                    contentPadding = contentPadding,
                    state = state,
                    effectFlow = effectFlow,
                    onEventSent = onEventSent,
                    onNavigationRequested = onNavigationRequested
                )
            }
        }
    }
}

@Composable
private fun DashboardScreenContent(
    contentPadding: PaddingValues,
    state: DashContract.State,
    effectFlow: Flow<DashContract.Effect>?,
    onEventSent: (event: DashContract.Event) -> Unit,
    onNavigationRequested: (DashContract.Effect.Navigation) -> Unit
) {
    HandleSideEffects(
        effectFlow = effectFlow,
        onNavigationRequested = onNavigationRequested
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = contentPadding,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Spacer(Modifier.height(MaterialTheme.spacing.smallTwo))
        }
        item {
            Text(
                text = stringResource(R.string.welcome_user, state.uiState.username),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        item {
            ChipCounter(count = state.uiState.totalChips, fontSize = 57)
        }
        item {
            SelectionRowContainer(
                onNavigateToPlayNow = {
                    onEventSent(DashContract.Event.PlayNowNoCredits)
                    if (state.uiState.totalChips > 0) {
                        onNavigationRequested(
                            DashContract.Effect.Navigation.NavDest(
                                NavigationItem.BettingScreen.name
                            )
                        )
                    }
                },
                onNavigateToPractice = {
                    onNavigationRequested(
                        DashContract.Effect.Navigation.NavDest(
                            NavigationItem.PracticeScreen.name
                        )
                    )
                },
                onNavigateToMultiplayer = {
                    onNavigationRequested(
                        DashContract.Effect.Navigation.NavDest(
                            NavigationItem.MultiplayerScreen.name
                        )
                    )
                }
            )
        }
        item {
            Spacer(Modifier.height(MaterialTheme.spacing.medium))
        }
        item {
            RankSection(
                userChips = state.uiState.totalChips,
                modifier = Modifier.padding(horizontal = MaterialTheme.spacing.smallThree)
            )
        }
        item {
            Spacer(Modifier.height(MaterialTheme.spacing.smallTwo))
        }
    }
}

@Composable
private fun HandleSideEffects(
    effectFlow: Flow<DashContract.Effect>?,
    onNavigationRequested: (DashContract.Effect.Navigation) -> Unit
) {
//    val activity = LocalActivity.current
    LaunchedEffect(SIDE_EFFECTS_KEY) {
        effectFlow?.onEach { effect ->
            when (effect) {
                is DashContract.Effect.Navigation -> {
                    onNavigationRequested(effect)
                }

                is DashContract.Effect.Notification -> {
                }
            }
        }?.collect()
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
private fun DashboardScreenPreview() {
    BlackJackTheme {
        DashboardScreen(
            contentPadding = PaddingValues(),
            state = DashContract.State(
                DashState(
                    totalChips = 1000,
                    username = "Tester"
                ),
                isInitialLoading = false
            ),
            effectFlow = null,
            onEventSent = {},
            onNavigationRequested = {}
        )
    }
}