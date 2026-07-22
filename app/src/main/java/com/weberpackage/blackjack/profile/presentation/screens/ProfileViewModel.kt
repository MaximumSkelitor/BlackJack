package com.weberpackage.blackjack.profile.presentation.screens

import androidx.lifecycle.viewModelScope
import com.weberpackage.blackjack.common.presentation.base.BaseViewModel
import com.weberpackage.blackjack.core.prefs.Pref
import com.weberpackage.blackjack.core.prefs.Prefs
import com.weberpackage.blackjack.profile.presentation.contract.ProfileContract
import com.weberpackage.blackjack.profile.presentation.model.ProfileState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val prefs: Prefs
) : BaseViewModel<ProfileContract.Event, ProfileContract.State, ProfileContract.Effect>() {

    init {
        collectPrefsFlow()
    }

    private fun collectPrefsFlow() {
        collectAndUpdateState(Pref.username) {
            copy(
                profileState = profileState.copy(username = it)
            )
        }
        viewModelScope.launch {
            prefs.collectPrefsFlow(Pref.totalChips).collect { chips ->
                val currentHighest = prefs.get(Pref.highestChips)
                if (chips > currentHighest) {
                    prefs.set(Pref.highestChips, chips)
                }
                setState {
                    copy(
                        profileState = profileState.copy(totalChips = chips)
                    )
                }
            }
        }
        collectAndUpdateState(Pref.highestChips) {
            copy(
                profileState = profileState.copy(highestChips = it),
            )
        }
        collectAndUpdateState(Pref.ownedPacks) {
            copy(
                profileState = profileState.copy(
                    ownedPacks = it.split(",").filter { s -> s.isNotEmpty() }.map { s -> s.toInt() }
                )
            )
        }
        collectAndUpdateState(Pref.equippedPack) {
            copy(
                profileState = profileState.copy(equippedPack = it)
            )
        }
        collectAndUpdateState(Pref.gamesPlayed) {
            copy(
                profileState = profileState.copy(gamesPlayed = it)
            )
        }
    }

    override fun setInitialState() = ProfileContract.State(
        profileState = ProfileState(
            username = prefs.get(Pref.username),
            totalChips = prefs.get(Pref.totalChips),
            highestChips = prefs.get(Pref.highestChips),
            gamesPlayed = prefs.get(Pref.gamesPlayed),
            ownedPacks = prefs.get(Pref.ownedPacks).split(",").filter { s -> s.isNotEmpty() }.map { s -> s.toInt() },
            equippedPack = prefs.get(Pref.equippedPack),
        ),
        isInitialLoading = false,
    )

    override fun handleEvents(event: ProfileContract.Event) {
        when (event) {
            is ProfileContract.Event.CheckTotalChips -> {}
            is ProfileContract.Event.CheckHighestChips -> {}
            is ProfileContract.Event.OnEquipPack -> prefs.set(Pref.equippedPack, event.packId)
        }
    }

    private fun <T> collectAndUpdateState(
        pref: Pref<T>,
        update: ProfileContract.State.(T) -> ProfileContract.State
    ) {
        viewModelScope.launch {
            prefs.collectPrefsFlow(pref).collect { value ->
                setState { update(value) }
            }
        }
    }
}
