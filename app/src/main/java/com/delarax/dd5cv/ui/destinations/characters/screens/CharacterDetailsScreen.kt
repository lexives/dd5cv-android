package com.delarax.dd5cv.ui.destinations.characters.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.delarax.dd5cv.extensions.filterToInt
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
import com.delarax.dd5cv.ui.components.text.IntVisualTransformation
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
        viewState = characterDetailsVM.viewState,
        onNameChanged = characterDetailsVM::updateName,
        onCurrentHPChanged = characterDetailsVM::updateCurrentHP,
        onMaxHPChanged = characterDetailsVM::updateMaxHP,
        onTemporaryHPChanged = characterDetailsVM::updateTemporaryHP,
        onProficiencyBonusChanged = characterDetailsVM::updateProficiencyBonus,
        onArmorClassChanged = characterDetailsVM::updateArmorClass,
        onInitiativeChanged = characterDetailsVM::updateInitiative,
    )
}

@FlowPreview
@ExperimentalPagerApi
@ExperimentalMaterialApi
@Composable
fun CharacterDetailsScreenContent(
    characterState: State<Character>,
    viewState: CharacterDetailsVM.ViewState,
    onNameChanged: (String) -> Unit,
    onCurrentHPChanged: (String) -> Unit,
    onMaxHPChanged: (String) -> Unit,
    onTemporaryHPChanged: (String) -> Unit,
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
                    inEditMode = viewState.inEditMode,
                    onNameChanged = onNameChanged
                )
            }
        ),
        TabData(
            text = FormattedResource(R.string.character_combat_tab),
            content = {
                CharacterCombatTab(
                    characterState = characterState,
                    viewState = viewState,
                    onCurrentHPChanged = onCurrentHPChanged,
                    onMaxHPChanged = onMaxHPChanged,
                    onTemporaryHPChanged = onTemporaryHPChanged,
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
            textStyle = MaterialTheme.typography.h6
        )

        HorizontalSpacer.Small()

        Text(
            text = stringResource(R.string.character_classes_label),
            style = MaterialTheme.typography.overline
        )
        CharacterClasses(classes = it.classes)
    }
}

@FlowPreview
@Composable
fun CharacterCombatTab(
    characterState: State<Character>,
    viewState: CharacterDetailsVM.ViewState,
    onCurrentHPChanged: (String) -> Unit,
    onMaxHPChanged: (String) -> Unit,
    onTemporaryHPChanged: (String) -> Unit,
    onProficiencyBonusChanged: (String) -> Unit,
    onArmorClassChanged: (String) -> Unit,
    onInitiativeChanged: (String) -> Unit,
) {
    val healthTextBoxMinSize = 40.dp
    characterState.getOrNull()?.let { character ->
        Row(modifier = Modifier.fillMaxWidth()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = stringResource(R.string.character_hp_label),
                    modifier = Modifier.padding(end = Dimens.Spacing.sm)
                )
                EditableText(
                    text = stringResource(id = R.string.single_arg, character.currentHP.toStringOrEmpty()),
                    onTextChanged = { text ->
                        if (text.length <= 3) {
                            onCurrentHPChanged(
                                text.filterToInt(maxDigits = 3, includeNegatives = false)
                            )
                        }
                    },
                    visualTransformation = IntVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    inEditMode = viewState.inEditMode,
                    modifier = Modifier
                        .defaultMinSize(minWidth = healthTextBoxMinSize)
                        .width(IntrinsicSize.Min)
                )
                Text(
                    text = stringResource(id = R.string.slash),
                    modifier = Modifier.padding(horizontal = Dimens.Spacing.sm)
                )
                EditableText(
                    text = stringResource(id = R.string.single_arg, character.maxHP.toStringOrEmpty()),
                    onTextChanged = { text ->
                        if (text.length <= 3) {
                            onMaxHPChanged(
                                text.filterToInt(maxDigits = 3, includeNegatives = false)
                            )
                        }
                    },
                    visualTransformation = IntVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    inEditMode = viewState.inEditMode,
                    modifier = Modifier
                        .defaultMinSize(minWidth = healthTextBoxMinSize)
                        .width(IntrinsicSize.Min)
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = stringResource(R.string.character_temp_hp_label),
                    modifier = Modifier.padding(end = Dimens.Spacing.sm)
                )
                EditableText(
                    text = stringResource(id = R.string.single_arg, character.temporaryHP.toStringOrEmpty()),
                    onTextChanged = { text ->
                        if (text.length <= 3) {
                            onTemporaryHPChanged(
                                text.filterToInt(maxDigits = 3, includeNegatives = false)
                            )
                        }
                    },
                    visualTransformation = IntVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    inEditMode = viewState.inEditMode,
                    modifier = Modifier
                        .defaultMinSize(minWidth = healthTextBoxMinSize)
                        .width(IntrinsicSize.Min)
                )
            }
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
                    text = character.proficiencyBonusOverride.toStringOrEmpty(),
                    onTextChanged = { text ->
                        if (text.length <= 2) {
                            onProficiencyBonusChanged(
                                text.filterToInt(maxDigits = 2, includeNegatives = false)
                            )
                        }
                    },
                    visualTransformation = BonusVisualTransformation(),
                    inEditMode = viewState.inEditMode,
                    textStyle = TextStyle(
                        textAlign = TextAlign.Center,
                        fontSize = Dimens.FontSize.xxl
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    )
                )
                Text(
                    text = stringResource(R.string.character_proficiency_bonus_label),
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
                    text = character.armorClassOverride.toStringOrEmpty(),
                    onTextChanged = { text ->
                        if (text.length <= 2) {
                            onArmorClassChanged(
                                text.filterToInt(maxDigits = 2, includeNegatives = false)
                            )
                        }
                    },
                    visualTransformation = IntVisualTransformation(),
                    inEditMode = viewState.inEditMode,
                    textStyle = TextStyle(
                        textAlign = TextAlign.Center,
                        fontSize = Dimens.FontSize.xxl
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    modifier = Modifier.fillMaxWidth(.8f)
                )
                Text(
                    text = stringResource(R.string.character_armor_class_label),
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
                    text = viewState.initiativeString,
                    onTextChanged = { text ->
                        if (text.length <= 2 || (text.startsWith("-") && text.length == 3)) {
                            onInitiativeChanged(text.filterToInt(maxDigits = 2))
                        }
                    },
                    visualTransformation = BonusVisualTransformation(),
                    inEditMode = viewState.inEditMode,
                    textStyle = TextStyle(
                        textAlign = TextAlign.Center,
                        fontSize = Dimens.FontSize.xxl
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    )
                )
                Text(
                    text = stringResource(R.string.character_initiative_label),
                    textAlign = TextAlign.Center,
                    fontSize = Dimens.FontSize.sm
                )
            }
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
//        CharacterDetailsScreenContent(
//            characterState = State.Success(DEFAULT_CHARACTERS[0]),
//            inEditMode = false,
//            onNameChanged = {}
//        )
        Column(modifier = Modifier.padding(Dimens.Spacing.md)) {
            CharacterCombatTab(
                characterState = State.Success(DEFAULT_CHARACTERS[0]),
                viewState = CharacterDetailsVM.ViewState(),
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

@FlowPreview
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
                viewState = CharacterDetailsVM.ViewState(inProgressCharacterId = "not null"),
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