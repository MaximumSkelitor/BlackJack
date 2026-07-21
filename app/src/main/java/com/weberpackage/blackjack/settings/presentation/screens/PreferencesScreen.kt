package com.weberpackage.blackjack.settings.presentation.screens

import android.content.res.Configuration
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.weberpackage.blackjack.R
import com.weberpackage.blackjack.common.presentation.base.SIDE_EFFECTS_KEY
import com.weberpackage.blackjack.common.presentation.components.StandardScaffold
import com.weberpackage.blackjack.common.presentation.model.AppLanguage
import com.weberpackage.blackjack.common.presentation.theme.AppTheme
import com.weberpackage.blackjack.common.presentation.theme.BlackJackTheme
import com.weberpackage.blackjack.common.presentation.utils.safeNavigate
import com.weberpackage.blackjack.common.presentation.utils.showAlerter
import com.weberpackage.blackjack.settings.presentation.components.AppearanceSection
import com.weberpackage.blackjack.settings.presentation.components.GameplaySection
import com.weberpackage.blackjack.settings.presentation.components.LanguageSection
import com.weberpackage.blackjack.settings.presentation.components.NavigationSection
import com.weberpackage.blackjack.settings.presentation.contract.SettingsContract
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun PreferencesScreenDest(
    navController: NavHostController,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    PreferencesScreen(
        state = viewModel.viewState.value,
        effectFlow = viewModel.effect,
        onEventSent = { event -> viewModel.setEvent(event) },
        onNavigationRequested = { navigationEffect ->
            when (navigationEffect) {
                is SettingsContract.Effect.Navigation.Back -> navController.popBackStack()
                is SettingsContract.Effect.Navigation.NavRoute -> {
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


@Suppress("unused")
@Composable
internal fun PreferencesScreen(
    enabled: Boolean = true,
    state: SettingsContract.State,
    effectFlow: Flow<SettingsContract.Effect>?,
    onEventSent: (event: SettingsContract.Event) -> Unit,
    onNavigationRequested: (SettingsContract.Effect.Navigation) -> Unit
) {
    val hazeState = rememberHazeState()

    HandleSideEffects(
        effectFlow = effectFlow,
        onNavigationRequested = onNavigationRequested
    )

    StandardScaffold(
        title = stringResource(R.string.preferences),
        hazeState = hazeState,
        showNavigationIcon = true,
        navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
        onNavigate = {
            onNavigationRequested(
                SettingsContract.Effect.Navigation.Back
            )
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .hazeSource(hazeState)
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                AppearanceSection(
                    currentTheme = state.appTheme,
                    onThemeSelected = {
                        onEventSent(
                            SettingsContract.Event.OnAppThemeSave(it)
                        )
                    }
                )
                LanguageSection(
                    selectedLanguage = state.language,
                    onLanguageSelected = {
                        onEventSent(
                            SettingsContract.Event.OnSetLanguage(it)
                        )
                    }
                )
                GameplaySection(
                editCustomBetOption = state.creditsSelected,
                onSelectedEditCustomBet = {
                    onEventSent(
                        SettingsContract.Event.OnSelectCredits(!state.creditsSelected)
                    )
                },
                saveCurrentBetOption = state.saveCurrentBetSelected,
                onSaveCurrentBet = {
                    onEventSent(
                        SettingsContract.Event.OnSelectSaveCurrentBet(!state.saveCurrentBetSelected)
                    )
                },
                saveCustomBetOption = state.saveCustomBetSelected,
                onSaveCustomBet = {
                    onEventSent(
                        SettingsContract.Event.OnSelectSaveCustomBet(!state.saveCustomBetSelected)
                    )
                }
            )
                NavigationSection(
                    showBottomBar = state.showBottomBar,
                    onShowBottomBar = {
                        onEventSent(
                            SettingsContract.Event.OnShowBottomBar(!state.showBottomBar)
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun HandleSideEffects(
    effectFlow: Flow<SettingsContract.Effect>?,
    onNavigationRequested: (SettingsContract.Effect.Navigation) -> Unit
) {
    val activity = LocalActivity.current
    LaunchedEffect(SIDE_EFFECTS_KEY) {
        effectFlow?.onEach { effect ->
            when (effect) {
                is SettingsContract.Effect.Navigation -> {
                    onNavigationRequested(effect)
                }

                is SettingsContract.Effect.Notification -> {
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
private fun PreferencesScreenPreview() {
    BlackJackTheme {
        PreferencesScreen(
            state = SettingsContract.State(
                username = "Poop",
                language = AppLanguage.ENGLISH,
                appTheme = AppTheme.SYSTEM,
                creditsSelected = false,
                saveCurrentBetSelected = false,
                saveCustomBetSelected = false,
                showBottomBar = true,
                isInitialLoading = false
            ),
            effectFlow = null,
            onEventSent = {},
            onNavigationRequested = {}
        )
    }
}