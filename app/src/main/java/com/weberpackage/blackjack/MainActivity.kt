package com.weberpackage.blackjack

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.core.content.ContextCompat
import com.weberpackage.blackjack.coredata.PreferenceManager
import com.weberpackage.blackjack.navigation.NavigationRoot3
import com.weberpackage.blackjack.ui.theme.BlackJackTheme

class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory(PreferenceManager(this))
    }

    private val adminReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == "com.weberpackage.blackjack.ADD_CREDITS") {
                val amount = intent.getIntExtra("amount", 0)
                viewModel.addCredits(amount)
                Log.d("BlackJackAdmin", "Added $amount credits via ADB")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
        enableEdgeToEdge()

        // Register Admin Receiver
        val filter = IntentFilter("com.weberpackage.blackjack.ADD_CREDITS")
        ContextCompat.registerReceiver(
            this,
            adminReceiver,
            filter,
            ContextCompat.RECEIVER_EXPORTED
        )

        Log.d("BlackJackAdmin", "Admin Console Active!")
        Log.d("BlackJackAdmin", "Use: adb shell am broadcast -a com.weberpackage.blackjack.ADD_CREDITS --ei amount 1000000")

        setContent {
            val theme by viewModel.theme
            BlackJackTheme(appTheme = theme) {
                NavigationRoot3(mainViewModel = viewModel)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(adminReceiver)
    }
}
