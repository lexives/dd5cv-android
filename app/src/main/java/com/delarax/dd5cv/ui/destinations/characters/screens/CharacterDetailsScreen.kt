package com.delarax.dd5cv.ui.destinations.characters.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.delarax.dd5cv.R
import com.delarax.dd5cv.data.characters.remote.RemoteCharacterDataSourceMocked.Companion.DEFAULT_CHARACTERS
import com.delarax.dd5cv.extensions.toStringOrEmpty
import com.delarax.dd5cv.models.characters.Character
import com.delarax.dd5cv.models.data.State
import com.delarax.dd5cv.models.ui.FormattedResource
import com.delarax.dd5cv.ui.components.HorizontalSpacer
import com.delarax.dd5cv.ui.components.PreviewSurface
import com.delarax.dd5cv.ui.components.TabData
import com.delarax.dd5cv.ui.components.TabScreenLayout
import com.delarax.dd5cv.ui.components.text.BonusVisualTransformation
import com.delarax.dd5cv.ui.components.text.EditableText
import com.delarax.dd5cv.ui.destinations.characters.screens.shared.CharacterClasses
import com.delarax.dd5cv.ui.destinations.characters.screens.shared.HealthBar
import com.delarax.dd5cv.ui.destinations.characters.viewmodels.CharacterDetailsVM
import com.delarax.dd5cv.ui.theme.Dimens
import com.delarax.dd5cv.ui.theme.shapes.ShieldShape
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.FlowPreview

@ExperimentalPagerApi
@ExperimentalMaterialApi
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
        onNameChanged = characterDetailsVM::updateName,
        onProficiencyBonusChanged = characterDetailsVM::updateProficiencyBonus,
        onArmorClassChanged = characterDetailsVM::updateArmorClass,
        onInitiativeChanged = characterDetailsVM::updateInitiative,
    )
}

@ExperimentalPagerApi
@ExperimentalMaterialApi
@Composable
fun CharacterDetailsScreenContent(
    characterState: State<Character>,
    inEditMode: Boolean,
    onNameChanged: (String) -> Unit,
    onProficiencyBonusChanged: (String) -> Unit,
    onArmorClassChanged: (String) -> Unit,
    onInitiativeChanged: (String) -> Unit,
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
        TabData(
            text = FormattedResource(R.string.character_combat_tab),
            content = {
                CharacterCombatTab(
                    characterState = characterState,
                    inEditMode = inEditMode,
                    onCurrentHPChanged = {},
                    onMaxHPChanged = {},
                    onTemporaryHPChanged = {},
                    onProficiencyBonusChanged = onProficiencyBonusChanged,
                    onArmorClassChanged = onArmorClassChanged,
                    onInitiativeChanged = onInitiativeChanged
                )
            }
        ),
        TabData(text = FormattedResource(R.string.character_skills_tab))
    )
    TabScreenLayout(
        tabData = tabs,
        scrollable = false,
        contentPadding = Dimens.Spacing.md,
        indicatorColor = MaterialTheme.colors.primaryVariant
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
            text = stringResource(R.string.character_name_label),
            style = MaterialTheme.typography.overline
        )
        EditableText(
            text = it.name ?: stringResource(R.string.default_character_name),
            onTextChanged = onNameChanged,
            inEditMode = inEditMode,
            textStyle = MaterialTheme.typography.h6,
            singleLine = true
        )

        HorizontalSpacer.Small()

        Text(
            text = stringResource(R.string.character_classes_label),
            style = MaterialTheme.typography.overline
        )
        CharacterClasses(classes = it.classes)
    }
}

