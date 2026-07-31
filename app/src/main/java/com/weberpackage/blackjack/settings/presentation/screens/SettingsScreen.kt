package com.weberpackage.blackjack.settings.presentation.screens

import android.content.res.Configuration
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Copyright
import androidx.compose.material.icons.filled.FiberNew
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.weberpackage.blackjack.BuildConfig
import com.weberpackage.blackjack.R
import com.weberpackage.blackjack.common.presentation.base.SIDE_EFFECTS_KEY
import com.weberpackage.blackjack.common.presentation.components.InitialLoadingProgress
import com.weberpackage.blackjack.common.presentation.components.StandardScaffold
import com.weberpackage.blackjack.common.presentation.model.AppLanguage
import com.weberpackage.blackjack.common.presentation.navigation.NavRoutes
import com.weberpackage.blackjack.common.presentation.theme.AppTheme
import com.weberpackage.blackjack.common.presentation.theme.BlackJackTheme
import com.weberpackage.blackjack.common.presentation.utils.safeNavigate
import com.weberpackage.blackjack.common.presentation.utils.showAlerter
import com.weberpackage.blackjack.settings.presentation.components.SettingsOption
import com.weberpackage.blackjack.settings.presentation.contract.SettingsContract
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun SettingsScreenDest(
    navController: NavHostController,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    SettingsScreen(
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


@Composable
private fun SettingsScreen(
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
    state: SettingsContract.State,
    effectFlow: Flow<SettingsContract.Effect>?,
    onEventSent: (event: SettingsContract.Event) -> Unit,
    onNavigationRequested: (SettingsContract.Effect.Navigation) -> Unit
) {
    val hazeState = rememberHazeState()
    val uriHandler = LocalUriHandler.current
    val scrollState = androidx.compose.foundation.lazy.rememberLazyListState()

    HandleSideEffects(
        effectFlow = effectFlow,
        onNavigationRequested = onNavigationRequested
    )

    StandardScaffold(
        title = stringResource(R.string.settings),
        hazeState = hazeState,
        showNavigationIcon = true,
        navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
        onNavigate = {
            onNavigationRequested(
                SettingsContract.Effect.Navigation.Back
            )
        }
    ) { contentPadding ->
        LazyColumn(
            state = scrollState,
            modifier = Modifier
                .fillMaxSize()
                .hazeSource(hazeState)
                .padding(contentPadding),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(modifier = Modifier.height(10.dp))
            }
            item {
                SettingsOption(
                    title = R.string.preferences,
                    description = "",
                    desc = R.string.preferences_settings_option_desc,
                    onClick = {
                        onNavigationRequested(
                            SettingsContract.Effect.Navigation.NavRoute(
                                NavRoutes.SettingsDest.Preferences
                            )
                        )
                    },
                    icon = Icons.Default.Tune
                )
            }
            item {
                SettingsOption(
                    title = R.string.username,
                    description = state.username,
                    onClick = {
                        onNavigationRequested(
                            SettingsContract.Effect.Navigation.NavRoute(
                                NavRoutes.SettingsDest.Username()
                            )
                        )
                    },
                    desc = R.string.username_settings_option_desc,
                    icon = Icons.Default.Badge
                )
            }
            item {
                SettingsOption(
                    title = R.string.join_discord,
                    description = "",
                    onClick = { uriHandler.openUri("https://discord.gg/MktkU63CZn") },
                    desc = R.string.discord_settings_option_desc,
                    icon = Icons.Default.Forum
                )
            }
            item {
                SettingsOption(
                    title = R.string.dialog_app_info_title,
                    description = "",
                    onClick = {
                        onEventSent(SettingsContract.Event.ShowAppInfo)
                    },
                    desc = R.string.app_info_settings_option_desc,
                    icon = Icons.Default.Info
                )
            }
            item {
                SettingsOption(
                    title = R.string.changelog,
                    description = "",
                    onClick = {
                        onNavigationRequested(
                            SettingsContract.Effect.Navigation.NavRoute(
                                NavRoutes.SettingsDest.Changelog
                            )
                        )
                    },
                    desc = R.string.changelog_settings_option_desc,
                    icon = Icons.Filled.FiberNew
                )
            }
            item {
                SettingsOption(
                    title = R.string.credits_license,
                    description = "",
                    onClick = {
                        onNavigationRequested(
                            SettingsContract.Effect.Navigation.NavRoute(
                                NavRoutes.SettingsDest.Credits
                            )
                        )
                    },
                    desc = R.string.credits_settings_option_desc,
                    icon = Icons.Filled.Copyright
                )
            }
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "BlackJack v${BuildConfig.VERSION_NAME}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f),
                    modifier = Modifier
                        .padding(bottom = 32.dp) // Prevents the text from hugging the screen edge
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
internal fun SettingsScreenPreview() {
    BlackJackTheme {
        SettingsScreen(
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
