package com.weberpackage.blackjack.home.presentation.screens

import androidx.lifecycle.viewModelScope
import com.weberpackage.blackjack.BuildConfig
import com.weberpackage.blackjack.common.presentation.base.BaseViewModel
import com.weberpackage.blackjack.core.prefs.Pref
import com.weberpackage.blackjack.core.prefs.Prefs
import com.weberpackage.blackjack.shop.utils.DailyCreditsUtils.getTimeRemaining
import com.weberpackage.blackjack.home.presentation.contract.HomeContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val prefs: Prefs
) : BaseViewModel<HomeContract.Event, HomeContract.State, HomeContract.Effect>() {

    init {
        checkIfAppUpdated()
        collectPrefsFlow()
    }

    override fun setInitialState() = HomeContract.State(
        totalChips = 0,
        showBottomBar = true,
        isInitialLoading = true,
        shopNotification = null,
        isDataError = false
    )

    override fun handleEvents(event: HomeContract.Event) {}

    private fun collectPrefsFlow() {
        collectAndUpdateState(Pref.totalChips) { copy(totalChips = it) }
        collectAndUpdateState(Pref.showBottomBar) { copy(showBottomBar = it) }
        collectAndUpdateState(Pref.lastClaimTime) {
            copy(
                shopNotification = if (getTimeRemaining(it) == 0L) "!" else null
            )
        }
    }

    private fun checkIfAppUpdated() {
        val storedAppVersion = prefs.get(Pref.storedAppVersion)
        val currentVersion = BuildConfig.VERSION_CODE
        if (storedAppVersion < currentVersion) {
            prefs.set(Pref.storedAppVersion, currentVersion)
            viewModelScope.launch {
                delay(1200.milliseconds)
                setEffect {
                    HomeContract.Effect.Navigation.Changelog
                }
            }
        }
    }

    private fun <T> collectAndUpdateState(
        pref: Pref<T>,
        update: HomeContract.State.(T) -> HomeContract.State
    ) {
        viewModelScope.launch {
            prefs.collectPrefsFlow(pref).collect { value ->
                setState { update(value) }
            }
        }
    }
}