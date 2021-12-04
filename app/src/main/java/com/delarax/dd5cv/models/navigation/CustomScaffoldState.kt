package com.delarax.dd5cv.models.navigation

import com.delarax.dd5cv.models.FormattedResource
import com.delarax.dd5cv.ui.components.ActionItem

data class CustomScaffoldState(
    val title: FormattedResource = FormattedResource(),
    val actionMenu: List<ActionItem> = listOf(),
    val leftActionItem: ActionItem? = null,
    val floatingActionButtonState: FloatingActionButtonState? = null
)