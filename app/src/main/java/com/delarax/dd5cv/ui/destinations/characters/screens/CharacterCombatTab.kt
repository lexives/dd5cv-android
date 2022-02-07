package com.delarax.dd5cv.ui.destinations.characters.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.delarax.dd5cv.R
import com.delarax.dd5cv.extensions.toStringOrEmpty
import com.delarax.dd5cv.models.characters.Character
import com.delarax.dd5cv.models.characters.DeathSave
import com.delarax.dd5cv.models.data.State
import com.delarax.dd5cv.models.ui.DialogData
import com.delarax.dd5cv.models.ui.FormattedResource
import com.delarax.dd5cv.ui.components.PreviewSurface
import com.delarax.dd5cv.ui.components.layout.BorderedColumn
import com.delarax.dd5cv.ui.components.layout.HorizontalSpacer
import com.delarax.dd5cv.ui.components.resolve
import com.delarax.dd5cv.ui.components.text.BonusVisualTransformation
import com.delarax.dd5cv.ui.components.text.CondensedIntTextField
import com.delarax.dd5cv.ui.components.text.EditableIntText
import com.delarax.dd5cv.ui.components.text.IntVisualTransformation
import com.delarax.dd5cv.ui.destinations.characters.screens.shared.DeathSaves
import com.delarax.dd5cv.ui.destinations.characters.screens.shared.HealthBar
import com.delarax.dd5cv.ui.destinations.characters.viewmodels.CharacterDetailsVM
import com.delarax.dd5cv.ui.theme.Dimens
import com.delarax.dd5cv.ui.theme.Green500
import com.delarax.dd5cv.ui.theme.Yellow400
import com.delarax.dd5cv.ui.theme.shapes.ShieldShape
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.FlowPreview

