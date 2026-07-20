package com.weberpackage.blackjack.home.presentation.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.weberpackage.blackjack.R
import com.weberpackage.blackjack.common.presentation.navigation.NavRoutes
import com.weberpackage.blackjack.home.presentation.model.NavigationItem

@Composable
fun buildNavigationItems(
    shopNotifications: String? = null
): List<NavigationItem> {
    return listOf(
        NavigationItem(
            title = stringResource(R.string.nav_dashboard),
            route = NavRoutes.HomeDest.Dashboard,
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
        ),
        NavigationItem(
            title = stringResource(R.string.nav_profile),
            route = NavRoutes.HomeDest.Profile,
            selectedIcon = Icons.Filled.Person,
            unselectedIcon = Icons.Outlined.Person
        ),
        NavigationItem(
            title = stringResource(R.string.nav_shop),
            route = NavRoutes.HomeDest.Shop,
            selectedIcon = Icons.Filled.ShoppingBag,
            unselectedIcon = Icons.Outlined.ShoppingBag,
            badgeText = shopNotifications
        )
    )
}