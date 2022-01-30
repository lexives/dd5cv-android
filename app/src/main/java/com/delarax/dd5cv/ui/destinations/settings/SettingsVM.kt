package com.delarax.dd5cv.ui.destinations.settings

import androidx.lifecycle.ViewModel
import com.delarax.dd5cv.models.ui.FormattedResource
import com.delarax.dd5cv.models.ui.ScaffoldState
import com.delarax.dd5cv.ui.AppStateActions
import com.delarax.dd5cv.ui.destinations.Destinations
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsVM @Inject constructor(
    private val appStateActions: AppStateActions
) : ViewModel() {

    fun updateScaffoldState() = appStateActions.updateScaffold(
        ScaffoldState(
            title = FormattedResource(Destinations.SETTINGS.titleRes),
        )
    )
}