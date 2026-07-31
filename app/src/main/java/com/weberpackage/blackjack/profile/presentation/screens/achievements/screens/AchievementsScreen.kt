package com.weberpackage.blackjack.profile.presentation.screens.achievements

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.weberpackage.blackjack.R
import com.weberpackage.blackjack.common.presentation.base.SIDE_EFFECTS_KEY
import com.weberpackage.blackjack.common.presentation.components.InitialLoadingProgress
import com.weberpackage.blackjack.common.presentation.components.StandardScaffold
import com.weberpackage.blackjack.common.presentation.theme.BlackJackTheme
import com.weberpackage.blackjack.common.presentation.utils.showAlerter
import com.weberpackage.blackjack.profile.presentation.screens.achievements.components.AchievementDetailBottomSheet
import com.weberpackage.blackjack.profile.presentation.screens.achievements.components.AchievementItem
import com.weberpackage.blackjack.profile.presentation.screens.achievements.contract.AchievementsContract
import com.weberpackage.blackjack.profile.presentation.screens.achievements.utils.AchievementUtils.initialAchievements
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun AchievementsScreenDest(
    navController: NavHostController,
    viewModel: AchievementsViewModel = hiltViewModel()
) {
    AchievementsScreen(
        state = viewModel.viewState.value,
        effectFlow = viewModel.effect,
        onEventSent = { viewModel.setEvent(it) },
        onNavigationRequested = { effect ->
            when (effect) {
                is AchievementsContract.Effect.Navigation.Back -> navController.popBackStack()
            }
        }
    )
}

@Composable
fun AchievementsScreen(
    state: AchievementsContract.State,
    effectFlow: Flow<AchievementsContract.Effect>?,
    onEventSent: (event: AchievementsContract.Event) -> Unit,
    onNavigationRequested: (AchievementsContract.Effect.Navigation) -> Unit
) {
    val hazeState = rememberHazeState()
    val activity = androidx.activity.compose.LocalActivity.current

    var showDetailSheet by remember { mutableStateOf(false) }
    var selectedAchievementIndex by remember { mutableIntStateOf(0) }

    if (showDetailSheet) {
        AchievementDetailBottomSheet(
            achievements = state.achievements,
            initialIndex = selectedAchievementIndex,
            onDismissRequest = { showDetailSheet = false }
        )
    }

    HandleSideEffects(
        effectFlow = effectFlow,
        onNavigationRequested = onNavigationRequested,
        activity = activity
    )

    StandardScaffold(
        title = stringResource(R.string.achievements_title),
        hazeState = hazeState,
        showNavigationIcon = true,
        navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
        onNavigate = { onEventSent(AchievementsContract.Event.OnBackClicked) }
    ) { contentPadding ->
        if (state.isLoading) {
            InitialLoadingProgress()
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .hazeSource(hazeState)
                    .padding(contentPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                itemsIndexed(state.achievements) { index, achievement ->
                    AchievementItem(
                        achievement = achievement,
                        onClaimClicked = { onEventSent(AchievementsContract.Event.OnClaimClicked(achievement)) },
                        onClick = {
                            selectedAchievementIndex = index
                            showDetailSheet = true
                        }
                    )
                }
            }
        }
    }
}


@Composable
private fun HandleSideEffects(
    effectFlow: Flow<AchievementsContract.Effect>?,
    onNavigationRequested: (AchievementsContract.Effect.Navigation) -> Unit,
    activity: android.app.Activity?
) {
    LaunchedEffect(SIDE_EFFECTS_KEY) {
        effectFlow?.onEach { effect ->
            when (effect) {
                is AchievementsContract.Effect.Navigation -> {
                    onNavigationRequested(effect)
                }
                is AchievementsContract.Effect.Notification -> {
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
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
private fun AchievementsScreenPreview() {
    BlackJackTheme {
        AchievementsScreen(
            state = AchievementsContract.State(
                isLoading = false,
                achievements = initialAchievements.mapIndexed { index, achievement ->
                    when (index) {
                        0 -> achievement.copy(currentValue = 1, isUnlocked = true)
                        1 -> achievement.copy(currentValue = 45)
                        2 -> achievement.copy(currentValue = 15000)
                        else -> achievement
                    }
                }
            ),
            onEventSent = {},
            effectFlow = null,
            onNavigationRequested = {}
        )
    }
}