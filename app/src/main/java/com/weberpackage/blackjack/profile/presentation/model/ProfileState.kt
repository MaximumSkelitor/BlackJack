package com.weberpackage.blackjack.profile.presentation.model

data class ProfileState(
    val username: String = "",
    val totalChips: Int = 0,
    val highestChips: Int = 0,
    val gamesPlayed: Long = 0,
    val ownedPacks: List<Int> = emptyList(),
    val equippedPack: Int = 0,
)
