package com.weberpackage.blackjack.changelog.presentation.component

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.weberpackage.blackjack.changelog.presentation.model.ChangelogData.Log
import com.weberpackage.blackjack.common.presentation.theme.BlackJackTheme
import com.weberpackage.blackjack.common.presentation.theme.spacing

@Composable
internal fun LogItem(
    log: Log,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(MaterialTheme.spacing.smallOne)
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Image(
                modifier = modifier
                    .size(
                        width = 40.dp,
                        height = 20.dp
                    )
                    .padding(
                        top = MaterialTheme.spacing.extraExtraSmall,
                        end = MaterialTheme.spacing.extraSmallOne
                    ),
                painter = painterResource(log.icon),
                contentDescription = null
            )
            Text(
                text = log.text,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = modifier.padding(start = MaterialTheme.spacing.extraSmall)
            )
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
private fun LogItemPreview() {
    val log by remember {
        mutableStateOf(
            Log(
                type = "new",
                text = "Added new feature!"
            )
        )
    }
    BlackJackTheme {
        Surface {
            LogItem(
                log = log
            )
        }
    }
}