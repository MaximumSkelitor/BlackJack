package com.weberpackage.blackjack.screens.settings.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.weberpackage.blackjack.R
import com.weberpackage.blackjack.coredata.PreferenceManager
import com.weberpackage.blackjack.screens.structure.SimpleTopBar

@Composable
fun UsernameScreen(
    onBack: () -> Unit,
    preferenceManager: PreferenceManager
) {
    var username by remember { mutableStateOf(preferenceManager.getUsername()) }
    val maxChar = 15

    val cornerShape = RoundedCornerShape(12.dp)

    SimpleTopBar(
        screenTitle = R.string.username,
        onBack = onBack,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = stringResource(R.string.username_title),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = stringResource(R.string.username_desc),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            OutlinedTextField(
                value = username,
                onValueChange = { if (it.length <= maxChar) username = it },
                label = { Text(stringResource(R.string.username)) },
                placeholder = { Text("Enter your username") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                supportingText = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text("${username.length} / $maxChar")
                    }
                }
            )

            Button(
                onClick = {
                    if (username.isNotBlank()) {
                        preferenceManager.saveUsername(username.trim())
                        onBack()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .shadow(elevation = 8.dp, shape = cornerShape, clip = false),
                shape = cornerShape,
                enabled = username.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.inverseOnSurface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Save, contentDescription = null)
                    Text(stringResource(R.string.save_username))
                }
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
private fun UsernameScreenPreview() {
    val context = LocalContext.current
    val preferenceManager = remember { PreferenceManager(context) }
    UsernameScreen(
        onBack = {},
        preferenceManager = preferenceManager
    )
}
