package com.delarax.dd5cv.ui.components.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.delarax.dd5cv.models.ui.LoadingIndicatorState
import com.delarax.dd5cv.ui.components.state.LoadingDots
import com.delarax.dd5cv.ui.theme.Dimens

@Composable
fun LoadingDialog(
    loadingIndicatorState: LoadingIndicatorState
) {
    val dialogSize = 80.dp
    val strokeWidth = 8.dp
    val padding = Dimens.Spacing.xl

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black)
            .alpha(0.2f)
    ) {
        androidx.compose.ui.window.Dialog(
            onDismissRequest = {},
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false
            )
        ) {
            Surface(
                color = MaterialTheme.colors.surface,
                shape = MaterialTheme.shapes.medium
            ) {
                loadingIndicatorState.progress?.let {
                    CircularProgressIndicator(
                        progress = it,
                        strokeWidth = strokeWidth,
                        modifier = Modifier
                            .padding(padding)
                            .size(dialogSize)
                    )
                } ?: CircularProgressIndicator(
                    strokeWidth = strokeWidth,
                    modifier = Modifier
                        .padding(padding)
                        .size(dialogSize)
                )
            }
        }
    }
}
