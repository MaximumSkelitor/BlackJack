package com.weberpackage.blackjack.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Copyright
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.Casino
import androidx.compose.material.icons.outlined.Copyright
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.ui.graphics.vector.ImageVector
import com.weberpackage.blackjack.R
import com.weberpackage.blackjack.common.presentation.model.ActionItem

enum class NavigationItem(
    val titleResId: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val showCredits: Boolean = false,
    val showBack: Boolean = false,
    val actions: List<ActionItem> = emptyList(),
    val showTopBar: Boolean = true,
) {
    // Main Screens
    DashboardScreen(
        R.string.home,
        Icons.Filled.Home,
        Icons.Outlined.Home,
        showCredits = true,
        actions = listOf(ActionItem.SETTINGS)
    ),
    ProfileScreen(
        R.string.profile,
        Icons.Filled.Person,
        Icons.Outlined.Person,
        actions = listOf(ActionItem.SETTINGS)
    ),
    ShopScreen(
        R.string.shop,
        Icons.Filled.ShoppingBag,
        Icons.Outlined.ShoppingBag,
        showCredits = true,
        actions = listOf(ActionItem.SETTINGS)
    ),
    FirstTimeLogin(
        R.string.sign_up,
        Icons.Filled.Badge,
        Icons.Outlined.Badge,
    ),

    // Gameplay Screens
    PlayNowScreen(
        R.string.play_now_mode,
        Icons.Filled.Casino,
        Icons.Outlined.Casino,
        showCredits = true,
        showBack = true
    ),
    BettingScreen(
        R.string.place_bet,
        Icons.Filled.Casino,
        Icons.Outlined.Casino,
        showCredits = true,
        showBack = true
    ),
    PracticeScreen(
        R.string.practice,
        Icons.AutoMirrored.Filled.MenuBook,
        Icons.AutoMirrored.Outlined.MenuBook,
        showBack = true
    ),
    MultiplayerScreen(
        R.string.multiplayer,
        Icons.Filled.Group,
        Icons.Outlined.Group,
        showBack = true
    ),

    // Settings Screens
    SettingsScreen(
        R.string.settings,
        Icons.Filled.Settings,
        Icons.Outlined.Settings,
        showBack = true
    ),
    PreferencesScreen(
        R.string.preferences,
        Icons.Filled.Tune,
        Icons.Outlined.Tune,
        showBack = true
    ),
    UsernameScreen(
        R.string.username,
        Icons.Filled.Badge,
        Icons.Outlined.Badge,
        showBack = true,
    ),
    CreditsScreen(
        R.string.credits_license,
        Icons.Filled.Copyright,
        Icons.Outlined.Copyright,
        showBack = true,
    ),
}
