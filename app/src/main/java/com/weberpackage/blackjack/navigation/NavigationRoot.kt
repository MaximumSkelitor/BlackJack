package com.weberpackage.blackjack.navigation

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.weberpackage.blackjack.ActionItem
import com.weberpackage.blackjack.MainViewModel
import com.weberpackage.blackjack.MainViewModelFactory
import com.weberpackage.blackjack.coredata.PreferenceManager
import com.weberpackage.blackjack.screens.dashboard.DashboardScreen
import com.weberpackage.blackjack.screens.dashboard.FirstTimeLoginScreen
import com.weberpackage.blackjack.screens.gameplay.multiplayer.MultiplayerScreen
import com.weberpackage.blackjack.screens.gameplay.multiplayer.MultiplayerViewModel
import com.weberpackage.blackjack.screens.gameplay.play_now.PlayNowScreen
import com.weberpackage.blackjack.screens.gameplay.play_now.PlayNowViewModel
import com.weberpackage.blackjack.screens.gameplay.play_now.PlayNowViewModelFactory
import com.weberpackage.blackjack.screens.gameplay.practice.PracticeScreen
import com.weberpackage.blackjack.screens.gameplay.practice.PracticeViewModel
import com.weberpackage.blackjack.screens.gameplay.practice.PracticeViewModelFactory
import com.weberpackage.blackjack.screens.profile.ProfileScreen
import com.weberpackage.blackjack.screens.settings.SettingsScreen
import com.weberpackage.blackjack.screens.settings.screens.PreferencesScreen
import com.weberpackage.blackjack.screens.settings.screens.UsernameScreen
import com.weberpackage.blackjack.screens.shop.ShopScreen
import com.weberpackage.blackjack.screens.structure.AppTopBar
import com.weberpackage.blackjack.screens.structure.gradientBackground
import com.weberpackage.blackjack.ui.theme.BlackJackTheme
import com.weberpackage.blackjack.ui.theme.spacing
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NavigationRoot3(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel =
        viewModel(factory = MainViewModelFactory(PreferenceManager(LocalContext.current))),
) {
    val context = LocalContext.current
    val preferenceManager = remember { PreferenceManager(context) }
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
    
    val startDestination = if (preferenceManager.hasSetUsername()) {
        NavigationItem.DashboardScreen.name
    } else {
        NavigationItem.FirstTimeLogin.name
    }

    val hazeState = rememberHazeState()

    val totalChips by mainViewModel.credits

    Scaffold(
        topBar = {
            if (navigationItem?.showTopBar == true) {
                AppTopBar(
                    hazeState = hazeState,
                    title = stringResource(navigationItem.titleResId),
                    totalChips = if (navigationItem.showCredits) totalChips else null,
                    showOnBack = navigationItem.showBack,
                    onBack = { navController.navigateUp() },
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
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
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
                .background(
                    gradientBackground()
                )
        ) {
            NavHost(
                modifier = Modifier
                    .background(Color.Transparent),
//                    .padding(paddingValues),
                navController = navController,
                startDestination = startDestination,
            ) {
                composable(NavigationItem.FirstTimeLogin.name) {
                    FirstTimeLoginScreen(
                        onBack = {
                            navController.navigate(NavigationItem.DashboardScreen.name) {
                                popUpTo(NavigationItem.FirstTimeLogin.name) { inclusive = true }
                            }
                        },
                        preferenceManager = preferenceManager,
                    )
                }
                composable(NavigationItem.DashboardScreen.name) {
                    DashboardScreen(
                        contentPadding = paddingValues,
                        onNavigateToPlayNow = {
                            navController.navigate(NavigationItem.PlayNowScreen.name)
                        },

                        onNavigateToPractice = {
                            navController.navigate(NavigationItem.PracticeScreen.name)
                        },
                        onNavigateToMultiplayer = {
                            navController.navigate(NavigationItem.MultiplayerScreen.name)
                        },
                        userUsername = preferenceManager.getUsername(),
                        totalChips = totalChips
                    )
                }
                composable(NavigationItem.PracticeScreen.name) {
                    val practiceViewModel: PracticeViewModel = viewModel(
                        factory = PracticeViewModelFactory(preferenceManager)
                    )
                    PracticeScreen(
                        onBack = { navController.navigateUp() },
                        viewModel = practiceViewModel,
                        preferenceManager = preferenceManager,
                        contentPadding = paddingValues
                    )
                }
                composable(NavigationItem.PlayNowScreen.name) {
                    val playNowViewModel: PlayNowViewModel = viewModel(
                        factory = PlayNowViewModelFactory(preferenceManager)
                    )
                    PlayNowScreen(
                        onBack = { navController.navigateUp() },
                        viewModel = playNowViewModel,
                        preferenceManager = preferenceManager,
                        onUpdateChips = { mainViewModel.refreshCredits() },
                        contentPadding = paddingValues
                    )
                }
                composable(NavigationItem.ProfileScreen.name) {
                    ProfileScreen(
                        totalChips = totalChips,
                        highestChips = preferenceManager.getHighestCredits(),
                        userUsername = preferenceManager.getUsername(),
                        ownedPacks = preferenceManager.getOwnedPacks(),
                        equippedPackId = preferenceManager.getEquippedPack(),
                        onEquipPack = { preferenceManager.setEquippedPack(it) },
                        contentPadding = paddingValues
                    )
                }

                composable(NavigationItem.MultiplayerScreen.name) {
                    val multiplayerViewModel: MultiplayerViewModel = viewModel()
                    MultiplayerScreen(
                        onBack = { navController.navigateUp() },
                        viewModel = multiplayerViewModel,
                        preferenceManager = preferenceManager,
                        contentPadding = paddingValues
                    )
                }
                composable(NavigationItem.SettingsScreen.name) {
                    SettingsScreen(
                        onNavigateToPreferences = {
                            navController.navigate(NavigationItem.PreferencesScreen.name)
                        },
                        onNavigateToUsername = {
                            navController.navigate(NavigationItem.UsernameScreen.name)
                        },
                        userUsername = preferenceManager.getUsername(),
                        contentPadding = paddingValues
                        )
                }
                composable(NavigationItem.PreferencesScreen.name) {
                    PreferencesScreen(
                        mainViewModel = mainViewModel,
                        enabled = currentRoute == NavigationItem.PreferencesScreen.name,
                        contentPadding = paddingValues
                    )
                }
                composable(NavigationItem.UsernameScreen.name) {
                    UsernameScreen(
                        onBack = {
                            navController.navigateUp()
                        },
                        preferenceManager = preferenceManager,
                        contentPadding = paddingValues
                    )
                }
                composable(NavigationItem.ShopScreen.name) {
                    ShopScreen(
                        contentPadding = paddingValues,
                        totalChips = totalChips,
                        onUpdateChips = { mainViewModel.addCredits(it) }
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
        NavigationRoot3()
    }
}