package com.weberpackage.blackjack.dashboard.presentation.model


import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class SelectionItem(
    val id: Int,
    val titleResId: Int,
    val subtitleResId: Int,
    val image: ImageVector,
    val tint: Color
)