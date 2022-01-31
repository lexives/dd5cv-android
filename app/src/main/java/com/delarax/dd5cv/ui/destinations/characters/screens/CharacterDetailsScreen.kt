package com.delarax.dd5cv.ui.destinations.characters.screens

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.delarax.dd5cv.R
import com.delarax.dd5cv.data.characters.remote.RemoteCharacterDataSourceMocked.Companion.DEFAULT_CHARACTERS
import com.delarax.dd5cv.models.characters.Character
import com.delarax.dd5cv.models.data.State
import com.delarax.dd5cv.models.ui.FormattedResource
import com.delarax.dd5cv.ui.components.HorizontalSpacer
import com.delarax.dd5cv.ui.components.PreviewSurface
import com.delarax.dd5cv.ui.components.TabData
import com.delarax.dd5cv.ui.components.TabScreenLayout
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
    val tabs = listOf(
        TabData(
            text = FormattedResource(R.string.character_description_tab),
            content = {
                CharacterDescriptionTab(
                    characterState = characterState,
                    inEditMode = inEditMode,
                    onNameChanged = onNameChanged
                )
            }
        ),
        TabData(text = FormattedResource(R.string.character_combat_tab)),
        TabData(text = FormattedResource(R.string.character_skills_tab))
    )

    TabScreenLayout(
        tabs = tabs,
        contentPadding = Dimens.Spacing.md,
    )
}

@Composable
fun CharacterDescriptionTab(
    characterState: State<Character>,
    inEditMode: Boolean,
    onNameChanged: (String) -> Unit
) {
    val character = characterState.getOrNull()
    character?.toSummary()?.let {
        Text(
            text = stringResource(R.string.name_label),
            style = MaterialTheme.typography.overline
        )
        EditableText(
            text = it.name ?: stringResource(R.string.default_character_name),
            onTextChanged = onNameChanged,
            inEditMode = inEditMode,
            textStyle = MaterialTheme.typography.h6
        )

        HorizontalSpacer.Small()

        Text(
            text = stringResource(R.string.classes_label),
            style = MaterialTheme.typography.overline
        )
        CharacterClasses(classes = it.classes)

    }
}
/****************************************** Previews **********************************************/

@Preview
//@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CharacterDetailsScreenPreview() {
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
private fun CharacterDetailsScreenEditModePreview() {
    PreviewSurface {
        CharacterDetailsScreenContent(
            characterState = State.Success(DEFAULT_CHARACTERS[0]),
            inEditMode = true,
            onNameChanged = {}
        )
    }
}