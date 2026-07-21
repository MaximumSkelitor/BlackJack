package com.weberpackage.blackjack.common.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.weberpackage.blackjack.R
import com.weberpackage.blackjack.common.presentation.base.formatChips


@Composable
fun ChipCounter(
    count: Int,
    modifier: Modifier = Modifier,
    fontSize: TextUnit,
    showText: Boolean = false,
    showChipIcon: Boolean = false
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        if (showChipIcon) {
            Icon(
                painter = painterResource(R.drawable.ic_poker_chip),
                contentDescription = "Chip Icon",
                modifier = Modifier.size(16.dp)
            )
            Spacer(Modifier.width(4.dp))
        }
        formatChips(count).forEach { char ->
            AnimatedContent(
                targetState = char,
                transitionSpec = {
                    if (targetState > initialState) {
                        slideInVertically { it } + fadeIn() togetherWith slideOutVertically { -it } + fadeOut()
                    } else {
                        slideInVertically { -it } + fadeIn() togetherWith slideOutVertically { it } + fadeOut()
                    }.using(SizeTransform(clip = false))
                },
                label = "DigitAnimation"
            ) { targetDigit ->
                Text(
                    text = targetDigit.toString(),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = fontSize
                )
            }
        }
        if (showText) {
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                text = stringResource(R.string.credits),
                fontWeight = FontWeight.SemiBold,
                fontSize = fontSize,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}