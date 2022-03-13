package com.delarax.dd5cv.ui.destinations.characters.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
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
import com.delarax.dd5cv.ui.components.DividerWithText
import com.delarax.dd5cv.ui.components.PreviewSurface
import com.delarax.dd5cv.ui.components.layout.BorderedColumn
import com.delarax.dd5cv.ui.components.layout.HorizontalSpacer
import com.delarax.dd5cv.ui.components.resolve
import com.delarax.dd5cv.ui.components.text.BonusVisualTransformation
import com.delarax.dd5cv.ui.components.text.ButtonText
import com.delarax.dd5cv.ui.components.text.CondensedIntTextField
import com.delarax.dd5cv.ui.components.text.EditableIntText
import com.delarax.dd5cv.ui.components.text.IntVisualTransformation
import com.delarax.dd5cv.ui.destinations.characters.screens.shared.CenteredBorderedStat
import com.delarax.dd5cv.ui.destinations.characters.screens.shared.DeathSaves
import com.delarax.dd5cv.ui.destinations.characters.screens.shared.HealthBar
import com.delarax.dd5cv.ui.destinations.characters.viewmodels.CharacterDetailsVM
import com.delarax.dd5cv.ui.theme.Cyan300
import com.delarax.dd5cv.ui.theme.Dimens
import com.delarax.dd5cv.ui.theme.Green500
import com.delarax.dd5cv.ui.theme.Yellow400
import com.delarax.dd5cv.ui.theme.Yellow600
import com.delarax.dd5cv.ui.theme.shapes.ShieldShape
import com.google.accompanist.flowlayout.FlowCrossAxisAlignment
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.FlowPreview

