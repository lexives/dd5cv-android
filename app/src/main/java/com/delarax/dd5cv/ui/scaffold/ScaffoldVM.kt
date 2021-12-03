package com.delarax.dd5cv.ui.scaffold

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import com.delarax.dd5cv.models.FormattedResource
import com.delarax.dd5cv.ui.components.ActionItem

class ScaffoldVM : ViewModel() {

    var viewState by mutableStateOf(ViewState())
        private set

    data class ViewState(
        val title: FormattedResource = FormattedResource(),
        val actionItems: List<ActionItem> = listOf(),
        val floatingActionButton: FloatingActionButton? = null
    )

    data class FloatingActionButton(
        val icon: ImageVector,
        val contentDescription: FormattedResource,
        val onClick: () -> Unit
    )

    fun setTitle(title: FormattedResource) {
        viewState = viewState.copy(title = title)
    }

    fun setActionItems(actionItems: List<ActionItem>) {
        viewState = viewState.copy(actionItems = actionItems)
    }

    fun setFloatingActionButton(floatingActionButton: FloatingActionButton?) {
        viewState = viewState.copy(floatingActionButton = floatingActionButton)
    }
}