package com.weberpackage.blackjack.settings.presentation.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.weberpackage.blackjack.R
import com.weberpackage.blackjack.common.presentation.components.StandardScaffold
import com.weberpackage.blackjack.common.presentation.theme.BlackJackTheme
import com.weberpackage.blackjack.settings.presentation.components.CreditItem
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState

private val libraries = listOf(
    "BlackJack" to "https://github.com/MaximumSkelitor/BlackJack/blob/master/LICENSE",
    "PiFire-Android" to "https://github.com/weberbox/PiFire-Android",
    "Jetpack Compose" to "https://developer.android.com/compose",
    "Material 3" to "https://m3.material.io/https://m3.material.io/",
    "Navigation 3" to "https://developer.android.com/guide/navigation/navigation-3",
    "Compose Unstyled" to "https://github.com/composablehorizons/compose-unstyled?tab=readme-ov-file",
    "Haze" to "https://github.com/chrisbanes/haze",
    "MaterialKolor" to "https://github.com/jordond/materialkolor",
    "Kotlinx Serialization" to "https://github.com/kotlin/kotlinx.serialization",
    "Google Fonts" to "https://fonts.google.com/",
    "Offsuit" to "https://www.offsuit.app/"
)

@Composable
fun CreditsScreen(
    onBack: () -> Unit,
) {
    val hazeState = rememberHazeState()

    StandardScaffold(
        title = stringResource(R.string.credits),
        hazeState = hazeState,
        showNavigationIcon = true,
        navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
        onNavigate = onBack
    ) { contentPadding ->
        Column(
            Modifier
                .hazeSource(hazeState)
                .fillMaxSize()
                .padding(contentPadding)
                .padding(bottom = 32.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 13.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "BlackJack created by Max Weber, © 2026",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )

                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outlineVariant,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 10.dp)
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                        .padding(horizontal = 10.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    items(libraries) { (name, desc) ->
                        CreditItem(name, desc)
                    }
                }
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
internal fun CreditsScreenPreview() {
    BlackJackTheme {
        CreditsScreen(
            onBack = {}
        )
    }
}