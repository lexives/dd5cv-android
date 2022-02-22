package com.delarax.dd5cv.ui.destinations.characters.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.delarax.dd5cv.R
import com.delarax.dd5cv.data.characters.remote.RemoteCharacterDataSourceMocked.Companion.DEFAULT_CHARACTERS
import com.delarax.dd5cv.models.characters.Character
import com.delarax.dd5cv.models.data.State
import com.delarax.dd5cv.models.ui.DialogData
import com.delarax.dd5cv.models.ui.FormattedResource
import com.delarax.dd5cv.ui.components.PreviewSurface
import com.delarax.dd5cv.ui.components.layout.HorizontalSpacer
import com.delarax.dd5cv.ui.components.text.EditableText
import com.delarax.dd5cv.ui.destinations.characters.screens.shared.CharacterClasses
import com.delarax.dd5cv.ui.destinations.characters.screens.shared.TraitList
import com.delarax.dd5cv.ui.theme.Dimens
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.FlowPreview

@Composable
fun CharacterDescriptionTab(
    characterState: State<Character>,
    showCustomDialog: (DialogData.CustomDialog) -> Unit,
    hideDialog: () -> Unit,
    inEditMode: Boolean,
    onNameChanged: (String) -> Unit,
    onPersonalityTraitsChanged: (List<String>) -> Unit,
    onIdealsChanged: (List<String>) -> Unit,
    onBondsChanged: (List<String>) -> Unit,
    onFlawsChanged: (List<String>) -> Unit,

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
            textStyle = MaterialTheme.typography.h6.copy(
                color = MaterialTheme.colors.onSurface
            )
        )

        HorizontalSpacer.Small()

        Text(
            text = stringResource(R.string.character_classes_label),
            style = MaterialTheme.typography.overline
        )
        CharacterClasses(classes = it.classes)

        HorizontalSpacer.Medium()

        /**
         * Column of Personality Traits, Ideals, Bonds, and Flaws
         */
        Column(verticalArrangement = Arrangement.spacedBy(Dimens.Spacing.sm)) {
            TraitList(
                traits = character.personalityTraits,
                onTraitsChanged = onPersonalityTraitsChanged,
                label = FormattedResource(R.string.character_traits_label),
                emptyMessage = FormattedResource(R.string.character_traits_empty_message),
                contentDescription = stringResource(
                    R.string.trait_buttons_content_description
                ),
                showCustomDialog = showCustomDialog,
                hideDialog = hideDialog,
                inEditMode = inEditMode
            )
            TraitList(
                traits = character.ideals,
                onTraitsChanged = onIdealsChanged,
                label = FormattedResource(R.string.character_ideals_label),
                emptyMessage = FormattedResource(R.string.character_ideals_empty_message),
                contentDescription = stringResource(
                    R.string.ideal_buttons_content_description
                ),
                showCustomDialog = showCustomDialog,
                hideDialog = hideDialog,
                inEditMode = inEditMode
            )
            TraitList(
                traits = character.bonds,
                onTraitsChanged = onBondsChanged,
                label = FormattedResource(R.string.character_bonds_label),
                emptyMessage = FormattedResource(R.string.character_bonds_empty_message),
                contentDescription = stringResource(
                    R.string.bond_buttons_content_description
                ),
                showCustomDialog = showCustomDialog,
                hideDialog = hideDialog,
                inEditMode = inEditMode
            )
            TraitList(
                traits = character.flaws,
                onTraitsChanged = onFlawsChanged,
                label = FormattedResource(R.string.character_flaws_label),
                emptyMessage = FormattedResource(R.string.character_flaws_empty_message),
                contentDescription = stringResource(
                    R.string.flaw_buttons_content_description
                ),
                showCustomDialog = showCustomDialog,
                hideDialog = hideDialog,
                inEditMode = inEditMode
            )
        }
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
                showCustomDialog = {},
                hideDialog = {},
                inEditMode = false,
                onNameChanged = {},
                onPersonalityTraitsChanged = {},
                onIdealsChanged = {},
                onBondsChanged = {},
                onFlawsChanged = {}
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
                showCustomDialog = {},
                hideDialog = {},
                inEditMode = true,
                onNameChanged = {},
                onPersonalityTraitsChanged = {},
                onIdealsChanged = {},
                onBondsChanged = {},
                onFlawsChanged = {}
            )
        }
    }
}