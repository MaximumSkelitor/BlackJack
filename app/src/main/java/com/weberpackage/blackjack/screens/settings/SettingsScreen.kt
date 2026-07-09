package com.weberpackage.blackjack.screens.settings

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.weberpackage.blackjack.R
import com.weberpackage.blackjack.screens.settings.structure.SettingsOption
import com.weberpackage.blackjack.screens.structure.gradientBackground
import com.weberpackage.blackjack.ui.theme.BlackJackTheme

@Composable
fun SettingsScreen(
    userUsername: String,
    onNavigateToPreferences: () -> Unit,
    onNavigateToUsername: () -> Unit,
    contentPadding: PaddingValues = PaddingValues()
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(gradientBackground())
                .padding(contentPadding)
                .padding(top = 10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.Start
        ) {
            SettingsOption(
                title = R.string.preferences,
                description = "",
                onClick = onNavigateToPreferences,
                icon = Icons.Default.Tune
            )
            SettingsOption(
                title = R.string.username,
                description = "",
                onClick = onNavigateToUsername,
                icon = Icons.Default.Badge
            )
//            SettingsOption(
//                title = R.string.app_info,
//                description = "",
//                onClick = onNavigateToUsername,
//                icon = Icons.Default.Info
//            )d
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(R.string.app_version),
                style = MaterialTheme.typography.bodyMedium, // Keeps the font appropriately small
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f), // Mutes the text color
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 100.dp) // Prevents the text from hugging the screen edge
            )
        }
    }
}


@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
internal fun SettingsScreenPreview() {
    BlackJackTheme {
        SettingsScreen(
            onNavigateToPreferences = {},
            onNavigateToUsername = {},
            userUsername = "Player",
        )
    }
}
