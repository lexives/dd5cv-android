package com.delarax.dd5cv.models.ui

import com.delarax.dd5cv.models.FormattedResource

data class ButtonData(
    val text: FormattedResource = FormattedResource(),
    val onClick: () -> Unit = {}
)