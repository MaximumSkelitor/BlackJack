package com.weberpackage.blackjack.settings.presentation.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun SwitchRow(
    switchTitle: Int,
    switchDesc: Int,
    selected: Boolean,
    onSelected: () -> Unit,
    enabled: Boolean,
) {
    SwitchOption(
        title = stringResource(switchTitle),
        desc = stringResource(switchDesc),
        selected = selected,
        onClick = onSelected,
        enabled = enabled
    )
    Spacer(Modifier.height(5.dp))
}
