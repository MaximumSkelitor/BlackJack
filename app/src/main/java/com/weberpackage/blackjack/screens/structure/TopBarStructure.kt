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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.weberpackage.blackjack.R
import com.weberpackage.blackjack.navigation.NavigationItem
import com.weberpackage.blackjack.ui.theme.BlackJackTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarStructure(
    @StringRes screenTitle: Int,
    screenToggle: () -> Unit,
    navigationIconItem: NavigationItem,
    content: @Composable (PaddingValues) -> Unit // Slot for screen content
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
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
                    IconButton(onClick = screenToggle) {
                        Icon(
                            imageVector = navigationIconItem.selectedIcon,
                            contentDescription = "Settings"
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
fun GameplayTopBar(
    @StringRes screenTitle: Int,
    totalChips: Int,
    toggleBack: () -> Unit,
    content: @Composable (PaddingValues) -> Unit // Slot for screen content
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
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
                        Text(
                            text = stringResource(R.string.total_credits, totalChips),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold
                        )
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
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
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
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
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
    BlackJackTheme {
        TopBarStructure(
            screenTitle = R.string.home,
            screenToggle = {},
            navigationIconItem = NavigationItem.ProfileScreen
        ) {}
//        GameplayTopBar(
//            screenTitle = R.string.practice,
//            toggleBack = {},
//            totalChips = 500,
//            content = { Text("Screen Content") }
//        )
    }
}