@ExperimentalFoundationApi
@FlowPreview
@Composable
fun CharacterCombatTab(
    characterState: State<Character>,
    viewState: CharacterDetailsVM.ViewState,
    showCustomDialog: (DialogData.CustomDialog) -> Unit,
    hideDialog: () -> Unit,
    onCurrentHPChanged: (String) -> Unit,
    onMaxHPChanged: (String) -> Unit,
    onTemporaryHPChanged: (String) -> Unit,
    onDeathSaveFailuresChanged: (DeathSave) -> Unit,
    onDeathSaveSuccessesChanged: (DeathSave) -> Unit,
    takeDamage: (String) -> Unit,
    heal: (String) -> Unit,
    gainTempHP: (String) -> Unit,
    onProficiencyBonusChanged: (String) -> Unit,
    onArmorClassChanged: (String) -> Unit,
    onInitiativeChanged: (String) -> Unit,
    onWalkSpeedChanged: (String) -> Unit,
    onClimbSpeedChanged: (String) -> Unit,
    onFlySpeedChanged: (String) -> Unit,
    onSwimSpeedChanged: (String) -> Unit,
    onBurrowSpeedChanged: (String) -> Unit,
) {
    val mainStatsHeight = 110.dp
    val mainStatsWidth = 100.dp

    val speedStatsHeight = 90.dp
    val speedStatsWidth = 110.dp

    val healthTextBoxMinWidth = 52.dp
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
                    textStyle = TextStyle(
                        fontSize = Dimens.FontSize.lg,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colors.onSurface
                    ),
                    includeNegatives = true,
                    visualTransformation = IntVisualTransformation(),
                    inEditMode = viewState.inEditMode,
                    backgroundColor = healthTextBoxBackgroundColor,
                    modifier = Modifier
                        .defaultMinSize(minWidth = healthTextBoxMinWidth)
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
                    textStyle = TextStyle(
                        fontSize = Dimens.FontSize.lg,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colors.onSurface
                    ),
                    visualTransformation = IntVisualTransformation(),
                    inEditMode = viewState.inEditMode,
                    backgroundColor = healthTextBoxBackgroundColor,
                    modifier = Modifier
                        .defaultMinSize(minWidth = healthTextBoxMinWidth)
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
                    textStyle = TextStyle(
                        fontSize = Dimens.FontSize.lg,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colors.onSurface
                    ),
                    visualTransformation = IntVisualTransformation(),
                    inEditMode = viewState.inEditMode,
                    backgroundColor = healthTextBoxBackgroundColor,
                    modifier = Modifier
                        .defaultMinSize(minWidth = healthTextBoxMinWidth)
                        .width(IntrinsicSize.Min)
                )
            }
        }

        HorizontalSpacer.Medium()

        /**
         * Health Bar
         */
        HealthBar(
            currentHP = character.currentHP ?: 0,
            maxHP = character.maxHP ?: 0,
            tempHP = character.temporaryHP ?: 0,
            borderThickness = 2.dp,
            currentHPColor = Green500,
            tempHPColor = Yellow400
        )

        HorizontalSpacer.Medium()

        /**
         * Death Saves
         */
        DeathSaves(
            failures = character.deathSaveFailures,
            successes = character.deathSaveSuccesses,
            onFailuresChanged = onDeathSaveFailuresChanged,
            onSuccessesChanged = onDeathSaveSuccessesChanged,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally)
        )

        HorizontalSpacer.Medium()

        /**
         * Row of Take Damage, Heal, and Gain Temp HP buttons
         */
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                enabled = !viewState.inEditMode,
                onClick = {
                    showCustomDialog(
                        getHealthDialog(
                            buttonText = FormattedResource(R.string.take_damage_button_text),
                            onSubmit = takeDamage,
                            hideDialog = hideDialog
                        )
                    )
                },
                modifier = Modifier.wrapContentWidth()
            ) {
                Text(stringResource(R.string.take_damage_button_text))
            }
            Button(
                enabled = !viewState.inEditMode,
                onClick = {
                    showCustomDialog(
                        getHealthDialog(
                            buttonText = FormattedResource(R.string.gain_health_button_text),
                            onSubmit = heal,
                            hideDialog = hideDialog
                        )
                    )
                },
                modifier = Modifier.wrapContentWidth()
            ) {
                Text(stringResource(R.string.gain_health_button_text))
            }
            Button(
                enabled = !viewState.inEditMode,
                onClick = {
                    showCustomDialog(
                        getHealthDialog(
                            buttonText = FormattedResource(R.string.gain_temp_hp_button_text),
                            onSubmit = gainTempHP,
                            hideDialog = hideDialog
                        )
                    )
                },
                modifier = Modifier.wrapContentWidth()
            ) {
                Text(stringResource(R.string.gain_temp_hp_button_text))
            }
        }

        HorizontalSpacer.Medium()

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

        HorizontalSpacer.Large()

        /**
         * Adaptive Row of Speeds
         */
        FlowRow(
            mainAxisAlignment = FlowMainAxisAlignment.Center,
            mainAxisSpacing = Dimens.Spacing.md,
            crossAxisSpacing = Dimens.Spacing.md,
            modifier = Modifier.fillMaxWidth()
        ) {
            CenteredBorderedStat(
                height = speedStatsHeight,
                width = speedStatsWidth,
                text = character.speed.toStringOrEmpty(),
                onTextChanged = onWalkSpeedChanged,
                visualTransformation = IntVisualTransformation(),
                maxDigits = 3,
                borderShape = RoundedCornerShape(10.dp),
                borderWidth = 1.dp,
                inEditMode = viewState.inEditMode,
                label = stringResource(R.string.walk_speed_label),
                suffix = character.speed?.let { FormattedResource(R.string.ft) }
            )
            CenteredBorderedStat(
                height = speedStatsHeight,
                width = speedStatsWidth,
                text = character.climbSpeed.toStringOrEmpty(),
                onTextChanged = onClimbSpeedChanged,
                visualTransformation = IntVisualTransformation(),
                maxDigits = 3,
                borderShape = RoundedCornerShape(10.dp),
                borderWidth = 1.dp,
                inEditMode = viewState.inEditMode,
                label = stringResource(R.string.climb_speed_label),
                suffix = character.climbSpeed?.let { FormattedResource(R.string.ft) },
            )
            CenteredBorderedStat(
                height = speedStatsHeight,
                width = speedStatsWidth,
                text = character.flySpeed.toStringOrEmpty(),
                onTextChanged = onFlySpeedChanged,
                visualTransformation = IntVisualTransformation(),
                maxDigits = 3,
                borderShape = RoundedCornerShape(10.dp),
                borderWidth = 1.dp,
                inEditMode = viewState.inEditMode,
                label = stringResource(R.string.fly_speed_label),
                suffix = character.flySpeed?.let { FormattedResource(R.string.ft) }
            )
            CenteredBorderedStat(
                height = speedStatsHeight,
                width = speedStatsWidth,
                text = character.swimSpeed.toStringOrEmpty(),
                onTextChanged = onSwimSpeedChanged,
                visualTransformation = IntVisualTransformation(),
                maxDigits = 3,
                borderShape = RoundedCornerShape(10.dp),
                borderWidth = 1.dp,
                inEditMode = viewState.inEditMode,
                label = stringResource(R.string.swim_speed_label),
                suffix = character.swimSpeed?.let { FormattedResource(R.string.ft) }
            )
            CenteredBorderedStat(
                height = speedStatsHeight,
                width = speedStatsWidth,
                text = character.burrowSpeed.toStringOrEmpty(),
                onTextChanged = onBurrowSpeedChanged,
                visualTransformation = IntVisualTransformation(),
                maxDigits = 3,
                borderShape = RoundedCornerShape(10.dp),
                borderWidth = 1.dp,
                inEditMode = viewState.inEditMode,
                label = stringResource(R.string.burrow_speed_label),
                suffix = character.burrowSpeed?.let { FormattedResource(R.string.ft) }
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
    borderWidth: Dp = 2.dp,
    suffix: FormattedResource? = null,
    includeNegatives: Boolean = false
) {
    BorderedColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        borderShape = borderShape,
        borderWidth = borderWidth,
        modifier = Modifier
            .height(height)
            .width(width)
    ) {
        Row(
            verticalAlignment = Alignment.Bottom
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
                modifier = statModifier,
            )
            suffix?.let {
                Text(
                    text = it.resolve(),
                    fontSize = Dimens.FontSize.sm,
                    modifier = Modifier.padding(bottom = Dimens.Spacing.sm)
                )
            }
        }
        Text(
            text = label,
            textAlign = TextAlign.Center,
            fontSize = Dimens.FontSize.sm,
            modifier = labelModifier
        )
    }
}

