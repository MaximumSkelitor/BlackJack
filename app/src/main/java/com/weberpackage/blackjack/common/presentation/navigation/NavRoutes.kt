package com.weberpackage.blackjack.common.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class NavRoutes {

    @Serializable
    data object Changelog

    @Serializable
    data object HomeGraph

    @Serializable
    sealed class HomeDest {
        @Serializable
        data object DashHome : HomeDest()
        @Serializable
        data object Dashboard : HomeDest()
        @Serializable
        data object Profile : HomeDest()
        @Serializable
        data object Shop : HomeDest()
    }

    @Serializable
    data object PlayGraph

    @Serializable
    sealed class PlayDest {
        @Serializable
        data object Practice : PlayDest()
        @Serializable
        data class PlayNow(val currentBet: Int) : PlayDest()
        @Serializable
        data class Betting(val previousBet: Int = 0) : PlayDest()
        @Serializable
        data object Multiplayer : PlayDest()
    }

    @Serializable
    data object SettingsGraph

    @Serializable
    sealed class SettingsDest {
        @Serializable
        data object SettingsHome : SettingsDest()
        @Serializable
        data object Preferences : SettingsDest()
        @Serializable
        data class Username(val firstTimeSetup: Boolean = false) : SettingsDest()
        @Serializable
        data object Credits : SettingsDest()

        @Serializable
        data object Changelog : SettingsDest()
    }

}
