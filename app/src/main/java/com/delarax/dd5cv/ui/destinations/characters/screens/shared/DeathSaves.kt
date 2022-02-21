package com.delarax.dd5cv.ui.destinations.characters.screens.shared

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.delarax.dd5cv.R
import com.delarax.dd5cv.models.characters.DeathSave
import com.delarax.dd5cv.ui.components.PreviewSurface
import com.delarax.dd5cv.ui.theme.Dimens
import com.delarax.dd5cv.ui.theme.Green500
import com.delarax.dd5cv.ui.theme.Red700
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.FlowPreview

@Composable
fun DeathSaves(
    failures: DeathSave,
    successes: DeathSave,
    onSuccessesChanged: (DeathSave) -> Unit,
    onFailuresChanged: (DeathSave) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(horizontalArrangement = Arrangement.spacedBy(Dimens.Spacing.sm)) {
                DeathSaveRadioButton(
                    selected = failures.third,
                    onClick = {
                        onFailuresChanged(
                            if (failures.third) DeathSave.Second else DeathSave.Third
                        )
                    },
                    isSuccess = false
                )
                DeathSaveRadioButton(
                    selected = failures.second,
                    onClick = {
                        onFailuresChanged(
                            if (failures.second && !failures.third) DeathSave.First
                            else DeathSave.Second
                        )
                    },
                    isSuccess = false
                )
                DeathSaveRadioButton(
                    selected = failures.first,
                    onClick = {
                        onFailuresChanged(
                            if (failures.first && !failures.second) DeathSave.None
                            else DeathSave.First
                        )
                    },
                    isSuccess = false
                )
            }
            Text(
                text = stringResource(R.string.death_save_failures_label),
                style = MaterialTheme.typography.subtitle2
            )
        }
        Text(
            text = stringResource(R.string.death_saves_label),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(horizontal = Dimens.Spacing.md)
                .width(IntrinsicSize.Min)
        )
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(horizontalArrangement = Arrangement.spacedBy(Dimens.Spacing.sm)) {
                DeathSaveRadioButton(
                    selected = successes.first,
                    onClick = {
                        onSuccessesChanged(
                            if (successes.first && !successes.second) DeathSave.None
                            else DeathSave.First
                        )
                    },
                    isSuccess = true
                )
                DeathSaveRadioButton(
                    selected = successes.second,
                    onClick = {
                        onSuccessesChanged(
                            if (successes.second && !successes.third) DeathSave.First
                            else DeathSave.Second
                        )
                    },
                    isSuccess = true
                )
                DeathSaveRadioButton(
                    selected = successes.third,
                    onClick = {
                        onSuccessesChanged(
                            if (successes.third) DeathSave.Second else DeathSave.Third
                        )
                    },
                    isSuccess = true
                )
            }
            Text(
                text = stringResource(R.string.death_save_successes_label),
                style = MaterialTheme.typography.subtitle2
            )
        }
    }
}

@Composable
private fun DeathSaveRadioButton(
    selected: Boolean,
    onClick: () -> Unit,
    isSuccess: Boolean, // if false, assume it's a failure radio button
    enabled: Boolean = true
) {
    val selectedColor = if (isSuccess) Green500 else Red700
    RadioButton(
        selected = selected,
        onClick = onClick,
        enabled = enabled,
        colors = RadioButtonDefaults.colors(selectedColor = selectedColor),
        modifier = Modifier
            .scale(1.33f)
            .padding(Dimens.Spacing.sm)
    )
}

/****************************************** Previews **********************************************/

@FlowPreview
@ExperimentalPagerApi
@ExperimentalMaterialApi
@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CharacterDetailsScreenPreview() {
    val (deathSaveSuccesses, setDeathSaveSuccesses) = remember { mutableStateOf(DeathSave.Third)}
    val (deathSaveFailures, setDeathSaveFailures) = remember { mutableStateOf(DeathSave.Third)}

    PreviewSurface {
        DeathSaves(
            failures = deathSaveFailures,
            successes = deathSaveSuccesses,
            onSuccessesChanged = setDeathSaveSuccesses,
            onFailuresChanged = setDeathSaveFailures
        )
    }
}
