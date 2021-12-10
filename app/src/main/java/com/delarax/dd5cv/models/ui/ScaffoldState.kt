package com.delarax.dd5cv.models.ui

import com.delarax.dd5cv.models.FormattedResource
import com.delarax.dd5cv.ui.components.ActionItem

data class ScaffoldState(
    val title: FormattedResource = FormattedResource(),
    val actionMenu: List<ActionItem> = listOf(),
    val leftActionItem: ActionItem? = null,
    val floatingActionButtonState: FloatingActionButtonState? = null
)