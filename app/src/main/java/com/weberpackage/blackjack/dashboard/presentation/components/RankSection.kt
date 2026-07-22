package com.weberpackage.blackjack.dashboard.presentation.components

import android.content.res.Configuration
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.weberpackage.blackjack.R
import com.weberpackage.blackjack.common.presentation.base.cardColorStops
import com.weberpackage.blackjack.common.presentation.theme.BlackJackTheme
import com.weberpackage.blackjack.dashboard.presentation.utils.RankUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RankSection(
    userChips: Int,
    userGamesPlayed: Long,
    modifier: Modifier = Modifier,
    onRankClick: () -> Unit = {}
) {
    var selectedRankIndex by remember { mutableStateOf<Int?>(null) }
    val ranks = RankUtils.ranks

    selectedRankIndex?.let { rankIndex ->
        RankDetailBottomSheet(
            initialRankIndex = rankIndex,
            userChips = userChips,
            userGamesPlayed = userGamesPlayed,
            onDismissRequest = { selectedRankIndex = null }
        )
    }

    Card(
        shape = RoundedCornerShape(24.dp),
        modifier = modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
//            containerColor = MaterialTheme.colorScheme.inverseOnSurface, // Allow gradient to show
            contentColor = contentColorFor(MaterialTheme.colorScheme.background)
        ),

        ) {
        Box(
            modifier = Modifier
                .background(Brush.horizontalGradient(colorStops = cardColorStops()))
                .animateContentSize()
                .padding(16.dp)
        ) {
            Column {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.your_rank),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                ranks.forEachIndexed { index, rank ->
                    // Determine lock status based on user chips and games played
                    val isUnlocked =
                        userChips >= rank.requiredChips &&
                                userGamesPlayed >= rank.requiredGamesPlayed

                    val prevRank = ranks.getOrNull(index - 1)

                    val isPrevUnlocked = prevRank == null ||
                            (userChips >= prevRank.requiredChips &&
                                    userGamesPlayed >= prevRank.requiredGamesPlayed)

                    RankRow(
                        rank = rank,
                        isLocked = !isUnlocked,
                        isPreviousUnlocked = isPrevUnlocked,
                        userChips = userChips,
                        userGamesPlayed = userGamesPlayed,
                        prevRank = prevRank,
                        onClick = {
                            selectedRankIndex = index
                            onRankClick()
                        }
                    )
                }
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
private fun RankSectionPreview() {
    BlackJackTheme {
        RankSection(userChips = 10000, userGamesPlayed = 100)
    }
}