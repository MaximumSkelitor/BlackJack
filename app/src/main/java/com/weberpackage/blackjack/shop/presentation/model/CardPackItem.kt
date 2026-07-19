package com.weberpackage.blackjack.shop.presentation.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Style
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.weberpackage.blackjack.R
import com.weberpackage.blackjack.common.presentation.model.PlayCard
import com.weberpackage.blackjack.common.presentation.model.Rank
import com.weberpackage.blackjack.common.presentation.model.Suit

data class CardPackItem(
    val id: Int,
    val nameResId: Int,
    val price: Int,
    val icon: ImageVector,
    val color: Color,
    val sampleCards: List<PlayCard>
)

val cardPacks = listOf(
    CardPackItem(
        id = 1,
        nameResId = R.string.card_pack_1,
        price = 500,
        icon = Icons.Default.Style,
        color = Color(0xFFA6A6A6), // Green
        sampleCards = listOf(
            PlayCard(Suit.SPADES, Rank.ACE),
            PlayCard(Suit.SPADES, Rank.ACE)
        )
    ),
    CardPackItem(
        id = 2,
        nameResId = R.string.card_pack_2,
        price = 1500,
        icon = Icons.Default.AutoAwesome,
        color = Color(0xFF2196F3), // Blue
        sampleCards = listOf(
            PlayCard(Suit.CLUBS, Rank.ACE),
            PlayCard(Suit.SPADES, Rank.ACE)
        )
    ),
    CardPackItem(
        id = 3,
        nameResId = R.string.card_pack_3,
        price = 5000,
        icon = Icons.Default.Diamond,
        color = Color(0xFF9C27B0), // Purple
        sampleCards = listOf(
            PlayCard(Suit.HEARTS, Rank.JACK),
            PlayCard(Suit.SPADES, Rank.ACE)
        )
    ),
    CardPackItem(
        id = 4,
        nameResId = R.string.card_pack_4,
        price = 15000,
        icon = Icons.Default.Verified,
        color = Color(0xFFFFC107), // Amber
        sampleCards = listOf(
            PlayCard(Suit.DIAMONDS, Rank.ACE),
            PlayCard(Suit.SPADES, Rank.ACE)
        )
    ),
    CardPackItem(
        id = 5,
        nameResId = R.string.card_pack_5,
        price = 50000,
        icon = Icons.Default.WorkspacePremium,
        color = Color(0xFFE91E63), // Pink/Red
        sampleCards = listOf(
            PlayCard(Suit.SPADES, Rank.ACE),
            PlayCard(Suit.SPADES, Rank.ACE)
        )
    )
)