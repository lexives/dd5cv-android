package com.delarax.dd5cv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.hilt.navigation.compose.hiltViewModel
import com.delarax.dd5cv.models.preferences.DarkThemePreference
import com.delarax.dd5cv.ui.components.LocalBackPressedDispatcher
import com.delarax.dd5cv.ui.scaffold.Dd5cvScaffold
import com.delarax.dd5cv.ui.theme.Dd5cvTheme
import com.delarax.dd5cv.ui.theme.ThemeVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Dd5cvContent {
                // Provide the onBackPressedDispatcher to child composables
                CompositionLocalProvider(LocalBackPressedDispatcher provides this.onBackPressedDispatcher) {
                    Dd5cvScaffold()
                }
            }
        }
    }
}

@Composable
fun Dd5cvContent(content: @Composable () -> Unit) {
    val themeVM: ThemeVM = hiltViewModel()
    val darkTheme: Boolean = when (themeVM.viewState.currentDarkThemePreference) {
        DarkThemePreference.OFF -> false
        DarkThemePreference.ON -> true
        DarkThemePreference.MATCH_SYSTEM -> isSystemInDarkTheme()
    }
    Dd5cvTheme(darkTheme = darkTheme) {
        content()
    }
}