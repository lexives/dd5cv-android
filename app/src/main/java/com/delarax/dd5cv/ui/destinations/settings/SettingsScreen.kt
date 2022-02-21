package com.delarax.dd5cv.ui.destinations.settings

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.delarax.dd5cv.R
import com.delarax.dd5cv.extensions.enumCaseToTitleCase
import com.delarax.dd5cv.models.preferences.DarkThemePreference
import com.delarax.dd5cv.ui.components.PreviewSurface
import com.delarax.dd5cv.ui.components.layout.VerticalSpacer
import com.delarax.dd5cv.ui.theme.Dimens
import com.delarax.dd5cv.ui.theme.ThemeVM
import com.google.accompanist.flowlayout.FlowRow

@Composable
fun SettingsScreen() {
    val settingsVM: SettingsVM = hiltViewModel()
    settingsVM.updateScaffoldState()

    val themeVM: ThemeVM = hiltViewModel()

    SettingsScreenContent(
        themeViewState = themeVM.viewState,
        onSelectDarkThemePreference = themeVM::setNightModePreference
    )
}

@Composable
fun SettingsScreenContent(
    themeViewState: ThemeVM.ViewState,
    onSelectDarkThemePreference: (DarkThemePreference) -> Unit
)  {
    Column(
        modifier = Modifier
            .padding(Dimens.Spacing.md)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = stringResource(R.string.night_mode_setting),
            style = MaterialTheme.typography.h6
        )
        FlowRow(
            mainAxisSpacing = Dimens.Spacing.md,
            crossAxisSpacing = Dimens.Spacing.sm,
            modifier = Modifier.padding(vertical = Dimens.Spacing.md)
        ) {
            themeViewState.darkThemePreferences.forEach {
                Row(
                    modifier = Modifier.clickable { onSelectDarkThemePreference(it) }
                ) {
                    RadioButton(
                        selected = (it == themeViewState.currentDarkThemePreference),
                        onClick = { onSelectDarkThemePreference(it) },
                        modifier = Modifier.scale(1.25f)
                    )
                    VerticalSpacer.Small()
                    Text(
                        text = it.name.enumCaseToTitleCase(),
                        modifier = Modifier.padding(end = Dimens.Spacing.md)
                    )
                }
            }
        }
        Divider(modifier = Modifier.padding(vertical = Dimens.Spacing.sm))
    }
}

/****************************************** Previews **********************************************/

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SettingsScreenPreview() {
    PreviewSurface {
        SettingsScreenContent(
            themeViewState = ThemeVM.ViewState(),
            onSelectDarkThemePreference = {}
        )
    }
}