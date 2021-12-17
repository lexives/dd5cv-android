package com.delarax.dd5cv.models.ui

data class AppState(
    val scaffoldState: ScaffoldState = ScaffoldState(),
    val dialogState: DialogState? = null,
    val loadingIndicatorState: LoadingIndicatorState? = null
)