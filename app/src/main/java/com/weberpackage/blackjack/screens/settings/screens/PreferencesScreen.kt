package com.weberpackage.blackjack.screens.settings.screens

import android.content.res.Configuration
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.PhoneAndroid
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.weberpackage.blackjack.MainViewModel
import com.weberpackage.blackjack.MainViewModelFactory
import com.weberpackage.blackjack.R
import com.weberpackage.blackjack.coredata.PreferenceManager
import com.weberpackage.blackjack.screens.structure.cardColorStops
import com.weberpackage.blackjack.screens.structure.gradientBackground
import com.weberpackage.blackjack.ui.theme.AppTheme
import com.weberpackage.blackjack.ui.theme.BlackJackTheme


@Composable
internal fun PreferencesScreen(
    contentPadding: PaddingValues = PaddingValues(),
    enabled: Boolean = true,
    mainViewModel: MainViewModel,
) {
    val currentLanguage by mainViewModel.language

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBackground())
            .padding(contentPadding)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            AppearanceSection(mainViewModel = mainViewModel, enabled = enabled)
            LanguageSection(
                selectedLanguage = currentLanguage,
                onLanguageSelected = { mainViewModel.setLanguage(it) }
            )
        }
    }
}

@Composable
private fun AppearanceSection(
    enabled: Boolean = true,
    mainViewModel: MainViewModel
) {
    val currentTheme by mainViewModel.theme

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Brush.horizontalGradient(colorStops = cardColorStops()))
    ) {
        Column(Modifier.selectableGroup()) {
            // Title
            Text(
                text = stringResource(R.string.display_section),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(start = 16.dp, top = 10.dp)
            )
            ThemeOption(
                text = stringResource(R.string.system_mode),
                appearanceImage = Icons.Outlined.PhoneAndroid,
                selected = currentTheme == AppTheme.SYSTEM,
                onClick = { mainViewModel.setTheme(AppTheme.SYSTEM) },
                enabled = enabled
            )
            ThemeOption(
                text = stringResource(R.string.light_mode),
                appearanceImage = Icons.Outlined.WbSunny,
                selected = currentTheme == AppTheme.LIGHT,
                onClick = { mainViewModel.setTheme(AppTheme.LIGHT) },
                enabled = enabled
            )
            ThemeOption(
                text = stringResource(R.string.dark_mode),
                appearanceImage = Icons.Outlined.DarkMode,
                selected = currentTheme == AppTheme.DARK,
                onClick = { mainViewModel.setTheme(AppTheme.DARK) },
                enabled = enabled
            )
        }
    }
}

@Composable
fun ThemeOption(
    text: String,
    appearanceImage: ImageVector,
    selected: Boolean,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(56.dp)
            .selectable(
                selected = selected, onClick = onClick, enabled = enabled, role = Role.RadioButton
            )
            .padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = null, // null recommended for accessibility with screen readers
            enabled = enabled
        )
        Spacer(Modifier.width(3.dp))
        Icon(
            imageVector = appearanceImage, contentDescription = "Appearance Image"
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(start = 5.dp)
        )
    }
}

@Composable
internal fun LanguageSection(
    selectedLanguage: String,
    onLanguageSelected: (String) -> Unit // Passes the selected language string back up to your view model
) {
    // List of language options to render inside the dropdown menu
    val languages = listOf("English (Default)", "Español", "Français", "Deutsch")
    var menuExpanded by remember { mutableStateOf(false) }

    val arrowRotationDegree by animateFloatAsState(
        targetValue = if (menuExpanded) 0f else -90f,
        animationSpec = tween(durationMillis = 200), // Speed of rotation
        label = "ArrowRotation"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Brush.horizontalGradient(colorStops = cardColorStops()))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Title
            Text(
                text = stringResource(R.string.language_section),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
            )

            Box {
                OutlinedCard(
                    onClick = { menuExpanded = true },
                    modifier = Modifier
                        .width(160.dp)
                        .height(50.dp),
                    shape = RoundedCornerShape(24.dp),
                    border = BorderStroke(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline
                    ),
                    colors = CardDefaults.outlinedCardColors(
                        containerColor = MaterialTheme.colorScheme.inverseOnSurface.copy(0.5f)
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 10.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = selectedLanguage,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = null,
                                modifier = Modifier
                                    .rotate(arrowRotationDegree),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false },
                    modifier = Modifier.width(150.dp)
                ) {
                    languages.forEach { language ->
                        DropdownMenuItem(
                            text = { Text(text = language) },
                            onClick = {
                                onLanguageSelected(language)
                                menuExpanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
private fun PreferencesScreenPreview() {
    val context = LocalContext.current
    val preferenceManager = PreferenceManager(context)
    val viewModel: MainViewModel = viewModel(factory = MainViewModelFactory(preferenceManager))
    BlackJackTheme {
        PreferencesScreen(
            mainViewModel = viewModel,
        )
    }
}