package com.weberpackage.blackjack.home.presentation.component

import android.content.res.Configuration
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.weberpackage.blackjack.common.presentation.theme.BlackJackTheme
import com.weberpackage.blackjack.common.presentation.theme.spacing
import com.weberpackage.blackjack.home.presentation.model.NavigationItem
import com.weberpackage.blackjack.home.presentation.util.buildNavigationItems

private typealias index = Int

@Composable
internal fun BottomBar(
    navigationItems: List<NavigationItem>,
    pagerState: PagerState,
    onClick: (index) -> Unit
) {
    NavigationBar(
        modifier = Modifier.clip(
            RoundedCornerShape(
                topStart = MaterialTheme.spacing.mediumTwo,
                topEnd = MaterialTheme.spacing.mediumTwo
            )
        ),
        tonalElevation = 0.dp
    ) {
        navigationItems.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = pagerState.currentPage == index,
                onClick = {
                    onClick(index)
                },
                icon = {
                    BottomBarIconView(
                        isSelected = pagerState.currentPage == index,
                        selectedIcon = item.selectedIcon,
                        unselectedIcon = item.unselectedIcon,
                        title = item.title,
                        badgeCount = item.badgeText
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.onSurface.copy(.1f),
                    selectedIconColor = MaterialTheme.colorScheme.onSurface,
                    selectedTextColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    }
}

@Composable
private fun BottomBarIconView(
    isSelected: Boolean,
    selectedIcon: ImageVector,
    unselectedIcon: ImageVector,
    title: String,
    badgeCount: String? = null
) {
    BadgedBox(
        badge = {
            BottomBarBadgeView(badgeCount)
        }
    ) {
        Icon(
            modifier = Modifier.size(30.dp),
            imageVector = if (isSelected) {
                selectedIcon
            } else {
                unselectedIcon
            },
            contentDescription = title,
        )
    }
}

@Composable
private fun BottomBarBadgeView(text: String? = null) {
    if (text != null) {
        Badge {
            Text(
                text = text
            )
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
private fun BottomBarPreview() {
    val navigationItems = buildNavigationItems()
    BlackJackTheme {
        Surface {
            BottomBar(
                navigationItems = navigationItems,
                pagerState = rememberPagerState(
                    initialPage = 1,
                    pageCount = { navigationItems.size }
                ),
                onClick = { }
            )
        }
    }
}