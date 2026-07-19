package com.weberpackage.blackjack.sign_up.presentation.screens

import android.content.res.Configuration
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.weberpackage.blackjack.R
import com.weberpackage.blackjack.common.presentation.base.SIDE_EFFECTS_KEY
import com.weberpackage.blackjack.common.presentation.model.AppLanguage
import com.weberpackage.blackjack.common.presentation.theme.AppTheme
import com.weberpackage.blackjack.navigation.NavigationItem
import com.weberpackage.blackjack.settings.presentation.contract.SettingsContract
import com.weberpackage.blackjack.settings.presentation.screens.SettingsViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun FirstLoginScreenDest(
    contentPadding: PaddingValues,
    navController: NavHostController,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    FirstLoginScreen(
        contentPadding = contentPadding,
        state = viewModel.viewState.value,
        effectFlow = viewModel.effect,
        onEventSent = { event -> viewModel.setEvent(event) },
        onNavigationRequested = { navigationEffect ->
            when (navigationEffect) {
                is SettingsContract.Effect.Navigation.Back -> navController.popBackStack()
                is SettingsContract.Effect.Navigation.NavRoute -> {
                }

                is SettingsContract.Effect.Navigation.NavDest -> {
                    navController.navigate(navigationEffect.route) {
                        popUpTo(NavigationItem.FirstTimeLogin.name) {
                            inclusive = true
                        }
                    }
                }
            }
        }
    )
}


@Composable
fun FirstLoginScreen(
    contentPadding: PaddingValues = PaddingValues(),
    state: SettingsContract.State,
    effectFlow: Flow<SettingsContract.Effect>?,
    onEventSent: (event: SettingsContract.Event) -> Unit,
    onNavigationRequested: (SettingsContract.Effect.Navigation) -> Unit
) {
    HandleSideEffects(
        effectFlow = effectFlow,
        onNavigationRequested = onNavigationRequested
    )

    val maxChar = 15

    val cornerShape = RoundedCornerShape(12.dp)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = stringResource(R.string.username_title),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = stringResource(R.string.username_desc),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            OutlinedTextField(
                value = state.username,
                onValueChange = {
                    if (it.length <= maxChar) onEventSent(
                        SettingsContract.Event.OnUsernameEdit(
                            username = it
                        )
                    )
                },
                label = { Text(stringResource(R.string.username)) },
                placeholder = { Text("Enter your username") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                supportingText = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text("${state.username.length} / $maxChar")
                    }
                }
            )

            Button(
                onClick = {
                    onEventSent(
                        SettingsContract.Event.OnSetUsername
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .shadow(elevation = 8.dp, shape = cornerShape, clip = false),
                shape = cornerShape,
                enabled = state.username.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.inverseOnSurface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(stringResource(R.string.continue_username))
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowForwardIos,
                        contentDescription = null,
                        Modifier.size(15.dp)
                    )
                }
            }
        }
    }
}

@Suppress("unused")
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
                }
            }
        }?.collect()
    }
}


@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
private fun FirstLoginScreenPreview() {
    FirstLoginScreen(
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
