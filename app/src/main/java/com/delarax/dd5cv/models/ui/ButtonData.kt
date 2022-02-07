package com.delarax.dd5cv.models.ui

import androidx.compose.ui.Modifier

data class ButtonData(
    val text: FormattedResource = FormattedResource(),
    val onClick: () -> Unit = {},
    val modifier: Modifier = Modifier
)