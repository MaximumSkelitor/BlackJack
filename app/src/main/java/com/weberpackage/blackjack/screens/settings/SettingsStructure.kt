package com.weberpackage.blackjack.screens.settings

import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.weberpackage.blackjack.R
import com.weberpackage.blackjack.navigation.NavigationItem
import com.weberpackage.blackjack.ui.theme.BlackJackTheme

@Composable
internal fun SimpleSettingsOption(
    @StringRes title: Int,
    onClick: () -> Unit,
    navigationItemIcon: NavigationItem,
) {
    val cornerShape = RoundedCornerShape(12.dp)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .height(56.dp)
            .shadow(
                elevation = 8.dp,
                shape = cornerShape,
                clip = false
            )
            .background(MaterialTheme.colorScheme.inverseOnSurface, cornerShape)
            .clip(cornerShape)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp) // Adjusted inner padding for better alignment
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = navigationItemIcon.selectedIcon,
                contentDescription = "Representer",
                tint = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.width(12.dp)) // Standard clean spacing

            Text(
                text = stringResource(title),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f) // Fills center space and pushes arrow to the edge
            )

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                contentDescription = "Arrow"
            )
        }
    }
}


@Composable
internal fun SettingsOption(
    @StringRes title: Int,
    description: String,
    onClick: () -> Unit,
    navigationItemIcon: NavigationItem
) {
    val cornerShape = RoundedCornerShape(12.dp)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .height(56.dp)
            .shadow(elevation = 8.dp, shape = cornerShape, clip = false)
            .background(MaterialTheme.colorScheme.inverseOnSurface, cornerShape)
            .clip(cornerShape)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp) // Adjusted inner padding
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left Group: Icon / Title
            Icon(
                imageVector = navigationItemIcon.selectedIcon,
                contentDescription = "Representer",
                tint = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.width(12.dp)) // More standard material spacing

            Text(
                text = stringResource(title),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f) // Takes all remaining middle space
            )

            // Right Group: Description / Arrow
            Text(
                text = description,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(end = 8.dp)
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                contentDescription = "Arrow"
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
private fun SettingsStructurePreview() {
    BlackJackTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            SimpleSettingsOption(
                R.string.preferences,
                onClick = {},
                navigationItemIcon = NavigationItem.PreferencesScreen
            )
            SettingsOption(
                R.string.username,
                stringResource(R.string.users_username, "Tester"),
                onClick = {},
                navigationItemIcon = NavigationItem.UsernameScreen
            )
        }
    }
}