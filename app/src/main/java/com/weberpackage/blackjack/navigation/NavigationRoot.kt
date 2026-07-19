package com.weberpackage.blackjack.navigation

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.weberpackage.blackjack.betting_screen.presentation.screen.BettingScreenDest
import com.weberpackage.blackjack.common.presentation.base.glowBackground
import com.weberpackage.blackjack.common.presentation.model.ActionItem
import com.weberpackage.blackjack.common.presentation.theme.BlackJackTheme
import com.weberpackage.blackjack.common.presentation.theme.spacing
import com.weberpackage.blackjack.common.presentation.utils.sharedViewModel
import com.weberpackage.blackjack.dashboard.presentation.screens.DashboardScreenDest
import com.weberpackage.blackjack.multiplayer.presentation.screens.MultiplayerScreenDest
import com.weberpackage.blackjack.play_now.presentation.screens.PlayNowScreenDest
import com.weberpackage.blackjack.practice.presentation.screens.PracticeScreenDest
import com.weberpackage.blackjack.profile.presentation.screens.ProfileScreenDest
import com.weberpackage.blackjack.screens.structure.AppTopBar
import com.weberpackage.blackjack.settings.presentation.screens.CreditsScreen
import com.weberpackage.blackjack.settings.presentation.screens.PreferencesScreenDest
import com.weberpackage.blackjack.settings.presentation.screens.SettingsScreenDest
import com.weberpackage.blackjack.settings.presentation.screens.SettingsViewModel
import com.weberpackage.blackjack.settings.presentation.screens.UsernameScreenDest
import com.weberpackage.blackjack.shop.presentation.screens.ShopScreenDest
import com.weberpackage.blackjack.sign_up.presentation.screens.FirstLoginScreenDest
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NavigationRoot3(
    modifier: Modifier = Modifier,
    totalChips: Int,
    hasSetUsername: Boolean,
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val navigationItem = currentRoute.getNavigationItem()

    // Check if bottom bar should be shown
    val showBottomBar = when (currentRoute) {
        NavigationItem.DashboardScreen.name,
        NavigationItem.ProfileScreen.name,
        NavigationItem.ShopScreen.name -> true

        else -> false
    }

    val startDestination = if (hasSetUsername) {
        NavigationItem.DashboardScreen.name
    } else {
        NavigationItem.FirstTimeLogin.name
    }

    val hazeState = rememberHazeState()

    val onBackOverride = remember { mutableStateOf<(() -> Unit)?>(null) }

    Scaffold(
        topBar = {
            if (navigationItem?.showTopBar == true) {
                AppTopBar(
                    hazeState = hazeState,
                    title = stringResource(navigationItem.titleResId),
                    totalChips = if (navigationItem.showCredits) totalChips else null,
                    showOnBack = navigationItem.showBack,
                    onBack = { onBackOverride.value?.invoke() ?: navController.navigateUp() },
                    actions = navigationItem.actions,
                    onAction = {
                        when (it) {
                            ActionItem.SETTINGS -> navController.navigate(NavigationItem.SettingsScreen.name)
                            ActionItem.PROFILE -> navController.navigate(NavigationItem.ProfileScreen.name)
                        }
                    }
                )
            }
        },
        containerColor = Color.Transparent,
        contentColor = contentColorFor(MaterialTheme.colorScheme.background),
        modifier = modifier
            .fillMaxSize(),
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    modifier = Modifier
                        .clip(
                            RoundedCornerShape(
                                topStart = MaterialTheme.spacing.mediumTwo,
                                topEnd = MaterialTheme.spacing.mediumTwo
                            )
                        ),
                    tonalElevation = 0.dp
                ) {
                    // Only show Home/Profile/Shop in the bottom navigation
                    NavigationItem.entries.filter {
                        it == NavigationItem.ProfileScreen || it == NavigationItem.DashboardScreen || it == NavigationItem.ShopScreen
                    }.forEach { item ->
                        val isSelected = currentRoute == item.name
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                navController.navigate(item.name) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                    contentDescription = stringResource(item.titleResId),
                                    modifier = Modifier.size(30.dp)
                                )
                            },
                            label = {
                                Text(
                                    text = stringResource(item.titleResId),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = MaterialTheme.colorScheme.onSurface.copy(.1f),
                                selectedIconColor = MaterialTheme.colorScheme.onSurface,
                                selectedTextColor = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .hazeSource(state = hazeState)
                .background(glowBackground())
        ) {
            NavHost(
                modifier = Modifier
                    .background(Color.Transparent),
//                    .padding(paddingValues),
                navController = navController,
                startDestination = startDestination,
//                enterTransition = { slideEnterTransition() },
//                exitTransition = { slideExitTransition() },
//                popEnterTransition = { slidePopEnterTransition() },
//                popExitTransition = { slidePopExitTransition() }
            ) {
                composable(NavigationItem.FirstTimeLogin.name) {
                    val viewModel = it.sharedViewModel<SettingsViewModel>(
                        navController = navController
                    )
                    FirstLoginScreenDest(
                        contentPadding = paddingValues,
                        navController = navController,
                        viewModel = viewModel
                    )
                }
                composable(NavigationItem.DashboardScreen.name) {
                    DashboardScreenDest(
                        contentPadding = paddingValues,
                        navController = navController
                    )
                }
                composable(NavigationItem.PracticeScreen.name) {
                    PracticeScreenDest(
                        contentPadding = paddingValues,
                        navController = navController
                    )
                }
                composable(NavigationItem.PlayNowScreen.name) {
                    PlayNowScreenDest(
                        contentPadding = paddingValues,
                        navController = navController,
                        onBackOverride = onBackOverride,
                    )
                }
                composable(NavigationItem.BettingScreen.name) {
                    BettingScreenDest(
                        contentPadding = paddingValues,
                        navController = navController
                    )
                }
                composable(NavigationItem.ProfileScreen.name) {
                    ProfileScreenDest(
                        contentPadding = paddingValues,
                        navController = navController
                    )
                }

                composable(NavigationItem.MultiplayerScreen.name) {
                    MultiplayerScreenDest(
                        contentPadding = paddingValues,
                        navController = navController
                    )
                }
                composable(NavigationItem.SettingsScreen.name) {
                    val viewModel = it.sharedViewModel<SettingsViewModel>(
                        navController = navController
                    )
                    SettingsScreenDest(
                        contentPadding = paddingValues,
                        navController = navController,
                        viewModel = viewModel
                    )
                }
                composable(NavigationItem.PreferencesScreen.name) {
                    val viewModel = it.sharedViewModel<SettingsViewModel>(
                        navController = navController
                    )
                    PreferencesScreenDest(
                        contentPadding = paddingValues,
                        navController = navController,
                        viewModel = viewModel
                    )
                }
                composable(NavigationItem.UsernameScreen.name) {
                    val viewModel = it.sharedViewModel<SettingsViewModel>(
                        navController = navController
                    )
                    UsernameScreenDest(
                        contentPadding = paddingValues,
                        navController = navController,
                        viewModel = viewModel
                    )
                }
                composable(NavigationItem.CreditsScreen.name) {
                    CreditsScreen(
                        contentPadding = paddingValues,
                    )
                }
                composable(NavigationItem.ShopScreen.name) {
                    ShopScreenDest(
                        contentPadding = paddingValues,
                        navController = navController
                    )
                }
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
fun NavigationRootPreview() {
    BlackJackTheme {
        NavigationRoot3(
            totalChips = 1000,
            hasSetUsername = false
        )
    }
}