@Composable
fun CharacterCombatTab(
    characterState: State<Character>,
    inEditMode: Boolean,
    onCurrentHPChanged: (String) -> Unit,
    onMaxHPChanged: (String) -> Unit,
    onTemporaryHPChanged: (String) -> Unit,
    onProficiencyBonusChanged: (String) -> Unit,
    onArmorClassChanged: (String) -> Unit,
    onInitiativeChanged: (String) -> Unit,
) {
    characterState.getOrNull()?.let { character ->

        var proficiencyBonusString by remember {
            mutableStateOf(character.proficiencyBonusOverride.toStringOrEmpty())
        }
        var armorClassString by remember {
            mutableStateOf(character.armorClassOverride.toStringOrEmpty())
        }
        var initiativeString by remember {
            mutableStateOf(character.initiativeOverride.toStringOrEmpty())
        }
        Row(
            modifier = Modifier.padding(horizontal = Dimens.Spacing.sm)
        ) {
            Text(
                text = stringResource(
                    id = R.string.character_hp,
                    character.currentHP ?: 0,
                    character.maxHP ?: 0,
                ),
                modifier = Modifier
                    .weight(1f)
                    .wrapContentWidth(Alignment.Start)
            )
            Text(
                text = stringResource(R.string.character_temp_hp, character.temporaryHP ?: 0),
                modifier = Modifier
                    .weight(1f)
                    .wrapContentWidth(Alignment.End),
            )
        }
        HealthBar(
            currentHP = character.currentHP ?: 0,
            maxHP = character.maxHP ?: 0,
            tempHP = character.temporaryHP ?: 0,
            modifier = Modifier.padding(vertical = Dimens.Spacing.sm)
        )
        HorizontalSpacer.Small()
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .height(110.dp)
                    .width(100.dp)
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colors.onSurface,
                        shape = RoundedCornerShape(30.dp)
                    )
                    .padding(Dimens.Spacing.md)
            ) {
                EditableText(
                    text = proficiencyBonusString,
                    onTextChanged = { text ->
                        val filteredText = text.filterIndexed { i, char ->
                            char.isDigit() || (i == 0 && char == '-')
                        }
                        proficiencyBonusString = filteredText
                        onProficiencyBonusChanged(filteredText)
                    },
                    visualTransformation = BonusVisualTransformation(),
                    inEditMode = inEditMode,
                    textStyle = TextStyle(
                        textAlign = TextAlign.Center,
                        fontSize = Dimens.FontSize.xxl
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    singleLine = true
                )
                Text(
                    text = stringResource(R.string.character_proficiency_bonus),
                    textAlign = TextAlign.Center,
                    fontSize = Dimens.FontSize.sm
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .height(110.dp)
                    .width(100.dp)
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colors.onSurface,
                        shape = ShieldShape(20.dp)
                    )
                    .padding(Dimens.Spacing.md)
            ) {
                EditableText(
                    text = armorClassString,
                    onTextChanged = { text ->
                        val filteredText = text.filterIndexed { i, char ->
                            char.isDigit() || (i == 0 && char == '-')
                        }
                        armorClassString = filteredText
                        onArmorClassChanged(filteredText)
                    },
                    inEditMode = inEditMode,
                    textStyle = TextStyle(
                        textAlign = TextAlign.Center,
                        fontSize = Dimens.FontSize.xxl
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    modifier = Modifier.fillMaxWidth(.8f),
                    singleLine = true
                )
                Text(
                    text = stringResource(R.string.character_armor_class),
                    textAlign = TextAlign.Center,
                    fontSize = Dimens.FontSize.sm,
                    modifier = Modifier.padding(horizontal = Dimens.Spacing.md)
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .height(110.dp)
                    .width(100.dp)
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colors.onSurface,
                        shape = CutCornerShape(20.dp)
                    )
                    .padding(Dimens.Spacing.md)
            ) {
                EditableText(
                    text = initiativeString,
                    onTextChanged = { text ->
                        val filteredText = text.filterIndexed { i, char ->
                            char.isDigit() || (i == 0 && char == '-')
                        }
                        initiativeString = filteredText
                        onInitiativeChanged(filteredText)
                    },
                    visualTransformation = BonusVisualTransformation(),
                    inEditMode = inEditMode,
                    textStyle = TextStyle(
                        textAlign = TextAlign.Center,
                        fontSize = Dimens.FontSize.xxl
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    singleLine = true
                )
                Text(
                    text = stringResource(R.string.character_initiative),
                    textAlign = TextAlign.Center,
                    fontSize = Dimens.FontSize.sm
                )
            }
        }
    }
}

/****************************************** Previews **********************************************/

@ExperimentalPagerApi
@ExperimentalMaterialApi
@Preview
//@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CharacterDetailsScreenPreview() {
    PreviewSurface {
//        CharacterDetailsScreenContent(
//            characterState = State.Success(DEFAULT_CHARACTERS[0]),
//            inEditMode = false,
//            onNameChanged = {}
//        )
        Column(modifier = Modifier.padding(Dimens.Spacing.md)) {
            CharacterCombatTab(
                characterState = State.Success(DEFAULT_CHARACTERS[0]),
                inEditMode = false,
                onCurrentHPChanged = {},
                onMaxHPChanged = {},
                onTemporaryHPChanged = {},
                onProficiencyBonusChanged = {},
                onArmorClassChanged = {},
                onInitiativeChanged = {}
            )
        }
    }
}

@ExperimentalPagerApi
@ExperimentalMaterialApi
@Preview
//@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CharacterDetailsScreenEditModePreview() {
    PreviewSurface {
//        CharacterDetailsScreenContent(
//            characterState = State.Success(DEFAULT_CHARACTERS[0]),
//            inEditMode = true,
//            onNameChanged = {}
//        )
        Column(modifier = Modifier.padding(Dimens.Spacing.md)) {
            CharacterCombatTab(
                characterState = State.Success(DEFAULT_CHARACTERS[0]),
                inEditMode = true,
                onCurrentHPChanged = {},
                onMaxHPChanged = {},
                onTemporaryHPChanged = {},
                onProficiencyBonusChanged = {},
                onArmorClassChanged = {},
                onInitiativeChanged = {}
            )
        }
    }
}