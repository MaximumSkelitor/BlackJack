package com.weberpackage.blackjack.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.Casino
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.ui.graphics.vector.ImageVector
import com.weberpackage.blackjack.R

enum class NavigationItem(
    val titleResId: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    // Screens
    DashboardScreen(
        R.string.home,
        Icons.Filled.Home,
        Icons.Outlined.Home
    ),
    ProfileScreen(
        R.string.profile,
        Icons.Filled.Person,
        Icons.Outlined.Person
    ),
    SettingsScreen(
        R.string.settings,
        Icons.Filled.Settings,
        Icons.Outlined.Settings
    ),

    // Gameplay Screens
    PlayNowScreen(
        R.string.play_now_mode,
        Icons.Filled.Casino,
        Icons.Outlined.Casino
    ),
    PracticeScreen(
        R.string.practice,
        Icons.AutoMirrored.Filled.MenuBook,
        Icons.AutoMirrored.Outlined.MenuBook
    ),
    MultiplayerScreen(
        R.string.multiplayer,
        Icons.Filled.Group,
        Icons.Outlined.Group
    ),

    // Settings Screens
    PreferencesScreen(
        R.string.preferences,
        Icons.Filled.Tune,
        Icons.Outlined.Tune
    ),
    UsernameScreen(
        R.string.username,
        Icons.Filled.Badge,
        Icons.Outlined.Badge
    ),
    FirstTimeLogin(
        R.string.sign_up,
        Icons.Filled.Badge,
        Icons.Outlined.Badge
    )
}
