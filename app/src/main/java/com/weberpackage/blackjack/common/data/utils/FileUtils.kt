package com.weberpackage.blackjack.common.data.utils

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun readJsonFromAssets(
    fileName: String,
    context: Context,
): String = withContext(Dispatchers.IO) {
    context.assets.open(fileName).bufferedReader().use { it.readText() }
}