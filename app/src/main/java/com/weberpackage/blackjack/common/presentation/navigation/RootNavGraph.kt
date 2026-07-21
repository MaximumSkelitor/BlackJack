package com.weberpackage.blackjack.common.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.weberpackage.blackjack.betting.presentation.screen.BettingScreenDest
import com.weberpackage.blackjack.changelog.presentation.screens.ChangelogScreenDestination
import com.weberpackage.blackjack.common.presentation.utils.sharedViewModel
import com.weberpackage.blackjack.home.presentation.screens.HomeScreenDest
import com.weberpackage.blackjack.multiplayer.presentation.screens.MultiplayerScreenDest
import com.weberpackage.blackjack.play_now.presentation.screens.PlayNowScreenDest
import com.weberpackage.blackjack.practice.presentation.screens.PracticeScreenDest
import com.weberpackage.blackjack.settings.presentation.screens.CreditsScreen
import com.weberpackage.blackjack.settings.presentation.screens.PreferencesScreenDest
import com.weberpackage.blackjack.settings.presentation.screens.SettingsScreenDest
import com.weberpackage.blackjack.settings.presentation.screens.SettingsViewModel
import com.weberpackage.blackjack.settings.presentation.screens.UsernameScreenDest

@Composable
fun RootNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: Any,
) {
    NavHost(
        modifier = Modifier.background(Color.Transparent),
        navController = navController,
        startDestination = startDestination
    ) {
        composable<NavRoutes.Changelog> {
            ChangelogScreenDestination(navController)
        }
        composable<NavRoutes.SettingsDest.Username> {
            val viewModel = it.sharedViewModel<SettingsViewModel>(
                navController = navController
            )
            UsernameScreenDest(
                navController = navController,
                viewModel = viewModel
            )
        }
        homeNavGraph(navController)
        playNavGraph(navController)
        settingsNavGraph(navController)
    }
}

fun NavGraphBuilder.homeNavGraph(navController: NavHostController) {
    navigation<NavRoutes.HomeGraph>(
        startDestination = NavRoutes.HomeDest.DashHome
    ) {
        composable<NavRoutes.HomeDest.DashHome> {
            HomeScreenDest(
                navController = navController
            )
        }
    }
}

fun NavGraphBuilder.playNavGraph(navController: NavHostController) {
    navigation<NavRoutes.PlayGraph>(
        startDestination = NavRoutes.PlayDest.Betting
    ) {
        composable<NavRoutes.PlayDest.Betting> {
            BettingScreenDest(
                navController = navController
            )
        }
        composable<NavRoutes.PlayDest.PlayNow> {
            PlayNowScreenDest(
                navController = navController
            )
        }
        composable<NavRoutes.PlayDest.Practice> {
            PracticeScreenDest(
                navController = navController
            )
        }
        composable<NavRoutes.PlayDest.Multiplayer> {
            MultiplayerScreenDest(
                navController = navController
            )
        }
    }
}

fun NavGraphBuilder.settingsNavGraph(
    navController: NavHostController
) {

    navigation<NavRoutes.SettingsGraph>(
        startDestination = NavRoutes.SettingsDest.SettingsHome
    ) {
        composable<NavRoutes.SettingsDest.SettingsHome> {
            val viewModel = it.sharedViewModel<SettingsViewModel>(
                navController = navController
            )
            SettingsScreenDest(
                navController = navController,
                viewModel = viewModel
            )
        }
        composable<NavRoutes.SettingsDest.Preferences> {
            val viewModel = it.sharedViewModel<SettingsViewModel>(
                navController = navController
            )
            PreferencesScreenDest(
                navController = navController,
                viewModel = viewModel
            )
        }
        composable<NavRoutes.SettingsDest.Credits> {
            CreditsScreen(
                onBack = { navController.popBackStack() }
            )
        }
        composable<NavRoutes.SettingsDest.Changelog> {
            ChangelogScreenDestination(
                navController = navController
            )
        }
    }
}