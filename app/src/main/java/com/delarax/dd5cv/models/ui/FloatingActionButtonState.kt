package com.delarax.dd5cv.models.ui

import androidx.compose.ui.graphics.vector.ImageVector

data class FloatingActionButtonState(
    val icon: ImageVector,
    val contentDescription: FormattedResource,
    val onClick: () -> Unit
)