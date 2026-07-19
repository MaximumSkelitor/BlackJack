package com.weberpackage.blackjack

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.weberpackage.blackjack.common.presentation.base.SIDE_EFFECTS_KEY
import com.weberpackage.blackjack.common.presentation.components.EventAlertDialog
import com.weberpackage.blackjack.common.presentation.contract.MainContract
import com.weberpackage.blackjack.common.presentation.state.EventDialogState
import com.weberpackage.blackjack.common.presentation.state.rememberEventDialogState
import com.weberpackage.blackjack.common.presentation.theme.BlackJackTheme
import com.weberpackage.blackjack.common.presentation.utils.DialogController
import com.weberpackage.blackjack.common.presentation.utils.ObserveAsEvents
import com.weberpackage.blackjack.core.prefs.Pref
import com.weberpackage.blackjack.core.prefs.Prefs
import com.weberpackage.blackjack.core.utils.UpdateManager
import com.weberpackage.blackjack.navigation.NavigationRoot3
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var appUpdateManager: UpdateManager

    @Inject
    lateinit var prefs: Prefs

    private val viewModel by viewModels<MainViewModel>()


    private val adminReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == "com.weberpackage.blackjack.ADD_CREDITS") {
                val amount = intent.getIntExtra("amount", 0)
                viewModel.setEvent(MainContract.Event.UpdateTotalCredits(amount))

                Timber.d("Added $amount credits via ADB")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
        enableEdgeToEdge()

        if (savedInstanceState == null) {
            prefs.set(Pref.customBet, 100)
        }

        // Register Admin Receiver
        val filter = IntentFilter("com.weberpackage.blackjack.ADD_CREDITS")
        ContextCompat.registerReceiver(
            this,
            adminReceiver,
            filter,
            ContextCompat.RECEIVER_EXPORTED 
        )

        Timber.d("Admin Console Active!")
        Timber.d("Use: adb shell am broadcast -a com.weberpackage.blackjack.ADD_CREDITS --ei amount 1000000")

        setContent {
            val state = viewModel.viewState.value
            val effectFlow = viewModel.effect
            val eventDialog = rememberEventDialogState()

            ObserveDialogEvents(eventDialog = eventDialog)

            HandleSideEffects(
                effectFlow = effectFlow
            )

            BlackJackTheme(appTheme = state.appTheme) {
                Surface {
                    EventAlertDialog(eventDialogState = eventDialog)
                }
                NavigationRoot3(
                    totalChips = state.totalCredits,
                    hasSetUsername = state.hasSetUsername
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(adminReceiver)
    }

    @Composable
    private fun ObserveDialogEvents(eventDialog: EventDialogState) {
        ObserveAsEvents(
            flow = DialogController.events
        ) { event ->
            eventDialog.show(dialogEvent = event)
        }
    }

    @Composable
    private fun HandleSideEffects(
        effectFlow: Flow<MainContract.Effect>,
    ) {
        LaunchedEffect(SIDE_EFFECTS_KEY) {
            effectFlow.onEach { effect ->
                when (effect) {
                    is MainContract.Effect.CheckForAppUpdates -> checkForUpdates()
                    else -> {}
                }
            }.collect()
        }
    }

    private fun checkForUpdates() {
        lifecycleScope.launch {
            appUpdateManager.checkForUpdate(this@MainActivity)
        }
    }
}
