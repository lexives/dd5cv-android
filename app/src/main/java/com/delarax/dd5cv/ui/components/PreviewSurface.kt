package com.delarax.dd5cv.ui.components

import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import com.delarax.dd5cv.ui.theme.Dd5cvTheme

@Composable
fun PreviewSurface(
    content: @Composable () -> Unit
) {
    Dd5cvTheme {
        Surface {
            content()
        }
    }
}