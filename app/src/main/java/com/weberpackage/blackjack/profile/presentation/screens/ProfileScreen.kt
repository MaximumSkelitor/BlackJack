package com.weberpackage.blackjack.profile.presentation.screens

import android.content.res.Configuration
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
import com.weberpackage.blackjack.common.presentation.theme.BlackJackTheme
import com.weberpackage.blackjack.profile.presentation.components.EquippableCardPack
import com.weberpackage.blackjack.profile.presentation.components.StatColumn
import com.weberpackage.blackjack.profile.presentation.contract.ProfileContract
import com.weberpackage.blackjack.profile.presentation.model.ProfileState
import com.weberpackage.blackjack.shop.presentation.model.cardPacks
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach


@Composable
fun ProfileScreenDest(
    contentPadding: PaddingValues,
    navController: NavHostController,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    ProfileScreen(
        contentPadding = contentPadding,
        state = viewModel.viewState.value,
        effectFlow = viewModel.effect,
        onEventSent = { event -> viewModel.setEvent(event) },
        onNavigationRequested = { navigationEffect ->
            when (navigationEffect) {
                is ProfileContract.Effect.Navigation.Back -> navController.popBackStack()
                is ProfileContract.Effect.Navigation.NavRoute -> {
                }

                is ProfileContract.Effect.Navigation.NavDest -> {
                    navController.navigate(navigationEffect.route)
                }
            }
        }
    )
}

@Composable
private fun ProfileScreen(
    contentPadding: PaddingValues,
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
                state = state,
                onEquipPack = { onEventSent(ProfileContract.Event.OnEquipPack(it)) },
                contentPadding = contentPadding,
            )
        }
    }
}

@Composable
fun ProfileScreenContent(
    state: ProfileContract.State,
    onEquipPack: (Int) -> Unit,
    contentPadding: PaddingValues = PaddingValues()
) {
    var currentEquipped by remember(state.profileState.equippedPack) { 
        mutableIntStateOf(state.profileState.equippedPack) 
    }
    val ownedPackItems = remember(state.profileState.ownedPacks) {
        cardPacks.filter { state.profileState.ownedPacks.contains(it.id) }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
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
                // Profile Picture
                Icon(
                    modifier = Modifier
                        .size(100.dp)
                        .border(
                            BorderStroke(4.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)),
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
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    StatColumn(
                        label = stringResource(R.string.balance),
                        value = formatChips(state.profileState.totalChips)
                    )
                    StatColumn(
                        label = stringResource(R.string.best),
                        value = formatChips(state.profileState.highestChips)
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
    LaunchedEffect(SIDE_EFFECTS_KEY) {
        effectFlow?.onEach { effect ->
            when (effect) {
                is ProfileContract.Effect.Navigation -> {
                    onNavigationRequested(effect)
                }

                is ProfileContract.Effect.Notification -> {
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
            onEquipPack = {}
        )
    }
}
