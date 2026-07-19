package com.weberpackage.blackjack.screens.structure

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.weberpackage.blackjack.common.presentation.base.hazeAppBarStyle
import com.weberpackage.blackjack.common.presentation.components.ChipCounter
import com.weberpackage.blackjack.common.presentation.model.ActionItem
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    hazeState: HazeState,
    title: String,
    totalChips: Int? = null,
    showOnBack: Boolean = false,
    onBack: (() -> Unit)? = null,
    actions: List<ActionItem>? = emptyList(),
    onAction: (ActionItem) -> Unit
) {
    val hazeStyle = hazeAppBarStyle()

    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            scrolledContainerColor = Color.Transparent,
        ),
        modifier = Modifier
            .hazeEffect(
                state = hazeState,
                style = hazeStyle
            ),
        title = {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold
                )
                totalChips?.let { chips ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(Modifier.width(30.dp))
                        ChipCounter(chips, fontSize = 20, showText = true)
                    }
                }
            }
        },
        navigationIcon = {
            if (showOnBack) {
                IconButton(
                    onClick = { onBack?.invoke() },
                    colors = IconButtonDefaults.iconButtonColors(

                    )
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        },
        actions = {
            actions?.forEach {
                IconButton(
                    onClick = {
                        onAction(it)
                    }
                ) {
                    Icon(
                        imageVector = it.icon,
                        contentDescription = null
                    )
                }
            }
        },

    )
}
