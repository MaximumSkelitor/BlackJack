package com.weberpackage.blackjack.screens.profile.structure

import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.weberpackage.blackjack.ui.theme.BlackJackTheme
import com.weberpackage.blackjack.R
import com.weberpackage.blackjack.screens.structure.cardColorStops

@Composable
internal fun ProfileDescriptions(
    @StringRes title: Int,
    amount: Int
) {
    val cornerShape = RoundedCornerShape(12.dp)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
            .shadow(
                elevation = 8.dp,
                shape = cornerShape,
                clip = false
            )
            .background(Brush.horizontalGradient(colorStops = cardColorStops()), cornerShape)
            .clip(cornerShape)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Box(
                modifier = Modifier.padding(horizontal = 4.dp)
            ) {
                Text(
                    stringResource(title, amount),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface.copy(.9f),
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }
    }
}



@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
fun ProfileStructurePreview() {
    BlackJackTheme {
        ProfileDescriptions(R.string.total_chips_profile, 1000)
    }
}
