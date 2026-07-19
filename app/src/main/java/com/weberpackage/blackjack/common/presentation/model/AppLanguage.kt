package com.weberpackage.blackjack.common.presentation.model

import com.weberpackage.blackjack.R

enum class AppLanguage(val title: Int, val code: String) {
    ENGLISH(title = R.string.english, code = "en"),
    SPANISH(title = R.string.spanish, code = "es"),
    FRENCH(title = R.string.french, code = "fr"),
    GERMAN(title = R.string.german, code = "de");

    companion object {
        infix fun from(input: String?): AppLanguage = AppLanguage.entries.firstOrNull {
            it.code.equals(input, ignoreCase = true) ||
                    it.name.equals(input, ignoreCase = true) ||
                    // Legacy names
                    (it == SPANISH && input == "Español") ||
                    (it == FRENCH && input == "Français") ||
                    (it == GERMAN && (input == "Deutsch" || input == "Dutch")) ||
                    (it == ENGLISH && input == "English")
        } ?: ENGLISH
    }
}