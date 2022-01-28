package com.delarax.dd5cv.ui

import com.delarax.dd5cv.models.FormattedResource
import com.delarax.dd5cv.models.ui.AppState
import com.delarax.dd5cv.models.ui.ButtonData
import com.delarax.dd5cv.models.ui.DialogState
import com.delarax.dd5cv.models.ui.LoadingIndicatorState
import com.delarax.dd5cv.models.ui.ScaffoldState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppStateActions @Inject constructor() {
    private val _appStateFlow: MutableStateFlow<AppState> = MutableStateFlow(AppState())
    val appStateFlow: StateFlow<AppState> = _appStateFlow

    fun updateScaffold(newState: ScaffoldState) {
        if (newState != _appStateFlow.value.scaffoldState) {
            _appStateFlow.value = _appStateFlow.value.copy(scaffoldState = newState)
        }
    }

    fun showDialog(
        title: FormattedResource = FormattedResource(),
        message: FormattedResource = FormattedResource(),
        mainAction: ButtonData? = null,
        secondaryAction: ButtonData? = null,
        onDismissRequest: () -> Unit = {}
    ) {
        _appStateFlow.value = _appStateFlow.value.copy(
            dialogState = DialogState(
                title = title,
                message = message,
                mainAction = mainAction,
                secondaryAction = secondaryAction,
                onDismissRequest = onDismissRequest
            )
        )
    }

    fun hideDialog() {
        _appStateFlow.value = _appStateFlow.value.copy(
            dialogState = null
        )
    }

    fun showLoadingIndicator(progress: Float? = null) {
        _appStateFlow.value = _appStateFlow.value.copy(
            loadingIndicatorState = LoadingIndicatorState(progress)
        )
    }

    fun hideLoadingIndicator() {
        _appStateFlow.value = _appStateFlow.value.copy(
            loadingIndicatorState = null
        )
    }
}