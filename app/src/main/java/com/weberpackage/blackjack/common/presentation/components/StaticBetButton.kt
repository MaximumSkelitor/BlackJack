package com.weberpackage.blackjack.common.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.weberpackage.blackjack.common.presentation.model.BetItem

@Composable
fun StaticBetButtons(
    staticBets: List<BetItem>,
    onBetClick: (BetItem) -> Unit,
) {
    LazyVerticalGrid(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        columns = GridCells.Fixed(4),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
        contentPadding = PaddingValues(
            top = 4.dp,
            bottom = 4.dp,
            start = 4.dp,
            end = 4.dp
        ),
    ) {
        items(staticBets.size) { index ->
            val staticBet = staticBets[index]
            BlackjackButton(
                modifier = Modifier.fillMaxWidth(),
                text = staticBet.text,
                enabled = true,
                onClick = { onBetClick(staticBet) }
            )
        }
    }
}

fun buildStaticBetButtons(): List<BetItem> {
    return listOf(
        BetItem(5),
        BetItem(10),
        BetItem(50),
        BetItem(100),
        BetItem(-5),
        BetItem(-10),
        BetItem(-50),
        BetItem(0),
    )
}