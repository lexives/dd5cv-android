package com.delarax.dd5cv.ui.scaffold

import android.util.Log
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
        val actionMenu: List<ActionItem> = listOf(),
        val leftActionItem: ActionItem? = null,
        val floatingActionButton: FloatingActionButton? = null
    )

    data class FloatingActionButton(
        val icon: ImageVector,
        val contentDescription: FormattedResource,
        val onClick: () -> Unit
    )

    fun setScaffold(newViwState: ViewState) {
        viewState = newViwState
    }
}