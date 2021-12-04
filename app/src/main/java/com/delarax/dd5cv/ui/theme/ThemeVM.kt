package com.delarax.dd5cv.ui.theme

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.delarax.dd5cv.data.preferences.PreferencesRepo
import com.delarax.dd5cv.models.preferences.DarkThemePreference
import com.delarax.dd5cv.models.preferences.DarkThemePreference.MATCH_SYSTEM
import com.delarax.dd5cv.models.preferences.DarkThemePreference.OFF
import com.delarax.dd5cv.models.preferences.DarkThemePreference.ON
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThemeVM @Inject constructor(
    private val preferencesRepo: PreferencesRepo
) : ViewModel() {

    var viewState by mutableStateOf(ViewState())
    private set

    data class ViewState(
        val currentDarkThemePreference: DarkThemePreference = MATCH_SYSTEM
    ) {
        val darkThemePreferences: List<DarkThemePreference> = listOf(ON, OFF, MATCH_SYSTEM)
    }

    init {
        viewModelScope.launch {
            preferencesRepo.darkThemeFlow.collect {
                applyNightModePreference(it)
            }
        }
    }

    fun setNightModePreference(preference: DarkThemePreference) {
        viewModelScope.launch {
            preferencesRepo.setDarkThemePreference(preference)
        }
    }

    private fun applyNightModePreference(preference: DarkThemePreference) {
        val mode = when(preference) {
            OFF -> AppCompatDelegate.MODE_NIGHT_NO
            ON -> AppCompatDelegate.MODE_NIGHT_YES
            else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }
        AppCompatDelegate.setDefaultNightMode(mode)
        viewState = viewState.copy(currentDarkThemePreference = preference)
    }
}