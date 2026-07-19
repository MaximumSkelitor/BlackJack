package com.weberpackage.blackjack.settings.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.PhoneAndroid
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.weberpackage.blackjack.R
import com.weberpackage.blackjack.common.presentation.base.cardColorStops
import com.weberpackage.blackjack.common.presentation.theme.AppTheme

@Composable
internal fun AppearanceSection(
    currentTheme: AppTheme,
    onThemeSelected: (AppTheme) -> Unit,
    enabled: Boolean = true
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Brush.horizontalGradient(colorStops = cardColorStops()))
    ) {
        Column(Modifier.selectableGroup()) {
            // Title
            Text(
                text = stringResource(R.string.display_section),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(start = 16.dp, top = 10.dp)
            )
            RadioButtonOption(
                text = stringResource(R.string.system_mode),
                appearanceImage = Icons.Outlined.PhoneAndroid,
                selected = currentTheme == AppTheme.SYSTEM,
                onClick = { onThemeSelected(AppTheme.SYSTEM) },
                enabled = enabled
            )
            RadioButtonOption(
                text = stringResource(R.string.light_mode),
                appearanceImage = Icons.Outlined.WbSunny,
                selected = currentTheme == AppTheme.LIGHT,
                onClick = { onThemeSelected(AppTheme.LIGHT) },
                enabled = enabled
            )
            RadioButtonOption(
                text = stringResource(R.string.dark_mode),
                appearanceImage = Icons.Outlined.DarkMode,
                selected = currentTheme == AppTheme.DARK,
                onClick = { onThemeSelected(AppTheme.DARK) },
                enabled = enabled
            )
        }
    }
}
