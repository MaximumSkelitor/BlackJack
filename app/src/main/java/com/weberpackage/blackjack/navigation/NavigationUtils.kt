package com.weberpackage.blackjack.navigation

fun String?.getNavigationItem() : NavigationItem? {
    return NavigationItem.entries.firstOrNull {this == it.name}
}