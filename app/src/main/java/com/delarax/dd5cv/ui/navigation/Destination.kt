package com.delarax.dd5cv.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.People
import androidx.compose.ui.graphics.vector.ImageVector
import com.delarax.dd5cv.R

enum class Destination(
    val route: String,
    @StringRes val titleRes: Int,
    val icon: ImageVector? = null,
) {
    CHARACTERS(
        route = "characters",
        titleRes = R.string.destination_characters_title,
        icon = Icons.Default.People
    )
}