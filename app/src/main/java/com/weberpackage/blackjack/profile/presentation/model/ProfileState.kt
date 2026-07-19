package com.weberpackage.blackjack.profile.presentation.model

data class ProfileState(
    val username: String,
    val totalChips: Int,
    val highestChips: Int,
    val ownedPacks: List<Int>,
    val equippedPack: Int,
)
