package com.delarax.dd5cv.ui.destinations.characters.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.delarax.dd5cv.R
import com.delarax.dd5cv.data.characters.remote.RemoteCharacterDataSourceMocked.Companion.DEFAULT_CHARACTERS
import com.delarax.dd5cv.models.characters.Character
import com.delarax.dd5cv.models.data.State
import com.delarax.dd5cv.ui.components.PreviewSurface
import com.delarax.dd5cv.ui.components.layout.HorizontalSpacer
import com.delarax.dd5cv.ui.components.text.EditableText
import com.delarax.dd5cv.ui.destinations.characters.screens.shared.CharacterClasses
import com.delarax.dd5cv.ui.theme.Dimens
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.FlowPreview

@Composable
fun CharacterDescriptionTab(
    characterState: State<Character>,
    inEditMode: Boolean,
    onNameChanged: (String) -> Unit
) {
    val character = characterState.getOrNull()
    character?.toSummary()?.let {
        Text(
            text = stringResource(R.string.character_name_label),
            style = MaterialTheme.typography.overline
        )
        EditableText(
            text = it.name ?: stringResource(R.string.default_character_name),
            onTextChanged = onNameChanged,
            inEditMode = inEditMode,
            textStyle = TextStyle(
                fontSize = Dimens.FontSize.xl,
                color = MaterialTheme.colors.onSurface
            )
        )

        HorizontalSpacer.Small()

        Text(
            text = stringResource(R.string.character_classes_label),
            style = MaterialTheme.typography.overline
        )
        CharacterClasses(classes = it.classes)
    }
}

/****************************************** Previews **********************************************/

@FlowPreview
@ExperimentalPagerApi
@ExperimentalMaterialApi
@Preview
//@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CharacterDetailsScreenPreview() {
    PreviewSurface {
        Column(modifier = Modifier.padding(Dimens.Spacing.md)) {
            CharacterDescriptionTab(
                characterState = State.Success(DEFAULT_CHARACTERS[0]),
                inEditMode = false,
                onNameChanged = {}
            )
        }
    }
}

@FlowPreview
@ExperimentalPagerApi
@ExperimentalMaterialApi
@Preview
//@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CharacterDetailsScreenEditModePreview() {
    PreviewSurface {
        Column(modifier = Modifier.padding(Dimens.Spacing.md)) {
            CharacterDescriptionTab(
                characterState = State.Success(DEFAULT_CHARACTERS[0]),
                inEditMode = true,
                onNameChanged = {}
            )
        }
    }
}