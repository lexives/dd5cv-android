package com.delarax.dd5cv.ui.destinations.characters.screens

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.delarax.dd5cv.R
import com.delarax.dd5cv.extensions.toStringOrEmpty
import com.delarax.dd5cv.models.characters.Character
import com.delarax.dd5cv.models.data.State
import com.delarax.dd5cv.ui.components.PreviewSurface
import com.delarax.dd5cv.ui.components.layout.BorderedColumn
import com.delarax.dd5cv.ui.components.layout.HorizontalSpacer
import com.delarax.dd5cv.ui.components.text.BonusVisualTransformation
import com.delarax.dd5cv.ui.components.text.EditableIntText
import com.delarax.dd5cv.ui.components.text.IntVisualTransformation
import com.delarax.dd5cv.ui.destinations.characters.screens.shared.HealthBar
import com.delarax.dd5cv.ui.destinations.characters.viewmodels.CharacterDetailsVM
import com.delarax.dd5cv.ui.theme.Dimens
import com.delarax.dd5cv.ui.theme.shapes.ShieldShape
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.FlowPreview

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
    val mainStatsHeight = 110.dp
    val mainStatsWidth = 100.dp
    val healthTextBoxMinSize = 44.dp
    val healthTextBoxBackgroundColor = MaterialTheme.colors.onSurface.copy(alpha = 0.15f)

    characterState.getOrNull()?.let { character ->
        /**
         * Row of Current HP, Max HP, and Temporary HP
         */
        Row(modifier = Modifier.fillMaxWidth()) {
            // Row of Current HP / Max HP label & values
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = stringResource(R.string.character_hp_label),
                    modifier = Modifier.padding(end = Dimens.Spacing.sm)
                )
                EditableIntText(
                    text = stringResource(
                        R.string.single_arg, character.currentHP.toStringOrEmpty()
                    ),
                    onTextChanged = onCurrentHPChanged,
                    maxDigits = 3,
                    visualTransformation = IntVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    inEditMode = viewState.inEditMode,
                    backgroundColor = healthTextBoxBackgroundColor,
                    modifier = Modifier
                        .defaultMinSize(minWidth = healthTextBoxMinSize)
                        .width(IntrinsicSize.Min)
                )
                Text(
                    text = stringResource(id = R.string.slash),
                    modifier = Modifier.padding(horizontal = Dimens.Spacing.sm)
                )
                EditableIntText(
                    text = stringResource(R.string.single_arg, character.maxHP.toStringOrEmpty()),
                    onTextChanged = onMaxHPChanged,
                    maxDigits = 3,
                    visualTransformation = IntVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    inEditMode = viewState.inEditMode,
                    backgroundColor = healthTextBoxBackgroundColor,
                    modifier = Modifier
                        .defaultMinSize(minWidth = healthTextBoxMinSize)
                        .width(IntrinsicSize.Min)
                )
            }
            // Row of Temp HP label & value
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = stringResource(R.string.character_temp_hp_label),
                    modifier = Modifier.padding(end = Dimens.Spacing.sm)
                )
                EditableIntText(
                    text = stringResource(
                        R.string.single_arg, character.temporaryHP.toStringOrEmpty()
                    ),
                    onTextChanged = onTemporaryHPChanged,
                    maxDigits = 3,
                    visualTransformation = IntVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    inEditMode = viewState.inEditMode,
                    backgroundColor = healthTextBoxBackgroundColor,
                    modifier = Modifier
                        .defaultMinSize(minWidth = healthTextBoxMinSize)
                        .width(IntrinsicSize.Min)
                )
            }
        }

        /**
         * Health Bar
         */
        HealthBar(
            currentHP = character.currentHP ?: 0,
            maxHP = character.maxHP ?: 0,
            tempHP = character.temporaryHP ?: 0,
            borderThickness = 2.dp,
            modifier = Modifier.padding(vertical = Dimens.Spacing.sm)
        )

        HorizontalSpacer.Small()

        /**
         * Row of Proficiency Bonus, Armor Class, and Initiative
         */
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            CenteredBorderedStat(
                height = mainStatsHeight,
                width = mainStatsWidth,
                text = character.proficiencyBonusOverride.toStringOrEmpty(),
                onTextChanged = onProficiencyBonusChanged,
                visualTransformation = BonusVisualTransformation(),
                maxDigits = 2,
                borderShape = RoundedCornerShape(30.dp),
                inEditMode = viewState.inEditMode,
                label = stringResource(R.string.character_proficiency_bonus_label)
            )
            CenteredBorderedStat(
                height = mainStatsHeight,
                width = mainStatsWidth,
                text = character.armorClassOverride.toStringOrEmpty(),
                onTextChanged = onArmorClassChanged,
                visualTransformation = IntVisualTransformation(),
                maxDigits = 2,
                borderShape = ShieldShape(20.dp),
                inEditMode = viewState.inEditMode,
                label = stringResource(R.string.character_armor_class_label),
                statModifier = Modifier.fillMaxWidth(.8f),
                labelModifier = Modifier.padding(horizontal = Dimens.Spacing.md)
            )
            CenteredBorderedStat(
                height = mainStatsHeight,
                width = mainStatsWidth,
                text = viewState.initiativeString,
                onTextChanged = onInitiativeChanged,
                visualTransformation = BonusVisualTransformation(),
                maxDigits = 2,
                includeNegatives = true,
                borderShape = CutCornerShape(20.dp),
                inEditMode = viewState.inEditMode,
                label = stringResource(R.string.character_initiative_label)
            )
        }
    }
}

@Composable
private fun CenteredBorderedStat(
    height: Dp,
    width: Dp,
    text: String,
    onTextChanged: (String) -> Unit,
    visualTransformation: VisualTransformation,
    maxDigits: Int,
    borderShape: Shape,
    inEditMode: Boolean,
    label: String,
    statModifier: Modifier = Modifier,
    labelModifier: Modifier = Modifier,
    includeNegatives: Boolean = false
) {
    BorderedColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        borderShape = borderShape,
        modifier = Modifier
            .height(height)
            .width(width)
    ) {
        EditableIntText(
            text = text,
            onTextChanged = onTextChanged,
            maxDigits = maxDigits,
            includeNegatives = includeNegatives,
            visualTransformation = visualTransformation,
            inEditMode = inEditMode,
            textStyle = TextStyle(
                textAlign = TextAlign.Center,
                fontSize = Dimens.FontSize.xxl,
                color = MaterialTheme.colors.onSurface
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            modifier = statModifier,
        )
        Text(
            text = label,
            textAlign = TextAlign.Center,
            fontSize = Dimens.FontSize.sm,
            modifier = labelModifier
        )
    }
}

/****************************************** Previews **********************************************/

private val demoCharacter = Character(
    currentHP = 10,
    maxHP = 20,
    temporaryHP = 5,
    proficiencyBonusOverride = 2,
    armorClassOverride = 14,
    initiativeOverride = 3
)

@FlowPreview
@ExperimentalPagerApi
@ExperimentalMaterialApi
@Preview
//@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CharacterDetailsScreenPreview() {
    PreviewSurface {
        Column(modifier = Modifier.padding(Dimens.Spacing.md)) {
            CharacterCombatTab(
                characterState = State.Success(demoCharacter),
                viewState = CharacterDetailsVM.ViewState(initiativeString = "3"),
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
        Column(modifier = Modifier.padding(Dimens.Spacing.md)) {
            CharacterCombatTab(
                characterState = State.Success(demoCharacter),
                viewState = CharacterDetailsVM.ViewState(
                    initiativeString = "3",
                    inEditMode = true
                ),
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