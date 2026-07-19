package com.weberpackage.blackjack.shop.presentation.screens

import androidx.lifecycle.viewModelScope
import com.weberpackage.blackjack.common.presentation.base.BaseViewModel
import com.weberpackage.blackjack.core.prefs.Pref
import com.weberpackage.blackjack.core.prefs.Prefs
import com.weberpackage.blackjack.coredata.DailyCreditsUtils
import com.weberpackage.blackjack.shop.presentation.contract.ShopContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds


@HiltViewModel
class ShopViewModel @Inject constructor(
    private val prefs: Prefs
) : BaseViewModel<ShopContract.Event, ShopContract.State, ShopContract.Effect>() {

    init {
        collectPrefsFlow()
        startTimeRemainingUpdate()
    }

    private fun collectPrefsFlow() {
        collectAndUpdateState(Pref.totalChips) {
            copy(
                totalChips = it
            )
        }
        collectAndUpdateState(Pref.ownedPacks) {
            copy(
                ownedPacks = it.split(",").filter { s -> s.isNotEmpty() }.map { s -> s.toInt() },
                isInitialLoading = false
            )
        }
        collectAndUpdateState(Pref.lastClaimTime) {
            copy(
                lastClaimTime = it,
                timeRemaining = DailyCreditsUtils.getTimeRemaining(it)
            )
        }
    }

    private fun startTimeRemainingUpdate() {
        viewModelScope.launch {
            while (true) {
                setState {
                    copy(timeRemaining = DailyCreditsUtils.getTimeRemaining(lastClaimTime))
                }
                delay(1.seconds)
            }
        }
    }

    override fun setInitialState() = ShopContract.State(
        totalChips = prefs.get(Pref.totalChips),
        ownedPacks = prefs.get(Pref.ownedPacks).split(",").filter { s -> s.isNotEmpty() }.map { s -> s.toInt() },
        lastClaimTime = prefs.get(Pref.lastClaimTime),
        timeRemaining = DailyCreditsUtils.getTimeRemaining(prefs.get(Pref.lastClaimTime)),
        isInitialLoading = true,
    )

    override fun handleEvents(event: ShopContract.Event) {
        when (event) {
            is ShopContract.Event.OnPurchasePack -> onPurchasePack(event)
            is ShopContract.Event.OnUpdateCredits -> onUpdateCredits(event.amount)
            is ShopContract.Event.OnClaimDailyCredits -> onClaimDailyCredits(event.credits)
        }
    }

    private fun onClaimDailyCredits(credits: Int) {
        val currentTime = System.currentTimeMillis()
        if (DailyCreditsUtils.getTimeRemaining(viewState.value.lastClaimTime) == 0L) {
            prefs.set(Pref.lastClaimTime, currentTime)
            onUpdateCredits(credits)
        }
    }

    private fun onUpdateCredits(amount: Int) {
        val currentChips = prefs.get(Pref.totalChips)
        prefs.set(Pref.totalChips, currentChips + amount)
    }

    private fun onPurchasePack(event: ShopContract.Event.OnPurchasePack) {
        val currentChips = prefs.get(Pref.totalChips)
        if (currentChips >= event.price) {
            // Update Chips
            prefs.set(Pref.totalChips, currentChips - event.price)

            // Update Owned Packs
            val currentOwned = prefs.get(Pref.ownedPacks)
            val updatedOwned = if (currentOwned.isEmpty()) {
                event.packId.toString()
            } else {
                val ownedList = currentOwned.split(",").toMutableSet()
                ownedList.add(event.packId.toString())
                ownedList.joinToString(",")
            }

            // Set the owned pack preference after its updated
            prefs.set(Pref.ownedPacks, updatedOwned)
        }
    }


    private fun <T> collectAndUpdateState(
        pref: Pref<T>,
        update: ShopContract.State.(T) -> ShopContract.State
    ) {
        viewModelScope.launch {
            prefs.collectPrefsFlow(pref).collect { value ->
                setState { update(value) }
            }
        }
    }
}