private val MAIN_STAT_HEIGHT: Dp = 115.dp
private val MAIN_STAT_WIDTH: Dp = 105.dp
private val HEALTH_TEXT_BOX_MIN_WIDTH = 56.dp

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
    onInspirationChanged: (Boolean) -> Unit,
    onBaseSpeedChanged: (String) -> Unit,
    onClimbSpeedChanged: (String) -> Unit,
    onFlySpeedChanged: (String) -> Unit,
    onSwimSpeedChanged: (String) -> Unit,
    onBurrowSpeedChanged: (String) -> Unit
) {
    val healthTextBoxBackgroundColor = MaterialTheme.colors.onSurface.copy(alpha = 0.15f)
    val healthTextBoxBorderColor = MaterialTheme.colors.onSurface.copy(alpha = 0.25f)

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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = if (!viewState.inEditMode) {
                        Modifier.border(
                            width = 1.dp,
                            color = healthTextBoxBorderColor,
                            shape = RoundedCornerShape(5.dp)
                        )
                    } else Modifier
                ) {
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
                        backgroundColor = if (viewState.inEditMode) {
                            healthTextBoxBackgroundColor
                        } else null,
                        modifier = Modifier
                            .defaultMinSize(minWidth = HEALTH_TEXT_BOX_MIN_WIDTH)
                            .width(IntrinsicSize.Min)
                    )
                    Text(
                        text = stringResource(id = R.string.slash),
                        fontSize = Dimens.FontSize.lg,
                        modifier = Modifier.padding(horizontal = Dimens.Spacing.sm)
                    )
                    EditableIntText(
                        text = stringResource(
                            R.string.single_arg,
                            character.maxHP.toStringOrEmpty()
                        ),
                        onTextChanged = onMaxHPChanged,
                        maxDigits = 3,
                        textStyle = TextStyle(
                            fontSize = Dimens.FontSize.lg,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colors.onSurface
                        ),
                        visualTransformation = IntVisualTransformation(),
                        inEditMode = viewState.inEditMode,
                        backgroundColor = if (viewState.inEditMode) {
                            healthTextBoxBackgroundColor
                        } else null,
                        modifier = Modifier
                            .defaultMinSize(minWidth = HEALTH_TEXT_BOX_MIN_WIDTH)
                            .width(IntrinsicSize.Min)
                    )
                }
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
                    backgroundColor = if (viewState.inEditMode) {
                        healthTextBoxBackgroundColor
                    } else null,
                    modifier = if (!viewState.inEditMode) {
                        Modifier
                            .defaultMinSize(minWidth = HEALTH_TEXT_BOX_MIN_WIDTH)
                            .width(IntrinsicSize.Min)
                            .border(
                                width = 1.dp,
                                color = healthTextBoxBorderColor,
                                shape = RoundedCornerShape(5.dp)
                            )
                    } else {
                        Modifier
                            .defaultMinSize(minWidth = HEALTH_TEXT_BOX_MIN_WIDTH)
                            .width(IntrinsicSize.Min)
                    }
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
            barHeight = 32.dp,
            borderThickness = 2.dp,
            currentHPColor = Green500,
            tempHPColor = Yellow400
        )

        HorizontalSpacer.Medium()


        /**
         * Adaptive Row of Death Saves and Take Damage, Heal, and Gain Temp HP buttons
         */
        FlowRow(
            mainAxisAlignment = FlowMainAxisAlignment.Center,
            mainAxisSpacing = Dimens.Spacing.md,
            crossAxisSpacing = Dimens.Spacing.md,
            modifier = Modifier.fillMaxWidth()
        ) {
            DeathSaves(
                failures = character.deathSaveFailures,
                successes = character.deathSaveSuccesses,
                onFailuresChanged = onDeathSaveFailuresChanged,
                onSuccessesChanged = onDeathSaveSuccessesChanged,
                modifier = Modifier
                    .padding(horizontal = Dimens.Spacing.md)
                    .wrapContentWidth(Alignment.CenterHorizontally)
            )
            FlowRow(
                mainAxisAlignment = FlowMainAxisAlignment.Center,
                mainAxisSpacing = Dimens.Spacing.sm,
                crossAxisSpacing = Dimens.Spacing.sm,
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
                    ButtonText(stringResource(R.string.take_damage_button_text))
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
                    ButtonText(stringResource(R.string.gain_health_button_text))
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
                    ButtonText(stringResource(R.string.gain_temp_hp_button_text))
                }
            }
        }

        /**
         * Main Stats divider
         */
        DividerWithText(
            text = stringResource(id = R.string.main_stats_label),
            textStyle = MaterialTheme.typography.subtitle1,
            modifier = Modifier.padding(vertical = Dimens.Spacing.md)
        )
        HorizontalSpacer.Small()

        /**
         * Adaptive Row of Proficiency Bonus, Armor Class, Initiative, Walk Speed, and Inspiration
         */
        FlowRow(
            mainAxisAlignment = FlowMainAxisAlignment.Center,
            crossAxisAlignment = FlowCrossAxisAlignment.Center,
            mainAxisSpacing = Dimens.Spacing.md,
            crossAxisSpacing = Dimens.Spacing.md,
            modifier = Modifier.fillMaxWidth()
        ) {
            CenteredBorderedStat(
                height = MAIN_STAT_HEIGHT,
                width = MAIN_STAT_WIDTH,
                maxDigits = 2,
                text = character.proficiencyBonusOverride.toStringOrEmpty(),
                onTextChanged = onProficiencyBonusChanged,
                visualTransformation = BonusVisualTransformation(),
                borderShape = RoundedCornerShape(30.dp),
                inEditMode = viewState.inEditMode,
                label = stringResource(R.string.character_proficiency_bonus_label)
            )
            CenteredBorderedStat(
                height = MAIN_STAT_HEIGHT,
                width = MAIN_STAT_WIDTH,
                maxDigits = 2,
                text = character.armorClassOverride.toStringOrEmpty(),
                onTextChanged = onArmorClassChanged,
                visualTransformation = IntVisualTransformation(),
                borderShape = ShieldShape(20.dp),
                inEditMode = viewState.inEditMode,
                label = stringResource(R.string.character_armor_class_label),
                statModifier = Modifier
                    .fillMaxWidth(.8f)
                    .wrapContentWidth(Alignment.CenterHorizontally),
                labelModifier = Modifier.padding(horizontal = 18.dp)
            )
            CenteredBorderedStat(
                height = MAIN_STAT_HEIGHT,
                width = MAIN_STAT_WIDTH,
                maxDigits = 2,
                text = viewState.initiativeString,
                onTextChanged = onInitiativeChanged,
                visualTransformation = BonusVisualTransformation(),
                includeNegatives = true,
                borderShape = CutCornerShape(20.dp),
                inEditMode = viewState.inEditMode,
                label = stringResource(R.string.character_initiative_label)
            )
            CenteredBorderedStat(
                height = MAIN_STAT_WIDTH - 10.dp,
                width = MAIN_STAT_HEIGHT,
                maxDigits = 3,
                text = character.speed.toStringOrEmpty(),
                onTextChanged = onBaseSpeedChanged,
                visualTransformation = IntVisualTransformation(),
                borderShape = RoundedCornerShape(10.dp),
                inEditMode = viewState.inEditMode,
                label = FormattedResource(R.string.base_speed_label).resolve(),
                suffix = character.speed?.let { FormattedResource(R.string.ft) },
                statModifier = Modifier
                    .fillMaxWidth(0.9f)
                    .wrapContentWidth(Alignment.CenterHorizontally)
            )
            Inspiration(
                hasInspiration = character.inspiration,
                onInspirationChanged = onInspirationChanged
            )
        }

        /**
         * Other Speeds divider
         */
        DividerWithText(
            text = stringResource(id = R.string.other_speeds_label),
            textStyle = MaterialTheme.typography.subtitle1,
            modifier = Modifier.padding(vertical = Dimens.Spacing.md)
        )
        HorizontalSpacer.Small()

        /**
         * Adaptive Row of non-base speeds
         */
        FlowRow(
            mainAxisAlignment = FlowMainAxisAlignment.Center,
            mainAxisSpacing = Dimens.Spacing.md,
            crossAxisSpacing = Dimens.Spacing.md,
            modifier = Modifier.fillMaxWidth()
        ) {
            SpeedStat(
                text = character.climbSpeed?.toString(),
                onTextChanged = onClimbSpeedChanged,
                inEditMode = viewState.inEditMode,
                label = stringResource(R.string.climb_speed_label)
            )
            SpeedStat(
                text = character.flySpeed?.toString(),
                onTextChanged = onFlySpeedChanged,
                inEditMode = viewState.inEditMode,
                label = stringResource(R.string.fly_speed_label)
            )
            SpeedStat(
                text = character.swimSpeed?.toString(),
                onTextChanged = onSwimSpeedChanged,
                inEditMode = viewState.inEditMode,
                label = stringResource(R.string.swim_speed_label)
            )
            SpeedStat(
                text = character.burrowSpeed?.toString(),
                onTextChanged = onBurrowSpeedChanged,
                inEditMode = viewState.inEditMode,
                label = stringResource(R.string.burrow_speed_label)
            )
        }
    }
}

