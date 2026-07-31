package com.weberpackage.blackjack.profile.presentation.screens

import android.content.res.Configuration
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.weberpackage.blackjack.R
import com.weberpackage.blackjack.common.presentation.base.SIDE_EFFECTS_KEY
import com.weberpackage.blackjack.common.presentation.base.formatChips
import com.weberpackage.blackjack.common.presentation.components.InitialLoadingProgress
import com.weberpackage.blackjack.common.presentation.navigation.NavRoutes
import com.weberpackage.blackjack.common.presentation.theme.BlackJackTheme
import com.weberpackage.blackjack.common.presentation.utils.safeNavigate
import com.weberpackage.blackjack.common.presentation.utils.showAlerter
import com.weberpackage.blackjack.dashboard.presentation.components.RankDetailBottomSheet
import com.weberpackage.blackjack.dashboard.presentation.utils.RankUtils
import com.weberpackage.blackjack.profile.presentation.components.AchievementsBar
import com.weberpackage.blackjack.profile.presentation.components.EquippableCardPack
import com.weberpackage.blackjack.profile.presentation.components.RankProgressBar
import com.weberpackage.blackjack.profile.presentation.components.StatColumn
import com.weberpackage.blackjack.profile.presentation.contract.ProfileContract
import com.weberpackage.blackjack.profile.presentation.model.ProfileState
import com.weberpackage.blackjack.shop.presentation.model.cardPacks
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun ProfileScreenDest(
    navController: NavHostController,
    contentPadding: PaddingValues,
    hazeState: HazeState,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    ProfileScreen(
        contentPadding = contentPadding,
        hazeState = hazeState,
        state = viewModel.viewState.value,
        effectFlow = viewModel.effect,
        onEventSent = { event -> viewModel.setEvent(event) },
        onNavigationRequested = { navigationEffect ->
            when (navigationEffect) {
                is ProfileContract.Effect.Navigation.Back -> navController.popBackStack()
                is ProfileContract.Effect.Navigation.NavRoute -> {
                    navController.safeNavigate(
                        route = navigationEffect.route,
                        popUpToRoute = navigationEffect.popUpToRoute,
                        inclusive = navigationEffect.inclusive
                    )
                }
            }
        }
    )
}

@Composable
private fun ProfileScreen(
    contentPadding: PaddingValues,
    hazeState: HazeState,
    state: ProfileContract.State,
    effectFlow: Flow<ProfileContract.Effect>?,
    onEventSent: (event: ProfileContract.Event) -> Unit,
    onNavigationRequested: (ProfileContract.Effect.Navigation) -> Unit
) {
    HandleSideEffects(
        effectFlow = effectFlow,
        onNavigationRequested = onNavigationRequested
    )

    AnimatedContent(
        targetState = state.isInitialLoading,
        label = "ProfileLoading"
    ) { isInitialLoading ->
        if (isInitialLoading) {
            InitialLoadingProgress()
        } else {
            ProfileScreenContent(
                hazeState = hazeState,
                state = state,
                onEquipPack = { onEventSent(ProfileContract.Event.OnEquipPack(it)) },
                onNavigationRequested = onNavigationRequested,
                contentPadding = contentPadding,
            )
        }
    }
}

@Composable
fun ProfileScreenContent(
    hazeState: HazeState,
    state: ProfileContract.State,
    onEquipPack: (Int) -> Unit,
    onNavigationRequested: (ProfileContract.Effect.Navigation) -> Unit,
    contentPadding: PaddingValues = PaddingValues()
) {
    var currentEquipped by remember(state.profileState.equippedPack) {
        mutableIntStateOf(state.profileState.equippedPack)
    }
    var showRankSheet by remember { mutableStateOf(false) }

    val ownedPackItems = remember(state.profileState.ownedPacks) {
        cardPacks.filter { state.profileState.ownedPacks.contains(it.id) }
    }

    if (showRankSheet) {
        val targetRank = state.profileState.nextRank ?: state.profileState.currentRank
        val rankIndex = RankUtils.ranks.indexOf(targetRank)
        RankDetailBottomSheet(
            initialRankIndex = if (rankIndex != -1) rankIndex else 0,
            userChips = state.profileState.totalChips,
            userGamesPlayed = state.profileState.gamesPlayed,
            onDismissRequest = { showRankSheet = false }
        )
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .hazeSource(state = hazeState)
            .padding(contentPadding),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            // Top Side
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                // Profile Picture and stats
                Icon(
                    modifier = Modifier
                        .size(100.dp)
                        .border(
                            BorderStroke(
                                4.dp,
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                            ),
                            CircleShape
                        )
                        .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                        .clip(CircleShape)
                        .padding(12.dp),
                    imageVector = Icons.Filled.Person,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    contentDescription = "Profile"
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = state.profileState.username,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Row(
                    modifier = Modifier.padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    StatColumn(
                        label = stringResource(R.string.best),
                        value = formatChips(state.profileState.highestChips)
                    )
                    StatColumn(
                        label = stringResource(R.string.career_credits),
                        value = formatChips(state.profileState.careerCredits.toInt())
                    )
                    StatColumn(
                        label = stringResource(R.string.games_played),
                        value = formatChips(state.profileState.gamesPlayed.toInt())
                    )
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AchievementsBar(
                        unlockedCount = state.profileState.unlockedAchievements,
                        totalCount = state.profileState.totalAchievements,
                        onClick = {
                            onNavigationRequested(
                                ProfileContract.Effect.Navigation.NavRoute(
                                    NavRoutes.ProfileGraph
                                )
                            )
                        }
                    )

                    RankProgressBar(
                        rank = state.profileState.nextRank,
                        progress = state.profileState.rankProgress,
                        isExpanded = showRankSheet,
                        onClick = { showRankSheet = true }
                    )
                }



                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                )

                // Bottom Side
                Text(
                    text = stringResource(R.string.my_card_packs),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }
        }

        items(ownedPackItems) { pack ->
            EquippableCardPack(
                pack = pack,
                isEquipped = currentEquipped == pack.id,
                onClick = {
                    currentEquipped = pack.id
                    onEquipPack(pack.id)
                }
            )
        }

        item(span = { GridItemSpan(maxLineSpan) }) {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun HandleSideEffects(
    effectFlow: Flow<ProfileContract.Effect>?,
    onNavigationRequested: (ProfileContract.Effect.Navigation) -> Unit
) {
    val activity = LocalActivity.current
    LaunchedEffect(SIDE_EFFECTS_KEY) {
        effectFlow?.onEach { effect ->
            when (effect) {
                is ProfileContract.Effect.Navigation -> {
                    onNavigationRequested(effect)
                }

                is ProfileContract.Effect.Notification -> {
                    activity?.showAlerter(
                        message = effect.text,
                        isError = effect.error
                    )
                }
            }
        }?.collect()
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun ProfileScreenPreview() {
    BlackJackTheme {
        ProfileScreenContent(
            hazeState = HazeState(),
            state = ProfileContract.State(
                profileState = ProfileState(
                    username = "Player",
                    totalChips = 1000,
                    highestChips = 1000,
                    ownedPacks = listOf(1, 2, 3),
                    equippedPack = 1,
                ),
                isInitialLoading = false
            ),
            onEquipPack = {},
            onNavigationRequested = {}
        )
    }
}
