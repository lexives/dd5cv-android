package com.delarax.dd5cv.ui.destinations.characters.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.delarax.dd5cv.R
import com.delarax.dd5cv.data.characters.CharacterRepoMockData.Companion.DEFAULT_CHARACTERS
import com.delarax.dd5cv.models.FormattedResource
import com.delarax.dd5cv.models.State
import com.delarax.dd5cv.models.characters.Character
import com.delarax.dd5cv.models.navigation.CustomScaffoldState
import com.delarax.dd5cv.ui.components.ActionItem
import com.delarax.dd5cv.ui.components.PreviewSurface
import com.delarax.dd5cv.ui.destinations.characters.components.CharacterSummaryComponent
import com.delarax.dd5cv.ui.destinations.characters.viewmodels.CharacterDetailsVM
import com.delarax.dd5cv.ui.theme.Dimens

@Composable
fun CharacterDetailsScreen(
    characterId: String?,
    onBackPress: () -> Unit,
    setScaffold: (CustomScaffoldState) -> Unit
) {
    val characterDetailsVM: CharacterDetailsVM = hiltViewModel()
    characterDetailsVM.fetchCharacterById(characterId)

    setScaffold(
        CustomScaffoldState(
            title = characterDetailsVM.characterState.getOrNull()?.let {
                it.name?.let { name ->
                    FormattedResource(
                        resId = R.string.single_arg,
                        values = listOf(name)
                    )
                } ?: FormattedResource(R.string.default_character_name)

            } ?: FormattedResource(R.string.destination_characters_title),
            leftActionItem = ActionItem(
                name = stringResource(id = R.string.action_item_back),
                icon = Icons.Default.ArrowBack,
                onClick = onBackPress
            ),
            actionMenu = listOf(
                if (characterDetailsVM.viewState.inEditMode) {
                    ActionItem(
                        name = stringResource(R.string.action_item_turn_off_edit_mode),
                        icon = Icons.Default.Done,
                        onClick = {
                            characterDetailsVM.turnOffEditMode()
                        }
                    )
                } else {
                    ActionItem(
                        name = stringResource(R.string.action_item_turn_on_edit_mode),
                        icon = Icons.Default.Edit,
                        onClick = {
                            characterDetailsVM.turnOnEditMode()
                        }
                    )
                }
            )
        )
    )

    CharacterDetailsScreenContent(
        characterState = characterDetailsVM.characterState,
        viewState = characterDetailsVM.viewState
    )
}

@Composable
fun CharacterDetailsScreenContent(
    characterState: State<Character>,
    viewState: CharacterDetailsVM.ViewState
) {
    val character = characterState.getOrNull()
    Column(modifier = Modifier.padding(Dimens.Spacing.md)) {
        Text(if (viewState.inEditMode) "Edit Mode: ON" else "Edit Mode: OFF") // TODO: remove this
        Divider(modifier = Modifier.padding(vertical = Dimens.Spacing.sm)) // TODO: remove this
        character?.toSummary()?.let { CharacterSummaryComponent(characterSummary = it) }
    }
}

/****************************************** Previews **********************************************/

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CharacterDetailsScreenPreview() {
    PreviewSurface {
        CharacterDetailsScreenContent(
            State.Success(DEFAULT_CHARACTERS[0]),
            CharacterDetailsVM.ViewState(inEditMode = false)
        )
    }
}