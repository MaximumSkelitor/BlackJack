package com.weberpackage.blackjack.home.presentation.screens

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.weberpackage.blackjack.R
import com.weberpackage.blackjack.common.presentation.base.SIDE_EFFECTS_KEY
import com.weberpackage.blackjack.common.presentation.base.getPagerAnimationSpec
import com.weberpackage.blackjack.common.presentation.base.glowBackground
import com.weberpackage.blackjack.common.presentation.components.ChipCounter
import com.weberpackage.blackjack.common.presentation.components.HazeAppBar
import com.weberpackage.blackjack.common.presentation.navigation.NavRoutes
import com.weberpackage.blackjack.common.presentation.theme.spacing
import com.weberpackage.blackjack.common.presentation.utils.safeNavigate
import com.weberpackage.blackjack.common.presentation.utils.showAlerter
import com.weberpackage.blackjack.core.constants.AppConfig
import com.weberpackage.blackjack.dashboard.presentation.screens.DashboardScreenDest
import com.weberpackage.blackjack.home.presentation.component.BottomBar
import com.weberpackage.blackjack.home.presentation.contract.HomeContract
import com.weberpackage.blackjack.home.presentation.model.HomePages.Dashboard
import com.weberpackage.blackjack.home.presentation.model.HomePages.Profile
import com.weberpackage.blackjack.home.presentation.model.HomePages.Shop
import com.weberpackage.blackjack.home.presentation.util.buildNavigationItems
import com.weberpackage.blackjack.profile.presentation.screens.ProfileScreenDest
import com.weberpackage.blackjack.shop.presentation.screens.ShopScreenDest
import dev.chrisbanes.haze.rememberHazeState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@Composable
fun HomeScreenDest(
    navController: NavHostController,
    viewModel: HomeScreenViewModel = hiltViewModel()
) {
    HomeScreen(
        initialPage = Dashboard.page,
        navController = navController,
        state = viewModel.viewState.value,
        effectFlow = viewModel.effect,
        onEventSent = { event -> viewModel.setEvent(event) },
        onNavigationRequested = { navigationEffect ->
            when (navigationEffect) {
                is HomeContract.Effect.Navigation.Back -> navController.popBackStack()
                is HomeContract.Effect.Navigation.Changelog -> {
                    navController.safeNavigate(NavRoutes.Changelog)
                }

                is HomeContract.Effect.Navigation.NavRoute -> {
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

@Suppress("unused")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(
    initialPage: Int,
    navController: NavHostController,
    state: HomeContract.State,
    effectFlow: Flow<HomeContract.Effect>?,
    onEventSent: (event: HomeContract.Event) -> Unit,
    onNavigationRequested: (HomeContract.Effect.Navigation) -> Unit
) {
    val activity = LocalActivity.current
    val window = activity?.window
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val hazeState = rememberHazeState()
    val navigationItems = buildNavigationItems(shopNotifications = state.shopNotification)
    val scope = rememberCoroutineScope()

    var title by rememberSaveable { mutableStateOf(navigationItems[initialPage].title) }

    val pagerState = rememberPagerState(
        initialPage = initialPage,
        pageCount = { navigationItems.size }
    )

    HandleSideEffects(
        effectFlow = effectFlow,
        onNavigationRequested = onNavigationRequested
    )

    BackHandler(pagerState.currentPage != initialPage) {
        scope.launch {
            pagerState.animateScrollToPage(
                page = initialPage,
                animationSpec = getPagerAnimationSpec()
            )
        }
    }

    DisposableEffect(state.showBottomBar) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window?.isNavigationBarContrastEnforced = !state.showBottomBar
        }
        onDispose {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                window?.isNavigationBarContrastEnforced = true
            }
        }
    }

    LaunchedEffect(pagerState.currentPage) {
        title = navigationItems[pagerState.currentPage].title
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            HazeAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        AnimatedContent(
                            targetState = title,
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .fillMaxWidth(0.35f)
                        ) {
                            Text(
                                fontWeight = FontWeight.Bold,
                                text = it,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        state.totalChips?.let { chips ->
                            ChipCounter(
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .padding(start = 40.dp),
                                count = chips,
                                fontSize = 20.sp,
                                showText = true
                            )
                        }
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            onNavigationRequested(
                                HomeContract.Effect.Navigation.NavRoute(
                                    route = NavRoutes.SettingsDest.SettingsHome
                                )
                            )
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = stringResource(R.string.settings)
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
                hazeState = hazeState,
                showNavigationIcon = true,
                navigationIcon = Icons.AutoMirrored.Filled.ArrowBack
            )
        },
        bottomBar = {
            if (state.showBottomBar) {
                BottomBar(
                    navigationItems = navigationItems,
                    pagerState = pagerState,
                    onClick = { index ->
                        scope.launch {
                            pagerState.animateScrollToPage(
                                page = index,
                                animationSpec = getPagerAnimationSpec()
                            )
                        }
                    }
                )
            }
        },
        containerColor = Color.Transparent,
        contentColor = contentColorFor(MaterialTheme.colorScheme.background),
    ) { contentPadding ->
        HorizontalPager(
            userScrollEnabled = true,
            state = pagerState,
            beyondViewportPageCount = AppConfig.PAGER_BEYOND_COUNT,
            pageSpacing = MaterialTheme.spacing.smallOne,
            modifier = Modifier
                .fillMaxSize()
                .background(glowBackground())
        ) { page ->
            when (page) {
                Dashboard.page -> DashboardScreenDest(
                    navController = navController,
                    contentPadding = contentPadding,
                    hazeState = hazeState
                )

                Profile.page -> ProfileScreenDest(
                    navController = navController,
                    contentPadding = contentPadding,
                    hazeState = hazeState
                )

                Shop.page -> ShopScreenDest(
                    navController = navController,
                    contentPadding = contentPadding,
                    hazeState = hazeState
                )
            }
        }
    }

}

@Composable
private fun HandleSideEffects(
    effectFlow: Flow<HomeContract.Effect>?,
    onNavigationRequested: (HomeContract.Effect.Navigation) -> Unit
) {
    val activity = LocalActivity.current
    LaunchedEffect(SIDE_EFFECTS_KEY) {
        effectFlow?.onEach { effect ->
            when (effect) {
                is HomeContract.Effect.Navigation -> {
                    onNavigationRequested(effect)
                }

                is HomeContract.Effect.Notification -> {
                    activity?.showAlerter(
                        message = effect.text,
                        isError = effect.error
                    )
                }

                is HomeContract.Effect.OpenUpdateIntent -> {
                    activity?.startActivity(effect.intent)
                }
            }
        }?.collect()
    }
}
