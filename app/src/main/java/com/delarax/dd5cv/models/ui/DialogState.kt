package com.delarax.dd5cv.models.ui

data class DialogState(
    val title: FormattedResource = FormattedResource(),
    val message: FormattedResource = FormattedResource(),
    val mainAction: ButtonData? = null,
    val secondaryAction: ButtonData? = null,
    // The action that runs when the user presses the back button or anywhere outside of the dialog
    val onDismissRequest: () -> Unit = {}
)
