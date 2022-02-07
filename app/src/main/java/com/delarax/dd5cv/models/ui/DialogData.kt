package com.delarax.dd5cv.models.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable

sealed class DialogData {
    abstract val title: FormattedResource?
    // The action that runs when the user presses the back button or anywhere outside of the dialog
    abstract val onDismissRequest: () -> Unit

    data class MessageDialog(
        override val title: FormattedResource = FormattedResource(),
        val message: FormattedResource = FormattedResource(),
        val mainAction: ButtonData? = null,
        val secondaryAction: ButtonData? = null,
        val buttonAlignment: Arrangement.Horizontal = Arrangement.End,
        override val onDismissRequest: () -> Unit = {}
    ) : DialogData()

    data class CustomDialog(
        override val title: FormattedResource? = null,
        val content: @Composable () -> Unit,
        override val onDismissRequest: () -> Unit = {}
    ) : DialogData()
}