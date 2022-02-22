package com.delarax.dd5cv.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.res.stringResource
import com.delarax.dd5cv.models.ui.FormattedResource

@Composable
fun FormattedResource.resolve(): String {
    return if (values.isNotEmpty()) {
        stringResource(id = resId, *values.toTypedArray())
    } else {
        stringResource(id = resId)
    }
}

@Composable
fun FormattedResource?.resolveOrDefault(default: String): String {
    return this?.let {
        if (values.isNotEmpty()) {
            stringResource(id = resId, *values.toTypedArray())
        } else {
            stringResource(id = resId)
        }
    } ?: default
}

@Composable
fun FormattedResource?.resolveOrEmpty(): String {
    return this.resolveOrDefault(default = "")
}

fun Modifier.drawWithoutRect(rect: Rect?) =
    drawWithContent {
        if (rect != null) {
            clipRect(
                left = rect.left,
                top = rect.top,
                right = rect.right,
                bottom = rect.bottom,
                clipOp = ClipOp.Difference,
            ) {
                this@drawWithContent.drawContent()
            }
        } else {
            drawContent()
        }
    }