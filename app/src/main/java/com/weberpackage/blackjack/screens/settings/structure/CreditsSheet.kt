package com.weberpackage.blackjack.screens.settings.structure

import android.content.res.Configuration
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.composeunstyled.ModalBottomSheetState
import com.composeunstyled.Scrim
import com.composeunstyled.Sheet
import com.composeunstyled.SheetDetent
import com.composeunstyled.UnstyledModalBottomSheet
import com.weberpackage.blackjack.R
import com.weberpackage.blackjack.ui.theme.BlackJackTheme

@Composable
private fun CreditItem(name: String, url: String) {
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { uriHandler.openUri(url) }
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = url,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

private val libraries = listOf(
    "BlackJack" to "https://github.com/MaximumSkelitor/BlackJack/blob/master/LICENSE",
    "Jetpack Compose" to "https://developer.android.com/compose",
    "Material 3" to "https://m3.material.io/https://m3.material.io/",
    "Navigation 3" to "https://developer.android.com/guide/navigation/navigation-3",
    "Compose Unstyled" to "https://github.com/composablehorizons/compose-unstyled?tab=readme-ov-file",
    "Haze" to "https://github.com/chrisbanes/haze",
    "MaterialKolor" to "https://github.com/jordond/materialkolor",
    "Kotlinx Serialization" to "https://github.com/kotlin/kotlinx.serialization",
    "Google Fonts" to "https://fonts.google.com/",
    "PiFire-Android" to "https://github.com/weberbox/PiFire-Android"
)

@Composable
fun CreditsSheetContent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            .padding(24.dp)
            .padding(bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(32.dp, 4.dp)
                .background(
                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                    RoundedCornerShape(100)
                )
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.credits_license),
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.height(2.dp))
        Text(
            text = "BlackJack created by Max Weber, © 2026",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(16.dp))

        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            items(libraries) { (name, desc) ->
                CreditItem(name, desc)
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 4.dp),
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
                )
            }
        }
    }
}

@Composable
fun CreditsSheet(
    state: ModalBottomSheetState
) {
    UnstyledModalBottomSheet(
        state = state,
        onDismiss = { state.targetDetent = SheetDetent.Hidden },
        overlay = {
            Scrim(
                modifier = Modifier.background(Color.Black.copy(alpha = 0.2f)),
                enter = fadeIn(),
                exit = fadeOut()
            )
        }
    ) {
        Sheet(
            modifier = Modifier.fillMaxWidth()
        ) {
            CreditsSheetContent()
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
internal fun CreditsSheetPreview() {
    BlackJackTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.BottomCenter
        ) {
            CreditsSheetContent()
        }
    }
}
