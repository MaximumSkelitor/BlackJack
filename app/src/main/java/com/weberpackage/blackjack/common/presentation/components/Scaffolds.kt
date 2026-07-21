package com.weberpackage.blackjack.common.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.weberpackage.blackjack.common.presentation.base.glowBackground
import dev.chrisbanes.haze.HazeState

@Composable
fun StandardScaffold(
    modifier: Modifier = Modifier,
    title: String,
    hazeState: HazeState,
    totalChips: Int? = null,
    showNavigationIcon: Boolean,
    navigationIcon: ImageVector,
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
    onNavigate: (() -> Unit)? = null,
    showText: Boolean = true,
    showChipIcon: Boolean = false,
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
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
                                .then(
                                    if (totalChips != null)
                                        Modifier.fillMaxWidth(0.35f) else Modifier
                                )
                        ) {
                            Text(
                                fontWeight = FontWeight.Bold,
                                text = it,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        totalChips?.let { chips ->
                            ChipCounter(
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .padding(start = 40.dp),
                                count = chips,
                                fontSize = 20.sp,
                                showText = showText,
                                showChipIcon = showChipIcon
                            )
                        }
                    }
                },
                scrollBehavior = scrollBehavior,
                hazeState = hazeState,
                showNavigationIcon = showNavigationIcon,
                navigationIcon = navigationIcon,
                onNavigate = {
                    onNavigate?.invoke()
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = contentColorFor(MaterialTheme.colorScheme.background),
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(glowBackground()
                )
        ) {
            content(contentPadding)
        }
    }
}