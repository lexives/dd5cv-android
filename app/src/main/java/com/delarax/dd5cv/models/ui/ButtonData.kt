package com.delarax.dd5cv.models.ui

data class ButtonData(
    val text: FormattedResource = FormattedResource(),
    val onClick: () -> Unit = {}
)