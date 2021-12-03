package com.delarax.dd5cv.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.delarax.dd5cv.models.FormattedResource

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