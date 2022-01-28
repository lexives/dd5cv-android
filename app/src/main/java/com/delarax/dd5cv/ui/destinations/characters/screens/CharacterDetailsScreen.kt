package com.delarax.dd5cv.ui.destinations.characters.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.delarax.dd5cv.R
import com.delarax.dd5cv.data.characters.remote.RemoteCharacterDataSourceMocked.Companion.DEFAULT_CHARACTERS
import com.delarax.dd5cv.models.FormattedResource
import com.delarax.dd5cv.models.State
import com.delarax.dd5cv.models.characters.Character
import com.delarax.dd5cv.ui.components.HorizontalSpacer
import com.delarax.dd5cv.ui.components.PreviewSurface
import com.delarax.dd5cv.ui.components.resolve
import com.delarax.dd5cv.ui.destinations.characters.screens.shared.CharacterClasses
import com.delarax.dd5cv.ui.destinations.characters.viewmodels.CharacterDetailsVM
import com.delarax.dd5cv.ui.theme.Dimens

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

    if (characterDetailsVM.viewState.inEditMode) {
        // because of isEditModeEnabled there should always be character data at this point
        CharacterDetailsScreenContentEditMode(
            character = characterDetailsVM.viewState.characterState.getOrDefault(Character()),
            onNameChanged = characterDetailsVM::updateName,
            onNameSubmit = characterDetailsVM::saveEdits
        )
    } else {
        CharacterDetailsScreenContent(
            characterState = characterDetailsVM.viewState.characterState,
        )
    }
}

@Composable
fun CharacterDetailsScreenContent(
    characterState: State<Character>
) {
    val character = characterState.getOrNull()
    Column(modifier = Modifier.padding(Dimens.Spacing.md)) {
        character?.toSummary()?.let {
            Text(
                text = FormattedResource(R.string.name_label).resolve(),
                fontSize = Dimens.FontSize.xs
            )
            Text(
                text = it.name ?: stringResource(R.string.default_character_name),
                style = MaterialTheme.typography.h6
            )

            HorizontalSpacer.Small()

            Text(
                text = FormattedResource(R.string.classes_label).resolve(),
                fontSize = Dimens.FontSize.xs
            )
            CharacterClasses(classes = it.classes)
        }
    }
}

@Composable
fun CharacterDetailsScreenContentEditMode(
    character: Character,
    onNameChanged: (String) -> Unit,
    onNameSubmit: () -> Unit
) {
    Column(modifier = Modifier.padding(Dimens.Spacing.md)) {
        // TODO
    }
}

/****************************************** Previews **********************************************/

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CharacterDetailsScreenPreview() {
    PreviewSurface {
        CharacterDetailsScreenContent(
            State.Success(DEFAULT_CHARACTERS[0])
        )
    }
}