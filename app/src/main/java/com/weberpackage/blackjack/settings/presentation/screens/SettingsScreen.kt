package com.weberpackage.blackjack.settings.presentation.screens

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Copyright
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.weberpackage.blackjack.BuildConfig
import com.weberpackage.blackjack.R
import com.weberpackage.blackjack.common.presentation.base.SIDE_EFFECTS_KEY
import com.weberpackage.blackjack.common.presentation.components.InitialLoadingProgress
import com.weberpackage.blackjack.common.presentation.model.AppLanguage
import com.weberpackage.blackjack.common.presentation.theme.AppTheme
import com.weberpackage.blackjack.common.presentation.theme.BlackJackTheme
import com.weberpackage.blackjack.navigation.NavigationItem
import com.weberpackage.blackjack.settings.presentation.components.SettingsOption
import com.weberpackage.blackjack.settings.presentation.contract.SettingsContract
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun SettingsScreenDest(
    contentPadding: PaddingValues,
    navController: NavHostController,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    SettingsScreen(
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
                    navController.navigate(navigationEffect.route)
                }
            }
        }
    )
}


@Composable
private fun SettingsScreen(
    contentPadding: PaddingValues,
    state: SettingsContract.State,
    effectFlow: Flow<SettingsContract.Effect>?,
    onEventSent: (event: SettingsContract.Event) -> Unit,
    onNavigationRequested: (SettingsContract.Effect.Navigation) -> Unit
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
                SettingsScreenContent(
                    contentPadding = contentPadding,
                    state = state,
                    effectFlow = effectFlow,
                    onEventSent = onEventSent,
                    onNavigationRequested = onNavigationRequested,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreenContent(
    contentPadding: PaddingValues,
    state: SettingsContract.State,
    effectFlow: Flow<SettingsContract.Effect>?,
    onEventSent: (event: SettingsContract.Event) -> Unit,
    onNavigationRequested: (SettingsContract.Effect.Navigation) -> Unit
) {
    HandleSideEffects(
        effectFlow = effectFlow,
        onNavigationRequested = onNavigationRequested
    )

    val uriHandler = LocalUriHandler.current

    Column(
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(top = 10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.Start
        ) {
            SettingsOption(
                title = R.string.preferences,
                description = "",
                onClick = {
                    onNavigationRequested(
                        SettingsContract.Effect.Navigation.NavDest(
                            NavigationItem.PreferencesScreen.name
                        )
                    )
                },
                icon = Icons.Default.Tune
            )
            SettingsOption(
                title = R.string.username,
                description = state.username,
                onClick = {
                    onNavigationRequested(
                        SettingsContract.Effect.Navigation.NavDest(
                            NavigationItem.UsernameScreen.name
                        )
                    )
                },
                icon = Icons.Default.Badge
            )
            SettingsOption(
                title = R.string.join_discord,
                description = "",
                onClick = { uriHandler.openUri("https://discord.gg/MktkU63CZn") },
                icon = Icons.Default.Forum
            )
            SettingsOption(
                title = R.string.dialog_app_info_title,
                description = "",
                onClick = {
                    onEventSent(SettingsContract.Event.ShowAppInfo)
                },
                icon = Icons.Default.Info
            )
            SettingsOption(
                title = R.string.credits_license,
                description = "",
                onClick = {
                    onNavigationRequested(
                        SettingsContract.Effect.Navigation.NavDest(
                            NavigationItem.CreditsScreen.name
                        )
                    )
                },
                icon = Icons.Filled.Copyright
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "BlackJack v${BuildConfig.VERSION_NAME}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 100.dp) // Prevents the text from hugging the screen edge
            )
        }
    }
}

@Composable
private fun HandleSideEffects(
    effectFlow: Flow<SettingsContract.Effect>?,
    onNavigationRequested: (SettingsContract.Effect.Navigation) -> Unit
) {
//    val activity = LocalActivity.current
    LaunchedEffect(SIDE_EFFECTS_KEY) {
        effectFlow?.onEach { effect ->
            when (effect) {
                is SettingsContract.Effect.Navigation -> {
                    onNavigationRequested(effect)
                }

                is SettingsContract.Effect.Notification -> {
                }
            }
        }?.collect()
    }
}


@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
internal fun SettingsScreenPreview() {
    BlackJackTheme {
        SettingsScreen(
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
