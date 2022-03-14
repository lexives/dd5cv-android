package com.delarax.dd5cv.ui.theme.shapes

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

enum class ArcSide { TOP, BOTTOM, START, END }

class ArcShape(
    private val arcSide: ArcSide
) : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Generic(
            path = drawArcPath(size, arcSide)
        )
    }

    private fun drawArcPath(size: Size, arcSide: ArcSide): Path {
        val rect = Rect(
            left = 0f,
            top = 0f,
            right = size.width,
            bottom = size.height
        )

        return Path().apply {
            when (arcSide) {
                ArcSide.TOP -> {
                    arcTo(
                        rect = rect,
                        startAngleDegrees = 0f,
                        sweepAngleDegrees = -180f,
                        forceMoveTo = false
                    )
                }
                ArcSide.BOTTOM -> {
                    arcTo(
                        rect = rect,
                        startAngleDegrees = 0f,
                        sweepAngleDegrees = 180f,
                        forceMoveTo = false
                    )
                }
                ArcSide.START -> {
                    arcTo(
                        rect = rect,
                        startAngleDegrees = 90f,
                        sweepAngleDegrees = 180f,
                        forceMoveTo = false
                    )
                }
                ArcSide.END -> {
                    arcTo(
                        rect = rect,
                        startAngleDegrees = -90f,
                        sweepAngleDegrees = 180f,
                        forceMoveTo = false
                    )
                }
            }
        }
    }
}