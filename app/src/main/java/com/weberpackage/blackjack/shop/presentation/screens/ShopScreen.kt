package com.weberpackage.blackjack.shop.presentation.screens

import android.content.res.Configuration
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.weberpackage.blackjack.R
import com.weberpackage.blackjack.common.presentation.base.SIDE_EFFECTS_KEY
import com.weberpackage.blackjack.common.presentation.components.InitialLoadingProgress
import com.weberpackage.blackjack.common.presentation.theme.BlackJackTheme
import com.weberpackage.blackjack.common.presentation.utils.safeNavigate
import com.weberpackage.blackjack.common.presentation.utils.showAlerter
import com.weberpackage.blackjack.shop.presentation.components.CardPackSection
import com.weberpackage.blackjack.shop.presentation.components.ShopSelectionRowContainer
import com.weberpackage.blackjack.shop.presentation.contract.ShopContract
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun ShopScreenDest(
    contentPadding: PaddingValues,
    navController: NavHostController,
    hazeState: HazeState,
    viewModel: ShopViewModel = hiltViewModel(),
) {
    ShopScreen(
        contentPadding = contentPadding,
        hazeState = hazeState,
        state = viewModel.viewState.value,
        effectFlow = viewModel.effect,
        onEventSent = { event -> viewModel.setEvent(event) },
        onNavigationRequested = { navigationEffect ->
            when (navigationEffect) {
                is ShopContract.Effect.Navigation.Back -> navController.popBackStack()
                is ShopContract.Effect.Navigation.NavRoute -> {
                    navController.safeNavigate(
                        route = navigationEffect.route,
                        popUpToRoute = navigationEffect.popUpToRoute,
                        inclusive = navigationEffect.inclusive
                    )
                }

                is ShopContract.Effect.Navigation.NavDest -> {
                    navController.navigate(navigationEffect.route)
                }
            }
        }
    )
}

@Composable
private fun ShopScreen(
    contentPadding: PaddingValues,
    hazeState: HazeState,
    state: ShopContract.State,
    effectFlow: Flow<ShopContract.Effect>?,
    onEventSent: (event: ShopContract.Event) -> Unit,
    onNavigationRequested: (ShopContract.Effect.Navigation) -> Unit
) {
    HandleSideEffects(
        effectFlow = effectFlow,
        onNavigationRequested = onNavigationRequested
    )

    AnimatedContent(
        targetState = state.isInitialLoading,
        label = "ShopLoading"
    ) { isInitialLoading ->
        if (isInitialLoading) {
            InitialLoadingProgress()
        } else {
            ShopScreenContent(
                hazeState = hazeState,
                state = state,
                onEventSent = onEventSent,
                contentPadding = contentPadding,
            )
        }
    }
}

@Composable
private fun ShopScreenContent(
    contentPadding: PaddingValues,
    hazeState: HazeState,
    state: ShopContract.State,
    onEventSent: (ShopContract.Event) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .hazeSource(state = hazeState)
            .padding(contentPadding)
            .verticalScroll(rememberScrollState())
            .padding(top = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Daily Credits Section
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.padding(top = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.daily_credits),
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.ExtraBold
                    )
                }

                ShopSelectionRowContainer(
                    state = state,
                    onEventSent = onEventSent
                )
            }

            // Card Packs Section
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = stringResource(R.string.card_packs),
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.ExtraBold
                    )
                }

                CardPackSection(
                    currentChips = state.totalChips,
                    ownedPacks = state.ownedPacks,
                    onPurchase = { pack ->
                        onEventSent(ShopContract.Event.OnPurchasePack(pack.id, pack.price))
                    }
                )
            }

            Spacer(Modifier.height(20.dp))
        }
    }
}

@Composable
private fun HandleSideEffects(
    effectFlow: Flow<ShopContract.Effect>?,
    onNavigationRequested: (ShopContract.Effect.Navigation) -> Unit
) {
    val activity = LocalActivity.current
    LaunchedEffect(SIDE_EFFECTS_KEY) {
        effectFlow?.onEach { effect ->
            when (effect) {
                is ShopContract.Effect.Navigation -> {
                    onNavigationRequested(effect)
                }

                is ShopContract.Effect.Notification -> {
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
private fun ShopScreenPreview() {
    BlackJackTheme {
        ShopScreenContent(
            contentPadding = PaddingValues(),
            hazeState = HazeState(),
            state = ShopContract.State(
                totalChips = 1000,
                ownedPacks = listOf(1 ),
                lastClaimTime = 0L,
                timeRemaining = 0L,
                isInitialLoading = false
            ),
            onEventSent = {}
        )
    }
}