private fun getHealthDialog(
    buttonText: FormattedResource,
    onSubmit: (String) -> Unit,
    hideDialog: () -> Unit
): DialogData.CustomDialog = DialogData.CustomDialog(
    onDismissRequest = hideDialog,
    content = {
        val (damage, setDamage) = remember { mutableStateOf("") }
        val focusRequester = remember { FocusRequester() }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens.Spacing.lg),
            modifier = Modifier.padding(Dimens.Spacing.lg)
        ) {
            CondensedIntTextField(
                value = damage,
                onValueChange = setDamage,
                maxDigits = 3,
                textStyle = TextStyle(
                    fontSize = Dimens.FontSize.xxl,
                    textAlign = TextAlign.Center,
                    baselineShift = BaselineShift(-0.25f),
                    color = MaterialTheme.colors.onSurface
                ),
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .width(80.dp)
                    .height(60.dp)
            )
            Button(
                onClick = {
                    onSubmit(damage)
                    hideDialog()
                },
                Modifier.width(IntrinsicSize.Max)
            ) {
                Text(buttonText.resolve())
            }
            // Requests focus to the text field because it has modifier.focusRequester
            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }
        }
    }
)

/****************************************** Previews **********************************************/

private val demoCharacter = Character(
    currentHP = 10,
    maxHP = 20,
    temporaryHP = 5,
    proficiencyBonusOverride = 2,
    armorClassOverride = 14,
    initiativeOverride = 3
)

@ExperimentalFoundationApi
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
                showCustomDialog = {},
                hideDialog = {},
                onCurrentHPChanged = {},
                onMaxHPChanged = {},
                onTemporaryHPChanged = {},
                onDeathSaveFailuresChanged = {},
                onDeathSaveSuccessesChanged = {},
                takeDamage = {},
                heal = {},
                gainTempHP = {},
                onProficiencyBonusChanged = {},
                onArmorClassChanged = {},
                onInitiativeChanged = {},
                onWalkSpeedChanged = {},
                onClimbSpeedChanged = {},
                onFlySpeedChanged = {},
                onSwimSpeedChanged = {},
                onBurrowSpeedChanged = {}
            )
        }
    }
}

@ExperimentalFoundationApi
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
                showCustomDialog = {},
                hideDialog = {},
                onCurrentHPChanged = {},
                onMaxHPChanged = {},
                onTemporaryHPChanged = {},
                onDeathSaveFailuresChanged = {},
                onDeathSaveSuccessesChanged = {},
                takeDamage = {},
                heal = {},
                gainTempHP = {},
                onProficiencyBonusChanged = {},
                onArmorClassChanged = {},
                onInitiativeChanged = {},
                onWalkSpeedChanged = {},
                onClimbSpeedChanged = {},
                onFlySpeedChanged = {},
                onSwimSpeedChanged = {},
                onBurrowSpeedChanged = {}
            )
        }
    }
}