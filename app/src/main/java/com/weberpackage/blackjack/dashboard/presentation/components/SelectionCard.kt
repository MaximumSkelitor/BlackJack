package com.weberpackage.blackjack.dashboard.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.weberpackage.blackjack.common.presentation.base.cardColorStops
import com.weberpackage.blackjack.dashboard.presentation.model.SelectionItem


@Composable
internal fun SelectionCard(
    item: SelectionItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cornerShape = RoundedCornerShape(24.dp)
    Box(
        modifier = modifier
            .size(width = 300.dp, height = 225.dp)
            .shadow(
                elevation = 20.dp,
                shape = cornerShape,
                spotColor = item.tint,
                clip = false
            )
            .background(
                brush = Brush.horizontalGradient(
                    colorStops = cardColorStops()
                ),
                shape = cornerShape
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                modifier = Modifier.size(80.dp),
                imageVector = item.image,
                contentDescription = null,
                tint = item.tint
            )
            Column {
                Text(
                    text = stringResource(item.titleResId),
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 28.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(item.subtitleResId),
                    color = MaterialTheme.colorScheme.onSurface.copy(0.4f),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 22.sp
                )
            }
        }
    }
}
