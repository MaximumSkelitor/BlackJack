package com.weberpackage.blackjack.navigation

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.weberpackage.blackjack.coredata.PreferenceManager
import com.weberpackage.blackjack.screens.dashboard.DashboardScreen
import com.weberpackage.blackjack.screens.multiplayer.MultiplayerScreen
import com.weberpackage.blackjack.screens.multiplayer.MultiplayerViewModel
import com.weberpackage.blackjack.screens.practice.PracticeScreen
import com.weberpackage.blackjack.screens.practice.PracticeViewModel
import com.weberpackage.blackjack.screens.practice.PracticeViewModelFactory
import com.weberpackage.blackjack.screens.profile.ProfileScreen
import com.weberpackage.blackjack.screens.settings.SettingsScreen
import com.weberpackage.blackjack.screens.settings.screens.PreferencesScreen
import com.weberpackage.blackjack.screens.settings.screens.UsernameScreen
import com.weberpackage.blackjack.ui.theme.BlackJackTheme

import com.weberpackage.blackjack.MainViewModel
import com.weberpackage.blackjack.MainViewModelFactory

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NavigationRoot3(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel = viewModel(factory = MainViewModelFactory(PreferenceManager(LocalContext.current)))
) {
    val context = LocalContext.current
    val preferenceManager = remember { PreferenceManager(context) }
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Check if bottom bar should be shown
    // Hide bottom bar for specific screens that have their own back navigation
    val showBottomBar = currentRoute != NavigationItem.PracticeScreen.name && 
                       currentRoute != NavigationItem.MultiplayerScreen.name &&
                       currentRoute != NavigationItem.SettingsScreen.name &&
                       currentRoute != NavigationItem.PreferencesScreen.name &&
                       currentRoute != NavigationItem.UsernameScreen.name

    Scaffold(
        modifier = modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)),
                ) {
                    // Only show Home and Profile in the bottom navigation
                    NavigationItem.entries.filter { 
                        it == NavigationItem.DashboardScreen || it == NavigationItem.ProfileScreen 
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
                                indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                                selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }
            }
        }
    ) {  paddingValues ->
        NavHost(
            modifier = Modifier.padding(paddingValues),
            navController = navController,
            startDestination = NavigationItem.DashboardScreen.name,
        ) {
            composable(NavigationItem.DashboardScreen.name) {
                val practiceViewModel: PracticeViewModel = viewModel(
                    factory = PracticeViewModelFactory(preferenceManager)
                )
                DashboardScreen(
                    onNavigateToPractice = {
                        navController.navigate(NavigationItem.PracticeScreen.name)
                    },
                    onNavigateToProfile = {
                        navController.navigate(NavigationItem.ProfileScreen.name) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onNavigateToMultiplayer = {
                        navController.navigate(NavigationItem.MultiplayerScreen.name)
                    },
                    viewModel = practiceViewModel,
                    userUsername = preferenceManager.getUsername()
                )
            }
            composable(NavigationItem.PracticeScreen.name) {
                val practiceViewModel: PracticeViewModel = viewModel(
                    factory = PracticeViewModelFactory(preferenceManager)
                )
                PracticeScreen(
                    onBack = { navController.navigateUp() },
                    viewModel = practiceViewModel
                )
            }
            composable(NavigationItem.ProfileScreen.name) {
                ProfileScreen(
                    totalChips = preferenceManager.getChips(),
                    highestChips = preferenceManager.getHighestCredits(),
                    settingsToggle = {
                        navController.navigate(NavigationItem.SettingsScreen.name)
                    },
                    userUsername = preferenceManager.getUsername()
                )
            }

            composable(NavigationItem.MultiplayerScreen.name) {
                val multiplayerViewModel: MultiplayerViewModel = viewModel()
                MultiplayerScreen(
                    onBack = { navController.navigateUp() },
                    viewModel = multiplayerViewModel
                )
            }
            composable(NavigationItem.SettingsScreen.name) {
                SettingsScreen(
                    onBack = {
                        navController.navigate(NavigationItem.ProfileScreen.name)
                    },
                    mainViewModel = mainViewModel,
                    onNavigateToAppearance = {
                        navController.navigate(NavigationItem.PreferencesScreen.name)
                    },
                    onNavigateToUsername = {
                        navController.navigate(NavigationItem.UsernameScreen.name)
                    },
                    userUsername = preferenceManager.getUsername()
                )
            }
            composable(NavigationItem.PreferencesScreen.name) {
                PreferencesScreen(
                    onBack = {
                        navController.navigateUp()
                    },
                    mainViewModel = mainViewModel,
                    enabled = currentRoute == NavigationItem.PreferencesScreen.name
                )
            }
            composable(NavigationItem.UsernameScreen.name) {
                UsernameScreen(
                    onBack = {
                        navController.navigateUp()
                    },
                    preferenceManager = preferenceManager
                )
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