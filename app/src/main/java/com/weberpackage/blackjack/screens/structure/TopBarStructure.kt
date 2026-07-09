package com.weberpackage.blackjack.screens.structure

import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.weberpackage.blackjack.ui.theme.BlackJackTheme
import com.weberpackage.blackjack.R
import com.weberpackage.blackjack.coredata.PreferenceManager
import com.weberpackage.blackjack.navigation.NavigationItem
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.rememberHazeState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarStructure(
    @StringRes screenTitle: Int,
    screenToggle: () -> Unit,
    screenToggleEnabled: Boolean = true,
    navigationIconItem: NavigationItem,
    content: @Composable (PaddingValues) -> Unit // Slot for screen content
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Transparent,
        contentColor = contentColorFor(MaterialTheme.colorScheme.background),
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = stringResource(screenTitle),
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                },
                navigationIcon = {},
                actions = {
                    if (screenToggleEnabled) {
                        IconButton(onClick = screenToggle) {
                            Icon(
                                imageVector = navigationIconItem.selectedIcon,
                                contentDescription = "Settings"
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.onSurface
                ),
            )
        }
    ) { innerPadding ->
        // Pass the padding to the content block to handle edge-to-edge drawing correctly
        content(innerPadding)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameplayTopBar(
    @StringRes screenTitle: Int,
    totalChips: Int,
    toggleBack: () -> Unit,
    content: @Composable (PaddingValues) -> Unit,
    showTotalChips: Boolean = true // Slot for screen content
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Transparent,
        contentColor = contentColorFor(MaterialTheme.colorScheme.background),
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = stringResource(screenTitle),
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.width(20.dp))
                        if (showTotalChips) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Spacer(Modifier.width(30.dp))
                                ChipCounter(totalChips, fontSize = 20, showText = true)
                            }
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = toggleBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.onSurface
                ),
            )
        }
    ) { innerPadding ->
        // Pass the padding to the content block to handle edge-to-edge drawing correctly
        content(innerPadding)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleTopBar(
    @StringRes screenTitle: Int,
    onBack: () -> Unit,
    showBackButton: Boolean = true,
    showTotalChips: Boolean = false,
    preferenceManager: PreferenceManager,
    style: HazeStyle = hazeAppBarStyle(),
    content: @Composable (PaddingValues) -> Unit
) {
    val totalChips = preferenceManager.getChips()
    val hazeState = rememberHazeState()

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        containerColor = Color.Transparent,
        contentColor = contentColorFor(MaterialTheme.colorScheme.background),
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .hazeEffect(
                        state = hazeState,
                        style = style
                    ),
                title = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = stringResource(screenTitle),
                            fontWeight = FontWeight.Bold
                        )
                        if (showTotalChips) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Spacer(Modifier.width(30.dp))
                                ChipCounter(totalChips, fontSize = 20, showText = true)
                            }
                        }
                    }
                },
                navigationIcon = {
                    if (showBackButton) {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface
                ),
            )
        }
    ) { innerPadding ->
        content(innerPadding)
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
fun TopBarStructurePreview() {
    val context = LocalContext.current
    val preferenceManager = remember { PreferenceManager(context) }
    BlackJackTheme {
//        TopBarStructure(
//            screenTitle = R.string.home,
//            screenToggle = {},
//            navigationIconItem = NavigationItem.ProfileScreen
//        ) {}
//        GameplayTopBar(
//            screenTitle = R.string.practice,
//            toggleBack = {},
//            totalChips = 500,
//            content = { Text("Screen Content") }
//        )
        SimpleTopBar(
            screenTitle = R.string.shop,
            onBack = {},
            showBackButton = false,
            showTotalChips = true,
            preferenceManager = preferenceManager,
        ) {
        }
    }
}

