package com.delarax.dd5cv.ui.destinations

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.delarax.dd5cv.R

enum class Destinations(
    val route: String,
    @StringRes val titleRes: Int,
    val icon: ImageVector? = null,
) {
    CHARACTERS(
        route = "characters",
        titleRes = R.string.destination_characters_title,
        icon = Icons.Default.People
    ),
    SETTINGS(
        route = "settings",
        titleRes = R.string.destination_settings_title,
        icon = Icons.Default.Settings
    )
}