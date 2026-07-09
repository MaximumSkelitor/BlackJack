package com.weberpackage.blackjack.screens.shop

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.weberpackage.blackjack.R
import com.weberpackage.blackjack.coredata.PreferenceManager
import com.weberpackage.blackjack.screens.shop.sections.CardPackSection
import com.weberpackage.blackjack.screens.shop.sections.ShopSelectionRowContainer
import com.weberpackage.blackjack.screens.structure.gradientBackground
import com.weberpackage.blackjack.ui.theme.BlackJackTheme

@Composable
fun ShopScreen(
    contentPadding: PaddingValues = PaddingValues(),
    totalChips: Int,
    onUpdateChips: (Int) -> Unit,
) {
    val context = LocalContext.current
    val preferenceManager = remember { PreferenceManager(context) }
    val ownedPacks = remember { mutableStateListOf<Int>().apply { addAll(preferenceManager.getOwnedPacks()) } }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
            .background(gradientBackground())
            .padding(top = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Daily Credits Section
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.padding(top = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.daily_credits),
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.ExtraBold
                    )
                }

                ShopSelectionRowContainer(
                    onClaimed = {
                        onUpdateChips(preferenceManager.getChips() - totalChips)
                    }
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 40.dp),
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
            )

            // Card Packs Section
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = stringResource(R.string.card_packs),
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.ExtraBold
                    )
                }

                CardPackSection(
                    currentChips = totalChips,
                    ownedPacks = ownedPacks,
                    onPurchase = { pack ->
                        if (totalChips >= pack.price && !ownedPacks.contains(pack.id)) {
                            onUpdateChips(-pack.price)
                            preferenceManager.addOwnedPack(pack.id)
                            ownedPacks.add(pack.id)
                        }
                    }
                )
            }

            Spacer(Modifier.height(20.dp))
        }
    }
}


@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
private fun ShopScreenPreview() {
    BlackJackTheme {
        ShopScreen(totalChips = 1000, onUpdateChips = {})
    }
}
