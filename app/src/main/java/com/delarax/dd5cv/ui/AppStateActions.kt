package com.delarax.dd5cv.ui

import com.delarax.dd5cv.models.ui.AppState
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
}