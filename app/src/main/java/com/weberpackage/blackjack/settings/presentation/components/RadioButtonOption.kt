package com.weberpackage.blackjack.settings.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
internal fun RadioButtonOption(
    text: String,
    appearanceImage: ImageVector,
    selected: Boolean,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(56.dp)
            .selectable(
                selected = selected, onClick = onClick, enabled = enabled, role = Role.RadioButton
            )
            .padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = null, // null recommended for accessibility with screen readers
            enabled = enabled
        )
        Spacer(Modifier.width(3.dp))
        Icon(
            imageVector = appearanceImage, contentDescription = "Appearance Image"
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(start = 5.dp)
        )
    }
}