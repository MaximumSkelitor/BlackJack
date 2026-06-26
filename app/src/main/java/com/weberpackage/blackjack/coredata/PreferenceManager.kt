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
        } catch (e: Exception) {
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
        prefs.edit().putString("username", username).apply()
    }

    fun getUsername(): String {
        return prefs.getString("username", "Player") ?: "Player"
    }

    fun saveLanguage(language: String) {
        prefs.edit().putString("app_language", language).apply()
    }

    fun getLanguage(): String {
        return prefs.getString("app_language", "English") ?: "English"
    }
}