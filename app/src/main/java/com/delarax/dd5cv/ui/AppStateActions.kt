package com.delarax.dd5cv.ui

import com.delarax.dd5cv.models.ui.AppState
import com.delarax.dd5cv.models.ui.DialogData.CustomDialog
import com.delarax.dd5cv.models.ui.DialogData.MessageDialog
import com.delarax.dd5cv.models.ui.FormattedResource
import com.delarax.dd5cv.models.ui.LoadingIndicatorState
import com.delarax.dd5cv.models.ui.ScaffoldState
import com.delarax.dd5cv.models.ui.ToastData
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppStateActions @Inject constructor() {
    private val _appStateFlow: MutableStateFlow<AppState> = MutableStateFlow(AppState())
    val appStateFlow: StateFlow<AppState> = _appStateFlow

    private val _toastDataFlow: MutableSharedFlow<ToastData?> = MutableSharedFlow()
    val toastDataFlow: SharedFlow<ToastData?> = _toastDataFlow

    fun updateScaffold(newState: ScaffoldState) {
        if (newState != _appStateFlow.value.scaffoldState) {
            _appStateFlow.value = _appStateFlow.value.copy(scaffoldState = newState)
        }
    }

    fun showDialog(messageDialog: MessageDialog) {
        _appStateFlow.value = _appStateFlow.value.copy(dialogState = messageDialog)
    }

    fun showDialog(customDialog: CustomDialog) {
        _appStateFlow.value = _appStateFlow.value.copy(dialogState = customDialog)
    }

    fun hideDialog() {
        _appStateFlow.value = _appStateFlow.value.copy(dialogState = null)
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

    suspend fun showToast(
        message: FormattedResource,
        duration: Int
    ) {
        _toastDataFlow.emit(null)
        _toastDataFlow.emit(ToastData(message = message, duration = duration))
    }
}