package com.weberpackage.blackjack

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.weberpackage.blackjack.coredata.PreferenceManager
import com.weberpackage.blackjack.ui.theme.AppTheme

class MainViewModel(private val preferenceManager: PreferenceManager) : ViewModel() {
    init {
        // Ensure the saved language is applied on startup
        val savedLanguage = preferenceManager.getLanguage()
        val languageTag = when (savedLanguage) {
            "English" -> "en"
            "Español" -> "es"
            "Français" -> "fr"
            "Deutsch" -> "de"
            else -> "en"
        }
        val currentLocales = AppCompatDelegate.getApplicationLocales()
        if (currentLocales.isEmpty || currentLocales.get(0)?.language != languageTag) {
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(languageTag))
        }
    }

    private val _theme = mutableStateOf(preferenceManager.getTheme())
    val theme: State<AppTheme> = _theme

    fun setTheme(theme: AppTheme) {
        _theme.value = theme
        preferenceManager.saveTheme(theme)
    }

    private val _language = mutableStateOf(preferenceManager.getLanguage())
    val language: State<String> = _language

    fun setLanguage(languageName: String) {
        _language.value = languageName
        preferenceManager.saveLanguage(languageName)

        val languageTag = when (languageName) {
            "English" -> "en"
            "Español" -> "es"
            "Français" -> "fr"
            "Deutsch" -> "de"
            else -> "en"
        }
        val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(languageTag)
        AppCompatDelegate.setApplicationLocales(appLocale)
    }

    private val _credits = mutableIntStateOf(preferenceManager.getChips())
    val credits: State<Int> = _credits

    fun addCredits(amount: Int) {
        val newTotal = _credits.intValue + amount
        _credits.intValue = newTotal
        preferenceManager.saveChips(newTotal)
    }

    fun refreshCredits() {
        _credits.intValue = preferenceManager.getChips()
    }
}

class MainViewModelFactory(private val preferenceManager: PreferenceManager) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(preferenceManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
