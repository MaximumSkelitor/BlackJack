package com.weberpackage.blackjack.settings.presentation.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.weberpackage.blackjack.common.presentation.model.AppLanguage
import com.weberpackage.blackjack.common.presentation.theme.AppTheme
import com.weberpackage.blackjack.common.presentation.theme.BlackJackTheme
import com.weberpackage.blackjack.common.presentation.utils.safeNavigate
import com.weberpackage.blackjack.settings.presentation.components.AppearanceSection
import com.weberpackage.blackjack.settings.presentation.components.GameplaySection
import com.weberpackage.blackjack.settings.presentation.components.LanguageSection
import com.weberpackage.blackjack.settings.presentation.contract.SettingsContract
import kotlinx.coroutines.flow.Flow

@Composable
fun PreferencesScreenDest(
    contentPadding: PaddingValues,
    navController: NavHostController,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    PreferencesScreen(
        contentPadding = contentPadding,
        state = viewModel.viewState.value,
        effectFlow = viewModel.effect,
        onEventSent = { event -> viewModel.setEvent(event) },
        onNavigationRequested = { navigationEffect ->
            when (navigationEffect) {
                is SettingsContract.Effect.Navigation.Back -> navController.popBackStack()
                is SettingsContract.Effect.Navigation.NavRoute -> {
//                    navController.safeNavigate(
//                        route = navigationEffect.route,
//                        popUp = navigationEffect.popUp
//                    )
                }

                is SettingsContract.Effect.Navigation.NavDest -> {
                    navController.safeNavigate(navigationEffect.route)
                }
            }
        }
    )
}


@Suppress("unused")
@Composable
internal fun PreferencesScreen(
    contentPadding: PaddingValues = PaddingValues(),
    enabled: Boolean = true,
    state: SettingsContract.State,
    effectFlow: Flow<SettingsContract.Effect>?,
    onEventSent: (event: SettingsContract.Event) -> Unit,
    onNavigationRequested: (SettingsContract.Effect.Navigation) -> Unit
) {
    Column(
        modifier = Modifier
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
                customCreditsOption = state.creditsSelected,
                onSelectedCredits = {
                    onEventSent(
                        SettingsContract.Event.OnSelectCredits(!state.creditsSelected)
                    )
                }
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
private fun PreferencesScreenPreview() {
    BlackJackTheme {
        PreferencesScreen(
            contentPadding = PaddingValues(),
            state = SettingsContract.State(
                username = "Poop",
                language = AppLanguage.ENGLISH,
                appTheme = AppTheme.SYSTEM,
                creditsSelected = false,
                isInitialLoading = false
            ),
            effectFlow = null,
            onEventSent = {},
            onNavigationRequested = {}
        )
    }
}