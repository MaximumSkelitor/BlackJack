package com.weberpackage.blackjack.core.prefs

import com.weberpackage.blackjack.common.presentation.theme.AppTheme

data class Pref<T>(
    val key: String,
    val defaultValue: T
) {
    companion object {
        val showBottomBar = Pref("pref_show_bottom_bar", true)
        val storedAppVersion = Pref("pref_stored_app_version", 0)
        val appTheme = Pref("app_theme", AppTheme.SYSTEM.name)
        val totalChips = Pref("total_chips", 1000)
        val highestChips = Pref("highest_credits", 1000)
        val username = Pref("username", "")
        val setLanguage = Pref("app_language", "English")
        val lastClaimTime = Pref("last_claim_time", 0L)
        val ownedPacks = Pref("owned_packs", "1")
        val equippedPack = Pref("equipped_pack", 1)
        val currentBet = Pref("current_bet", 0)
        val customBet = Pref("custom_bet", 100)
        val updatePostponeTime = Pref("pref_update_postpone_time", 0L)
        val creditsOptionSelected = Pref("edit_credits_settings", false)
        val saveCurrentBet = Pref("save_current_bet", false)
        val saveCustomBet = Pref("save_custom_bet", false)
        val gamesPlayed = Pref("games_played", 0L)
    }
}