package com.delarax.dd5cv.ui.destinations.characters.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
                traits = listOf(
                    "I have a strong sense of fair play and always try to find the most " +
                            "equitable solution to arguments."
                ),
                onTraitsChanged = {},
                label = FormattedResource(R.string.character_traits_label),
                inEditMode = false,
                modifier = Modifier.fillMaxWidth()
            )
            TraitList(
                traits = listOf(
                    "Freedom. Tyrants must not be allowed to oppress the people. (Chaotic)"
                ),
                onTraitsChanged = {},
                label = FormattedResource(R.string.character_ideals_label),
                inEditMode = false,
                modifier = Modifier.fillMaxWidth()
            )
            TraitList(
                traits = listOf(
                    "I wish my childhood sweetheart had come with me to pursue my destiny."
                ),
                onTraitsChanged = {},
                label = FormattedResource(R.string.character_bonds_label),
                inEditMode = false,
                modifier = Modifier.fillMaxWidth()
            )
            TraitList(
                traits = listOf(
                    "Secretly, I believe that things would be better if I were a tyrant lording" +
                            " over the land."
                ),
                onTraitsChanged = {},
                label = FormattedResource(R.string.character_flaws_label),
                inEditMode = false,
                modifier = Modifier.fillMaxWidth()
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