package com.delarax.dd5cv.models.ui

import com.delarax.dd5cv.ui.components.toppappbar.ActionItem

data class ScaffoldState(
    val title: FormattedResource = FormattedResource(),
    val subtitle: FormattedResource? = null,
    val actionMenu: List<ActionItem> = listOf(),
    val floatingActionButtonState: FloatingActionButtonState? = null,
    val onBackPressed: (() -> Unit)? = null
)