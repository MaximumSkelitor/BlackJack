package com.weberpackage.blackjack.app

import android.app.Application
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.os.StrictMode.VmPolicy
import com.weberpackage.blackjack.BuildConfig
import com.weberpackage.blackjack.core.log.DebugLogTree
import com.weberpackage.blackjack.core.prefs.Prefs
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {
    @Inject
    lateinit var prefs: Prefs

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(DebugLogTree("BlackJackTag"))
        }

        Timber.d("Startup - Application Start")
    }

    @Suppress("unused")
    private fun strictMode() {
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(
                ThreadPolicy.Builder().detectAll().penaltyLog().build()
            )
            StrictMode.setVmPolicy(VmPolicy.Builder().detectAll().penaltyLog().build())
        }
    }
}