package com.weberpackage.blackjack

import android.annotation.SuppressLint
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
import com.weberpackage.blackjack.common.presentation.navigation.RootNavGraph
import com.weberpackage.blackjack.common.presentation.state.EventDialogState
import com.weberpackage.blackjack.common.presentation.state.rememberEventDialogState
import com.weberpackage.blackjack.common.presentation.theme.BlackJackTheme
import com.weberpackage.blackjack.common.presentation.utils.DialogController
import com.weberpackage.blackjack.common.presentation.utils.ObserveAsEvents
import com.weberpackage.blackjack.core.di.ReceiverModule.AdminFilter
import com.weberpackage.blackjack.core.prefs.Prefs
import com.weberpackage.blackjack.core.receiver.AdminReceiver
import com.weberpackage.blackjack.core.utils.UpdateManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var appUpdateManager: UpdateManager

    @Inject
    lateinit var prefs: Prefs

    @Inject
    lateinit var adminReceiver: AdminReceiver

    @Inject
    @AdminFilter
    lateinit var adminFilter: IntentFilter

    private val viewModel by viewModels<MainViewModel>()

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
        enableEdgeToEdge()

        ContextCompat.registerReceiver(
            this,
            adminReceiver,
            adminFilter,
            ContextCompat.RECEIVER_EXPORTED
        )

        setContent {
            MainContent()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(adminReceiver)
    }

    @Composable
    private fun MainContent() {
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

            RootNavGraph(
                startDestination = state.startDestination
            )
        }
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
