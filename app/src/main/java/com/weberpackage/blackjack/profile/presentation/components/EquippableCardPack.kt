package com.weberpackage.blackjack.profile.presentation.components

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
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.weberpackage.blackjack.R
import com.weberpackage.blackjack.common.presentation.base.cardColorStops
import com.weberpackage.blackjack.common.presentation.components.BlackjackCard
import com.weberpackage.blackjack.common.presentation.components.DownBlackJackCard
import com.weberpackage.blackjack.common.presentation.theme.BlackJackTheme
import com.weberpackage.blackjack.shop.presentation.model.CardPackItem
import com.weberpackage.blackjack.shop.presentation.model.cardPacks


@Composable
fun EquippableCardPack(
    pack: CardPackItem,
    isEquipped: Boolean,
    onClick: () -> Unit
) {
    val cornerShape = RoundedCornerShape(20.dp)

    val borderBrush = if (isEquipped) {
        SolidColor(MaterialTheme.colorScheme.outline.copy(.8f))
    } else {
        SolidColor(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
    }

    Box(
        modifier = Modifier
            .shadow(
                elevation = 20.dp,
                shape = cornerShape,
                spotColor = if (isEquipped) {
                    pack.color
                } else {
                    pack.color.copy(.4f)
                },
                clip = false
            )
            .fillMaxWidth()
            .aspectRatio(0.8f)
            .clip(cornerShape)
            .background(Brush.verticalGradient(colorStops = cardColorStops()))
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
                color = MaterialTheme.colorScheme.onSurface
            )
            Box(
                modifier = Modifier
                    .height(145.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                pack.sampleCards.forEachIndexed { index, card ->
                    val rotation = if (index == 0) -12f else 12f
                    val xOffset = if (index == 0) (-25).dp else 25.dp

                    DownBlackJackCard(
                        packId = pack.id,
                        modifier = Modifier
                            .graphicsLayer {
                                rotationZ = rotation
                                translationX = xOffset.toPx()
                                alpha = 1f
                                scaleX = 0.7f
                                scaleY = 0.7f
                            }
                            .zIndex(1f)
                    )
                    BlackjackCard(
                        card = card,
                        packId = pack.id,
                        modifier = Modifier
                            .graphicsLayer {
                                rotationZ = rotation
                                translationX = xOffset.toPx()
                                alpha = 1f
                                scaleX = 0.7f
                                scaleY = 0.7f
                            }
                            .zIndex(if (index == 0) 0f else 1f)
                    )
                }
            }

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
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
fun EquippableCardPackPreview() {
    BlackJackTheme {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(cardPacks) { pack ->
                EquippableCardPack(
                    pack = pack,
                    isEquipped = pack.id == 1,
                    onClick = { }
                )
            }
        }
    }
}
