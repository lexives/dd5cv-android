package com.delarax.dd5cv.ui.scaffold

import androidx.compose.material.DrawerState
import androidx.compose.material.DrawerValue
import androidx.compose.material.SnackbarHostState
import androidx.compose.ui.graphics.vector.ImageVector
import com.delarax.dd5cv.models.FormattedResource
import com.delarax.dd5cv.ui.components.ActionItem

data class CustomScaffoldState(
    val title: FormattedResource = FormattedResource(),
    val actionMenu: List<ActionItem> = listOf(),
    val leftActionItem: ActionItem? = null,
    val floatingActionButtonState: FloatingActionButtonState? = null,
    val drawerState: DrawerState = DrawerState(DrawerValue.Closed), // used for ScaffoldState
    val snackbarHostState: SnackbarHostState = SnackbarHostState() // used for ScaffoldState
)

data class FloatingActionButtonState(
    val icon: ImageVector,
    val contentDescription: FormattedResource,
    val onClick: () -> Unit
)