package com.weberpackage.blackjack.screens.dashboard

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.weberpackage.blackjack.R
import com.weberpackage.blackjack.coredata.PreferenceManager
import com.weberpackage.blackjack.navigation.NavigationItem
import com.weberpackage.blackjack.screens.practice.PracticeViewModel
import com.weberpackage.blackjack.screens.practice.PracticeViewModelFactory
import com.weberpackage.blackjack.screens.structure.TopBarStructure
import com.weberpackage.blackjack.ui.theme.BlackJackTheme
import kotlin.math.abs
import kotlin.math.min


// 1. Data model representation
data class SelectionItem(
    val id: Int,
    val titleResId: Int,
    val subtitleResId: Int,
    val image: ImageVector
)

@Composable
private fun SelectionRowContainer(
    onNavigateToPlayNow: () -> Unit,
    onNavigateToPractice: () -> Unit,
    onNavigateToMultiplayer: () -> Unit,
    modifier: Modifier = Modifier
) {
    val itemsList = remember {
        listOf(
            SelectionItem(
                id = 1,
                titleResId = R.string.play_now_mode,
                subtitleResId = R.string.play_now_subtitle,
                image = Icons.Filled.Casino
            ),
            SelectionItem(
                id = 2,
                titleResId = R.string.practice_mode,
                subtitleResId = R.string.practice_subtitle,
                image = Icons.AutoMirrored.Filled.MenuBook,
            ),
            SelectionItem(
                id = 23,
                titleResId = R.string.multiplayer,
                subtitleResId = R.string.multiplayer_subtitle,
                image = Icons.Filled.People,
            ),
        )
    }

    val listState = rememberLazyListState()

    LazyRow(
        modifier = modifier.fillMaxWidth(),
        state = listState,
        flingBehavior = rememberSnapFlingBehavior(lazyListState = listState),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Using itemsIndexed so each card knows its index for calculation
        itemsIndexed(itemsList) { index, item ->

            // Calculate scale dynamically based on distance to viewport center
            val scale by remember {
                derivedStateOf {
                    val layoutInfo = listState.layoutInfo
                    val visibleItemsInfo = layoutInfo.visibleItemsInfo
                    val itemInfo = visibleItemsInfo.firstOrNull { it.index == index }

                    if (itemInfo != null) {
                        // Viewport center
                        val viewportCenter =
                            (layoutInfo.viewportEndOffset + layoutInfo.viewportStartOffset) / 2f
                        // Card center
                        val itemCenter = itemInfo.offset + (itemInfo.size / 2f)

                        val distanceFromCenter = abs(viewportCenter - itemCenter)
                        val maxDistance = layoutInfo.viewportEndOffset / 2f
                        val progress = min(1f, distanceFromCenter / maxDistance)

                        // 1.0f when centered, scales down to 0.85f when scrolled away
                        1f - (progress * 0.15f)
                    } else {
                        0.85f // Off-screen or hidden items base scale
                    }
                }
            }

            // Apply scale using graphicsLayer to prevent costly layout recalculations
            SelectionCard(
                item = item,
                onClick = {
                    when (item.id) {
                        1 -> onNavigateToPlayNow()
                        2 -> onNavigateToPractice()
                        3 -> onNavigateToMultiplayer()
                    }
                },
                modifier = Modifier.graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                }
            )
        }
    }
}

@Composable
private fun SelectionCard(
    item: SelectionItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(300.dp)
            .height(200.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.inverseOnSurface)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Icon(
                    modifier = Modifier.size(80.dp),
                    imageVector = item.image,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
                // Bottom textual elements
                Column {
                    Text(
                        text = stringResource(item.titleResId),
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stringResource(item.subtitleResId),
                        color = MaterialTheme.colorScheme.onSurface.copy(0.8f),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onNavigateToPlayNow: () -> Unit,
    onNavigateToPractice: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToMultiplayer: () -> Unit,
    viewModel: PracticeViewModel,
    userUsername: String
) {
    val totalChips = viewModel.totalChips

    TopBarStructure(
        screenTitle = R.string.home,
        screenToggle = onNavigateToProfile,
        navigationIconItem = NavigationItem.ProfileScreen,
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Spacer(Modifier.padding())
                Text(
                    text = stringResource(R.string.welcome_user, userUsername),
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = totalChips.toString(),
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.displayLarge
                )
                SelectionRowContainer(
                    onNavigateToPlayNow = onNavigateToPlayNow,
                    onNavigateToPractice = onNavigateToPractice,
                    onNavigateToMultiplayer = onNavigateToMultiplayer
                )
            }
        }
    )
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
private fun DashboardScreenPreview() {
    val context = LocalContext.current
    val preferenceManager = remember { PreferenceManager(context) }
    val viewModel: PracticeViewModel = viewModel(
        factory = PracticeViewModelFactory(preferenceManager)
    )
    BlackJackTheme {
        DashboardScreen(
            onNavigateToPlayNow = {},
            onNavigateToPractice = {},
            onNavigateToProfile = {},
            onNavigateToMultiplayer = {},
            viewModel = viewModel,
            userUsername = preferenceManager.getUsername()
        )
    }
}
