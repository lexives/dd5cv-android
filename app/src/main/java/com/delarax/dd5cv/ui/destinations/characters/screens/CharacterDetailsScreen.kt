package com.delarax.dd5cv.ui.destinations.characters.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.delarax.dd5cv.R
import com.delarax.dd5cv.data.characters.remote.RemoteCharacterDataSourceMocked.Companion.DEFAULT_CHARACTERS
import com.delarax.dd5cv.models.ui.FormattedResource
import com.delarax.dd5cv.models.data.State
import com.delarax.dd5cv.models.characters.Character
import com.delarax.dd5cv.ui.components.HorizontalSpacer
import com.delarax.dd5cv.ui.components.PreviewSurface
import com.delarax.dd5cv.ui.components.resolve
import com.delarax.dd5cv.ui.components.text.EditableText
import com.delarax.dd5cv.ui.destinations.characters.screens.shared.CharacterClasses
import com.delarax.dd5cv.ui.destinations.characters.viewmodels.CharacterDetailsVM
import com.delarax.dd5cv.ui.theme.Dimens
import kotlinx.coroutines.FlowPreview

@FlowPreview
@Composable
fun CharacterDetailsScreen(
    characterId: String?,
    navBack: () -> Unit
) {
    val characterDetailsVM: CharacterDetailsVM = hiltViewModel()

    val hasRunAsyncInit = remember { mutableStateOf(false) }
    if (!hasRunAsyncInit.value) {
        characterDetailsVM.asyncInit(characterId)
        hasRunAsyncInit.value = true
    }

    characterDetailsVM.updateScaffoldState(navBack)

    val characterState = characterDetailsVM.characterStateFlow.collectAsState()

    CharacterDetailsScreenContent(
        characterState = characterState.value,
        inEditMode = characterDetailsVM.viewState.inEditMode,
        onNameChanged = characterDetailsVM::updateName
    )
}

@Composable
fun CharacterDetailsScreenContent(
    characterState: State<Character>,
    inEditMode: Boolean,
    onNameChanged: (String) -> Unit
) {
    val character = characterState.getOrNull()
    Column(modifier = Modifier.padding(Dimens.Spacing.md)) {
        character?.toSummary()?.let {
            Text(
                text = FormattedResource(R.string.name_label).resolve(),
                style = MaterialTheme.typography.overline
            )
            EditableText(
                text = it.name ?: FormattedResource(R.string.default_character_name).resolve(),
                onTextChanged = onNameChanged,
                inEditMode = inEditMode,
                textStyle = MaterialTheme.typography.h6
            )

            HorizontalSpacer.Small()

            Text(
                text = FormattedResource(R.string.classes_label).resolve(),
                style = MaterialTheme.typography.overline
            )
            CharacterClasses(classes = it.classes)

        }
    }
}

/****************************************** Previews **********************************************/

@Preview
//@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CharacterDetailsScreenPreview() {
    PreviewSurface {
        CharacterDetailsScreenContent(
            characterState = State.Success(DEFAULT_CHARACTERS[0]),
            inEditMode = false,
            onNameChanged = {}
        )
    }
}

@Preview
//@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CharacterDetailsScreenEditModePreview() {
    PreviewSurface {
        CharacterDetailsScreenContent(
            characterState = State.Success(DEFAULT_CHARACTERS[0]),
            inEditMode = true,
            onNameChanged = {}
        )
    }
}