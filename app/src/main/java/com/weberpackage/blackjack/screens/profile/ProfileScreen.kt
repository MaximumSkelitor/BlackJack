package com.weberpackage.blackjack.screens.profile

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.weberpackage.blackjack.R
import com.weberpackage.blackjack.screens.shop.sections.cardPacks
import com.weberpackage.blackjack.screens.structure.BlackjackCard
import com.weberpackage.blackjack.screens.structure.cardColorStops
import com.weberpackage.blackjack.screens.structure.formatChips
import com.weberpackage.blackjack.screens.structure.gradientBackground
import com.weberpackage.blackjack.ui.theme.BlackJackTheme

@Composable
fun ProfileScreen(
    totalChips: Int,
    highestChips: Int,
    userUsername: String,
    ownedPacks: List<Int>,
    equippedPackId: Int,
    onEquipPack: (Int) -> Unit,
    contentPadding: PaddingValues = PaddingValues()
) {
    var currentEquipped by remember(equippedPackId) { mutableIntStateOf(equippedPackId) }
    val ownedPackItems = remember(ownedPacks) { cardPacks.filter { ownedPacks.contains(it.id) } }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBackground())
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
                    text = userUsername,
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
                        value = formatChips(totalChips)
                    )
                    StatColumn(
                        label = stringResource(R.string.best),
                        value = formatChips(highestChips)
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
            EquippablePackCard(
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
fun StatColumn(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun EquippablePackCard(
    pack: com.weberpackage.blackjack.screens.shop.sections.CardPackItem,
    isEquipped: Boolean,
    onClick: () -> Unit
) {
    val cornerShape = RoundedCornerShape(20.dp)
    val cardColors = cardColorStops()

    val borderBrush = if (isEquipped) {
        androidx.compose.ui.graphics.SolidColor(MaterialTheme.colorScheme.outline.copy(.8f))
    } else {
        androidx.compose.ui.graphics.SolidColor(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.8f)
            .clip(cornerShape)
            .background(Brush.verticalGradient(colorStops = cardColors))
            .border(
                width = if (isEquipped) 3.dp else 1.dp,
                brush = borderBrush,
                shape = cornerShape
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(pack.nameResId),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.tertiary
            )

            BlackjackCard(
                card = pack.sampleCards.first(),
                packId = pack.id,
                modifier = Modifier.scale(0.8f)
            )

            if (isEquipped) {
                OutlinedButton(
                    onClick = onClick,
                    modifier = Modifier
                        .height(30.dp),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(0.dp),
                    border = BorderStroke(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.outline
                    ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor =
                            MaterialTheme.colorScheme.inverseOnSurface.copy(0.7f)
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = stringResource(R.string.equipped),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            } else {
                OutlinedButton(
                    onClick = onClick,
                    modifier = Modifier
                        .height(30.dp),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(0.dp),
                    border = BorderStroke(
                        width = .4.dp,
                        color = MaterialTheme.colorScheme.outline
                    ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor =
                            MaterialTheme.colorScheme.inverseOnSurface.copy(0.7f)
                    )
                ) {
                    Text(
                        text = stringResource(R.string.tap_equip),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun ProfileScreenPreview() {
    BlackJackTheme {
        ProfileScreen(
            totalChips = 100000,
            highestChips = 250000,
            userUsername = "Player",
            ownedPacks = listOf(1, 2),
            equippedPackId = 1,
            onEquipPack = {}
        )
    }
}
