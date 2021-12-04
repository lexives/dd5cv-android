package com.delarax.dd5cv.models.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import com.delarax.dd5cv.models.FormattedResource

data class FloatingActionButtonState(
    val icon: ImageVector,
    val contentDescription: FormattedResource,
    val onClick: () -> Unit
)