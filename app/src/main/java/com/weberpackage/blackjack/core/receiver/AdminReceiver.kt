package com.weberpackage.blackjack.core.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.weberpackage.blackjack.core.constants.Constants
import com.weberpackage.blackjack.core.prefs.Pref
import com.weberpackage.blackjack.core.prefs.Prefs
import timber.log.Timber
import javax.inject.Inject

class AdminReceiver @Inject constructor(
    private val prefs: Prefs
) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Constants.ACTION_ADD_CREDITS) {
            val amount = intent.getIntExtra("amount", 0)
            
            val currentTotal = prefs.get(Pref.totalChips)
            val newTotal = currentTotal + amount
            prefs.set(Pref.totalChips, newTotal)

            val currentHigh = prefs.get(Pref.highestChips)
            if (newTotal > currentHigh) {
                prefs.set(Pref.highestChips, newTotal)
            }

            Timber.d("Added $amount credits via ADB. New total: $newTotal")
        }
    }
}
