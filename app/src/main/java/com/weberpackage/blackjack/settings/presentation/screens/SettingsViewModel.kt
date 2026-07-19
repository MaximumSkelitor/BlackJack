package com.weberpackage.blackjack.settings.presentation.screens

import android.text.format.DateFormat
import androidx.lifecycle.viewModelScope
import com.weberpackage.blackjack.BuildConfig
import com.weberpackage.blackjack.R
import com.weberpackage.blackjack.common.presentation.base.BaseViewModel
import com.weberpackage.blackjack.common.presentation.model.AppLanguage
import com.weberpackage.blackjack.common.presentation.theme.AppTheme
import com.weberpackage.blackjack.common.presentation.utils.DialogAction
import com.weberpackage.blackjack.common.presentation.utils.DialogController
import com.weberpackage.blackjack.common.presentation.utils.DialogEvent
import com.weberpackage.blackjack.common.presentation.utils.UiText
import com.weberpackage.blackjack.common.presentation.utils.uiTextArgsOf
import com.weberpackage.blackjack.core.prefs.Pref
import com.weberpackage.blackjack.core.prefs.Prefs
import com.weberpackage.blackjack.navigation.NavigationItem
import com.weberpackage.blackjack.settings.presentation.contract.SettingsContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val prefs: Prefs
) : BaseViewModel<SettingsContract.Event, SettingsContract.State, SettingsContract.Effect>() {

    init {
        collectPrefsFlow()
    }

    private fun collectPrefsFlow() {
        collectAndUpdateState(Pref.username) {
            copy(
                username = it,
                isInitialLoading = false
            )
        }
        collectAndUpdateState(Pref.setLanguage) {
            copy(
                language = AppLanguage.from(it),
            )
        }
        collectAndUpdateState(Pref.appTheme) {
            copy(
                appTheme = AppTheme.fromName(it),
            )
        }
        collectAndUpdateState(Pref.creditsOptionSelected) {
            copy(
                creditsSelected = it
            )
        }
    }

    override fun setInitialState() = SettingsContract.State(
        username = "Player",
        language = AppLanguage.ENGLISH,
        appTheme = AppTheme.SYSTEM,
        creditsSelected = false,
        isInitialLoading = true,
    )

    override fun handleEvents(event: SettingsContract.Event) {
        when (event) {
            is SettingsContract.Event.ShowAppInfo -> showAboutDialog()
            is SettingsContract.Event.OnUsernameEdit -> onUsernameEdit(event.username)
            is SettingsContract.Event.OnSaveUsername -> onSaveUsername()
            is SettingsContract.Event.OnSetUsername -> onSetUsername()
            is SettingsContract.Event.OnAppThemeSave -> Pref.appTheme.setPref(event.appTheme.name)
            is SettingsContract.Event.OnSetLanguage -> Pref.setLanguage.setPref(event.language.code)
            is SettingsContract.Event.OnSelectCredits -> prefs.set(Pref.creditsOptionSelected, event.selected)
        }
    }

    private fun Pref<String>.setPref(value: String) {
        prefs.set(this, value)
    }

    private fun onUsernameEdit(username: String) {
        setState {
            copy(
                username = username
            )
        }
    }

    private fun onSaveUsername() {
        val username = viewState.value.username
        if (username.isNotBlank()) {
            prefs.set(Pref.username, username)
            prefs.set(Pref.hasSetUsername, true)
            setEffect {
                SettingsContract.Effect.Navigation.Back
            }
        }
    }

    private fun onSetUsername() {
        val username = viewState.value.username
        if (username.isNotBlank()) {
            prefs.set(Pref.username, username)
            prefs.set(Pref.hasSetUsername, true)
        }
        setEffect{
            SettingsContract.Effect.Navigation.NavDest(
                NavigationItem.DashboardScreen.name
            )
        }
    }

    private fun showAboutDialog() {
        viewModelScope.launch {
            DialogController.sendEvent(
                DialogEvent(
                    title = UiText(
                        R.string.dialog_app_info_title,
                    ),
                    message = UiText(
                        R.string.dialog_app_info_message,
                        uiTextArgsOf(
                            BuildConfig.VERSION_NAME,
                            String.format(Locale.US, BuildConfig.VERSION_CODE.toString()),
                            BuildConfig.BUILD_TYPE,
                            getBuildDate()
                        )
                    ),
                    positiveAction = DialogAction(
                        buttonText = UiText(R.string.close),
                        action = { }
                    )
                )
            )
        }
    }

    private fun getBuildDate(): String {
        return DateFormat.format("MM-dd-yy HH:mm", BuildConfig.BUILD_TIME.toLong()).toString()
    }

    private fun <T> collectAndUpdateState(
        pref: Pref<T>,
        update: SettingsContract.State.(T) -> SettingsContract.State
    ) {
        viewModelScope.launch {
            prefs.collectPrefsFlow(pref).collect { value ->
                setState { update(value) }
            }
        }
    }
}