package com.weberpackage.blackjack

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.weberpackage.blackjack.coredata.PreferenceManager
import com.weberpackage.blackjack.navigation.NavigationRoot3
import com.weberpackage.blackjack.ui.theme.BlackJackTheme

class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory(PreferenceManager(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
        enableEdgeToEdge()
        setContent {
            val theme by viewModel.theme
            BlackJackTheme(appTheme = theme) {
                NavigationRoot3(mainViewModel = viewModel)
            }
        }
    }
}

