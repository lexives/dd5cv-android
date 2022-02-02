package com.delarax.dd5cv.ui.theme.shapes

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection

class ShieldShape(private val cornerRadius: Dp) : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val pxValue = with (density) { cornerRadius.toPx() }
        return Outline.Generic(
            path = drawShieldPath(size, pxValue)
        )
    }

    private fun drawShieldPath(size: Size, cornerRadius: Float): Path {
        val halfCornerRadius = cornerRadius / 2
        val threeFourthsRadius = cornerRadius * (3f / 4f)

        return Path().apply {
            // Top left indent
            arcTo(
                rect = Rect(
                    left = -threeFourthsRadius,
                    top = -halfCornerRadius,
                    right = threeFourthsRadius,
                    bottom = cornerRadius + halfCornerRadius
                ),
                startAngleDegrees = 90f,
                sweepAngleDegrees = -90f,
                forceMoveTo = false
            )
            // Top arc
            quadraticBezierTo(
                x1 = size.width / 2,
                y1 = -halfCornerRadius,
                x2 = size.width - threeFourthsRadius,
                y2 = halfCornerRadius,
            )
            // Top right indent
            arcTo(
                rect = Rect(
                    left = size.width - threeFourthsRadius,
                    top = -halfCornerRadius,
                    right = size.width + threeFourthsRadius,
                    bottom = cornerRadius + halfCornerRadius
                ),
                startAngleDegrees = 180f,
                sweepAngleDegrees = -90f,
                forceMoveTo = false
            )
            // Right side arc
            quadraticBezierTo(
                x1 = size.width,
                y1 = size.height * .75f,
                x2 = size.width / 2,
                y2 = size.height,
            )
            // Left side arc
            quadraticBezierTo(
                x1 = 0f,
                y1 = size.height * .75f,
                x2 = 0f,
                y2 = cornerRadius + halfCornerRadius,
            )
            // Close the path
            close()
        }
    }
}