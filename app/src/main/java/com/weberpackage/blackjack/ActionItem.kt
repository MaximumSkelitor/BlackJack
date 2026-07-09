package com.weberpackage.blackjack

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

enum class ActionItem(val icon: ImageVector) {
    SETTINGS(Icons.Default.Settings),
    PROFILE(Icons.Default.Person)
}