@Composable
private fun SpeedStat(
    text: String?,
    onTextChanged: (String) -> Unit,
    inEditMode: Boolean,
    label: String,
    borderWidth: Dp = 1.dp,
) {
    CenteredBorderedStat(
        height = 85.dp,
        width = 100.dp,
        text = text ?: "",
        onTextChanged = onTextChanged,
        visualTransformation = IntVisualTransformation(),
        maxDigits = 3,
        fontSize = Dimens.FontSize.xl,
        borderShape = RoundedCornerShape(10.dp),
        borderWidth = borderWidth,
        inEditMode = inEditMode,
        label = label,
        suffix = text?.let { FormattedResource(R.string.ft) }
    )
}

@Composable
private fun Inspiration(
    hasInspiration: Boolean,
    onInspirationChanged: (Boolean) -> Unit
) {
    BorderedColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        borderShape = CircleShape,
        borderWidth = 2.dp,
        modifier = Modifier
            .height(MAIN_STAT_HEIGHT)
            .width(MAIN_STAT_HEIGHT)
            .clip(CircleShape)
            .clickable { onInspirationChanged(!hasInspiration) }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(top = Dimens.Spacing.xs)
                .size(60.dp)
        ) {
            if (hasInspiration) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_inspiration_background),
                    contentDescription = null,
                    tint = Yellow600
                )
            }
            Icon(
                painter = painterResource(id = R.drawable.ic_d20_center_up),
                contentDescription = stringResource(R.string.inspiration_label),
                tint = if (hasInspiration) {
                    Cyan300
                } else {
                    MaterialTheme.colors.onSurface.copy(alpha = 0.25f)
                },
                modifier = Modifier.fillMaxSize(0.8f)
            )
        }
        Text(
            text = stringResource(R.string.inspiration_label),
            style = MaterialTheme.typography.body2,
            textAlign = TextAlign.Center
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
                ButtonText(buttonText.resolve())
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
    initiativeOverride = 3,
    speed = 100,
    inspiration = true
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
                onInspirationChanged = {},
                onBaseSpeedChanged = {},
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
                onInspirationChanged = {},
                onBaseSpeedChanged = {},
                onClimbSpeedChanged = {},
                onFlySpeedChanged = {},
                onSwimSpeedChanged = {},
                onBurrowSpeedChanged = {}
            )
        }
    }
}