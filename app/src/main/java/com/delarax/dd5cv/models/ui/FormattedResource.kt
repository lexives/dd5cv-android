package com.delarax.dd5cv.models.ui

import androidx.annotation.StringRes
import com.delarax.dd5cv.R

data class FormattedResource(
    @StringRes val resId: Int = R.string.empty,
    val values: List<Any> = listOf()
) {
    constructor(string: String) : this(R.string.single_arg, listOf(string))
    constructor(resId: Int, value: String) : this(resId, listOf(value))
}
