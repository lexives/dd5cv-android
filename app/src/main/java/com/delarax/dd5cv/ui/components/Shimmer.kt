package com.delarax.dd5cv.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * *** WIP ***
 * Shimmer animation from top left corner to bottom right corner.
 */
@Composable
fun ShimmerLinearGradient(
    colors: List<Color>,
    height: Dp,
    startOffset: Float,
    endOffset: Float
) {
    val brush = Brush.linearGradient(
        colors,
        start = Offset(startOffset, startOffset),
        end = Offset(endOffset, endOffset)
    )
    Surface(shape = MaterialTheme.shapes.small) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .background(brush = brush)
        )
    }
}

/****************************************** Previews **********************************************/

@Preview
//@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES) TODO: night mode
@Composable
private fun ShimmerLinearGradientPreview() {
    ShimmerLinearGradient(
        colors = listOf(
            Color.LightGray.copy(alpha = 0.9f),
            Color.LightGray.copy(alpha = 0.2f),
            Color.LightGray.copy(alpha = 0.9f)
        ),
        height = 400.dp,
        startOffset = 200f,
        endOffset = 400f
    )
}