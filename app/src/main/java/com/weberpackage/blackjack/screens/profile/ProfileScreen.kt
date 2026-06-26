package com.weberpackage.blackjack.screens.profile

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.weberpackage.blackjack.R
import com.weberpackage.blackjack.navigation.NavigationItem
import com.weberpackage.blackjack.screens.structure.TopBarStructure
import com.weberpackage.blackjack.ui.theme.BlackJackTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    totalChips: Int,
    highestChips: Int,
    settingsToggle: () -> Unit,
    userUsername: String,
) {
    TopBarStructure(
        screenTitle = R.string.profile,
        screenToggle = settingsToggle,
        navigationIconItem = NavigationItem.SettingsScreen,
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize(1f)
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(1f)
                        .padding(top = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    // Profile Picture
                    Icon(
                        modifier = Modifier
                            .size(85.dp)
                            .border(
                                BorderStroke(3.dp, Color(0xFF8A8A8A)), CircleShape
                            )
                            .background(Color(0xFFBDBDBD), CircleShape)
                            .clip(CircleShape)
                            .padding(8.dp),
                        imageVector = Icons.Filled.Person,
                        tint = Color.White,
                        contentDescription = "Profile"
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Column(
                        modifier = Modifier.padding(horizontal = 50.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(
                                R.string.users_username,
                                userUsername
                            ),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        ProfileDescriptions(R.string.total_chips_profile, amount = totalChips)
                        ProfileDescriptions(R.string.highest_chips_profile, amount = highestChips)
                    }
                }
            }
        })
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
fun ProfileScreenPreview() {
    BlackJackTheme {
        ProfileScreen(
            totalChips = 1000, highestChips = 2000, settingsToggle = {}, userUsername = "Player"
        )
    }
}