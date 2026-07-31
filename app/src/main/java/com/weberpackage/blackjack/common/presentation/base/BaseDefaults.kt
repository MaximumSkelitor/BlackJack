package com.weberpackage.blackjack.common.presentation.base

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import java.text.NumberFormat
import java.util.Locale

fun getPagerAnimationSpec(): FiniteAnimationSpec<Float> {
    return tween(300)
}

// Formats a number with commas for better readability (e.g., 100,000).
fun formatChips(amount: Int): String {
    return NumberFormat.getNumberInstance(Locale.US).format(amount)
}

fun formatChipsLong(amount: Long): String {
    return NumberFormat.getNumberInstance(Locale.US).format(amount)
}

fun formatChipsCompact(amount: Int): String {
    return when {
        amount >= 1_000_000 -> {
            val millions = amount / 1_000_000.0
            if (millions % 1 == 0.0) String.format(Locale.US, "%.0fM", millions)
            else String.format(Locale.US, "%.1fM", millions)
        }
        amount >= 1_000 -> {
            val thousands = amount / 1_000.0
            if (thousands % 1 == 0.0) String.format(Locale.US, "%.0fk", thousands)
            else String.format(Locale.US, "%.1fk", thousands)
        }
        else -> amount.toString()
    }
}

@Composable
fun bigCardColorStops(): Array<Pair<Float, Color>> {
    val surface = MaterialTheme.colorScheme.surfaceContainer
    val primary = MaterialTheme.colorScheme.surfaceContainerLow

    return arrayOf(
        0.0f to surface,
        0.5f to primary.copy(.5f),
        1.0f to surface
    )
}

//@Composable
//fun glowBackground(): Brush {
//    return Brush.radialGradient(
//        colors = listOf(
//            Color(0xFF411313),
//            Color(0xFF191F2D),
//            Color(0xFF11151F)
//        ),
//        center = Offset(x = 1600f, y = -100f),
//        radius = 2000f
//    )
//}

@Composable
fun glowBackground(): Brush {
    return Brush.radialGradient(
        colors = listOf(
            MaterialTheme.colorScheme.onTertiary,
            MaterialTheme.colorScheme.onSecondary,
            MaterialTheme.colorScheme.background
        ),
        center = Offset(x = 1600f, y = -100f),
        radius = 2000f
    )
}

@Composable
fun cardColorStops(): Array<Pair<Float, Color>> {
    return arrayOf(
        0.0f to MaterialTheme.colorScheme.surfaceContainer,
        1f to MaterialTheme.colorScheme.surfaceContainerLow
    )
}

@Composable
fun cardColorStopsLight(): Array<Pair<Float, Color>> {
    return arrayOf(
        0.0f to MaterialTheme.colorScheme.surfaceContainer.copy(.7f),
        1f to MaterialTheme.colorScheme.surfaceContainerLow.copy(.7f)
    )
}

@OptIn(ExperimentalHazeMaterialsApi::class)
@Composable
fun hazeAppBarStyle(
    color: Color = MaterialTheme.colorScheme.background,
    noiseFactor: Float = 0f
): HazeStyle {
    return HazeMaterials.ultraThin(color).copy(noiseFactor = noiseFactor)
}

@Composable
fun gradientBackground(): Brush {
    return Brush.verticalGradient(
        0f to MaterialTheme.colorScheme.background,
        1000f to MaterialTheme.colorScheme.surfaceContainerLow
    )
}


@Composable
fun cardColorStops2(): Array<Pair<Float, Color>> {
    return arrayOf(
        0f to MaterialTheme.colorScheme.surfaceContainer.copy(0.4f),
        1f to MaterialTheme.colorScheme.surfaceContainer
    )
}

@Composable
fun radialGradientBackground(): Brush {
    return Brush.radialGradient(
        0f to MaterialTheme.colorScheme.surfaceContainer,
        3000f to MaterialTheme.colorScheme.background,
        radius = 800f
    )
}
