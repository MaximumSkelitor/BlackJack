package com.weberpackage.blackjack.settings.presentation.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.weberpackage.blackjack.R
import com.weberpackage.blackjack.common.presentation.base.cardColorStops
import com.weberpackage.blackjack.common.presentation.theme.BlackJackTheme

@Composable
internal fun NavigationSection(
    showBottomBar: Boolean = true,
    onShowBottomBar: () -> Unit,
    enabled: Boolean = true
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Brush.horizontalGradient(colorStops = cardColorStops()))
    ) {
        Column(
            Modifier
                .selectableGroup()
                .padding(bottom = 7.dp)
        ) {
            // Title
            Text(
                text = stringResource(R.string.navigation_section),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(start = 16.dp, top = 10.dp)
            )
            SwitchOption(
                title = stringResource(R.string.show_bottom_bar_title),
                desc = stringResource(R.string.show_bottom_bar_desc),
                selected = showBottomBar,
                onClick = onShowBottomBar,
                enabled = enabled
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
private fun BottomBarSectionPreview() {
    BlackJackTheme {
        NavigationSection(
            enabled = true,
            onShowBottomBar = {}
        )
    }
}