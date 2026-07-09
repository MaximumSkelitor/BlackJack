package com.weberpackage.blackjack.coredata

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

import com.weberpackage.blackjack.ui.theme.AppTheme

class PreferenceManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("blackjack_prefs", Context.MODE_PRIVATE)

    fun saveTheme(theme: AppTheme) {
        prefs.edit {
            putString("app_theme", theme.name)
        }
    }

    fun getTheme(): AppTheme {
        val themeName = prefs.getString("app_theme", AppTheme.SYSTEM.name)
        return try {
            AppTheme.valueOf(themeName ?: AppTheme.SYSTEM.name)
        } catch (_: Exception) {
            AppTheme.SYSTEM
        }
    }

    fun saveChips(chips: Int) {
        prefs.edit { 
            putInt("total_chips", chips)
            val currentHigh = getHighestCredits()
            if (chips > currentHigh) {
                putInt("highest_credits", chips)
            }
        }
    }

    fun getChips(): Int {
        val chips = prefs.getInt("total_chips", 1000)
        return if (chips <= 0) {
            saveChips(1000)
            1000
        } else {
            chips
        }
    }

    fun getHighestCredits(): Int {
        return prefs.getInt("highest_credits", 1000)
    }

    fun saveUsername(username: String) {
        prefs.edit { putString("username", username) }
        prefs.edit { putBoolean("has_set_username", true) }
    }

    fun getUsername(): String {
        return prefs.getString("username", "Player") ?: "Player"
    }

    fun hasSetUsername(): Boolean {
        return prefs.getBoolean("has_set_username", false)
    }

    fun saveLanguage(language: String) {
        prefs.edit { putString("app_language", language) }
    }

    fun getLanguage(): String {
        return prefs.getString("app_language", "English") ?: "English"
    }

    fun saveLastClaimTime(timeMillis: Long) {
        prefs.edit { putLong("last_claim_time", timeMillis) }
    }

    fun getLastClaimTime(): Long {
        return prefs.getLong("last_claim_time", 0L)
    }

    fun getOwnedPacks(): List<Int> {
        val packsString = prefs.getString("owned_packs", "1") ?: "1" // Default pack 1 is owned
        return packsString.split(",").filter { it.isNotEmpty() }.map { it.toInt() }
    }

    fun addOwnedPack(id: Int) {
        val owned = getOwnedPacks().toMutableSet()
        owned.add(id)
        prefs.edit { putString("owned_packs", owned.joinToString(",")) }
    }

    fun getEquippedPack(): Int {
        return prefs.getInt("equipped_pack", 1) // Default to pack 1
    }

    fun setEquippedPack(id: Int) {
        prefs.edit { putInt("equipped_pack", id) }
    }
}
