package com.weberpackage.blackjack.screens.settings

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.weberpackage.blackjack.MainViewModel
import com.weberpackage.blackjack.MainViewModelFactory
import com.weberpackage.blackjack.R
import com.weberpackage.blackjack.coredata.PreferenceManager
import com.weberpackage.blackjack.navigation.NavigationItem
import com.weberpackage.blackjack.screens.structure.SimpleTopBar
import com.weberpackage.blackjack.ui.theme.BlackJackTheme

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    userUsername: String,
    mainViewModel: MainViewModel,
    onNavigateToAppearance: () -> Unit,
    onNavigateToUsername: () -> Unit,
) {
    SimpleTopBar(
        screenTitle = R.string.settings,
        onBack = onBack,
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    SimpleSettingsOption(
                        title = R.string.preferences,
                        onClick = onNavigateToAppearance,
                        navigationItemIcon = NavigationItem.PreferencesScreen
                    )
                    SettingsOption(
                        title = R.string.username,
                        description = stringResource(R.string.users_username, userUsername),
                        onClick = onNavigateToUsername,
                        navigationItemIcon = NavigationItem.UsernameScreen
                    )
                }
            }
        }
    )
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
internal fun SettingsScreenPreview() {
    val context = LocalContext.current
    val viewModel: MainViewModel = viewModel(factory = MainViewModelFactory(PreferenceManager(context)))
    BlackJackTheme {
        SettingsScreen(
            onBack = {},
            mainViewModel = viewModel,
            onNavigateToAppearance = {},
            onNavigateToUsername = {},
            userUsername = "slimeypicklenut"
        )
    }
}
