package com.delarax.dd5cv.ui.destinations.characters.screens

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.delarax.dd5cv.R
import com.delarax.dd5cv.data.characters.remote.RemoteCharacterDataSourceMocked.Companion.DEFAULT_CHARACTERS
import com.delarax.dd5cv.models.characters.Ability
import com.delarax.dd5cv.models.characters.Character
import com.delarax.dd5cv.models.characters.DeathSave
import com.delarax.dd5cv.models.characters.Proficiency
import com.delarax.dd5cv.models.data.State
import com.delarax.dd5cv.models.ui.DialogData
import com.delarax.dd5cv.models.ui.FormattedResource
import com.delarax.dd5cv.ui.components.PreviewSurface
import com.delarax.dd5cv.ui.components.layout.TabData
import com.delarax.dd5cv.ui.components.layout.TabScreenLayout
import com.delarax.dd5cv.ui.destinations.characters.viewmodels.CharacterDetailsVM
import com.delarax.dd5cv.ui.theme.Dimens
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.FlowPreview

@ExperimentalFoundationApi
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
        showCustomDialog = characterDetailsVM::showCustomDialog,
        hideDialog = characterDetailsVM::hideDialog,
        onNameChanged = characterDetailsVM::updateName,
        onPersonalityTraitsChanged = characterDetailsVM::updatePersonalityTraits,
        onIdealsChanged = characterDetailsVM::updateIdeals,
        onBondsChanged = characterDetailsVM::updateBonds,
        onFlawsChanged = characterDetailsVM::updateFlaws,
        onCurrentHPChanged = characterDetailsVM::updateCurrentHP,
        onMaxHPChanged = characterDetailsVM::updateMaxHP,
        onTemporaryHPChanged = characterDetailsVM::updateTemporaryHP,
        onDeathSaveFailuresChanged = characterDetailsVM::updateDeathSaveFailures,
        onDeathSaveSuccessesChanged = characterDetailsVM::updateDeathSaveSuccesses,
        takeDamage = characterDetailsVM::takeDamage,
        heal = characterDetailsVM::heal,
        gainTempHP = characterDetailsVM::gainTempHP,
        onProficiencyBonusChanged = characterDetailsVM::updateProficiencyBonus,
        onArmorClassChanged = characterDetailsVM::updateArmorClass,
        onInitiativeChanged = characterDetailsVM::updateInitiative,
        onInspirationChanged = characterDetailsVM::updateInspiration,
        onBaseSpeedChanged = characterDetailsVM::updateBaseSpeed,
        onClimbSpeedChanged = characterDetailsVM::updateClimbSpeed,
        onFlySpeedChanged = characterDetailsVM::updateFlySpeed,
        onSwimSpeedChanged = characterDetailsVM::updateSwimSpeed,
        onBurrowSpeedChanged = characterDetailsVM::updateBurrowSpeed,
        onAbilityScoreChanged = characterDetailsVM::updateAbilityScore,
        onToggleProficiency = characterDetailsVM::toggleSkillProficiency,
        onToggleExpertise = characterDetailsVM::toggleSkillExpertise
    )
}

@ExperimentalFoundationApi
@FlowPreview
@ExperimentalPagerApi
@ExperimentalMaterialApi
@Composable
fun CharacterDetailsScreenContent(
    characterState: State<Character>,
    viewState: CharacterDetailsVM.ViewState,
    showCustomDialog: (DialogData.CustomDialog) -> Unit,
    hideDialog: () -> Unit,
    onNameChanged: (String) -> Unit,
    onPersonalityTraitsChanged: (List<String>) -> Unit,
    onIdealsChanged: (List<String>) -> Unit,
    onBondsChanged: (List<String>) -> Unit,
    onFlawsChanged: (List<String>) -> Unit,
    onCurrentHPChanged: (String) -> Unit,
    onMaxHPChanged: (String) -> Unit,
    onDeathSaveFailuresChanged: (DeathSave) -> Unit,
    onDeathSaveSuccessesChanged: (DeathSave) -> Unit,
    takeDamage: (String) -> Unit,
    heal: (String) -> Unit,
    gainTempHP: (String) -> Unit,
    onTemporaryHPChanged: (String) -> Unit,
    onProficiencyBonusChanged: (String) -> Unit,
    onArmorClassChanged: (String) -> Unit,
    onInitiativeChanged: (String) -> Unit,
    onInspirationChanged: (Boolean) -> Unit,
    onBaseSpeedChanged: (String) -> Unit,
    onClimbSpeedChanged: (String) -> Unit,
    onFlySpeedChanged: (String) -> Unit,
    onSwimSpeedChanged: (String) -> Unit,
    onBurrowSpeedChanged: (String) -> Unit,
    onAbilityScoreChanged: (Ability, Int?) -> Unit,
    onToggleProficiency: (Proficiency) -> Unit,
    onToggleExpertise: (Proficiency) -> Unit
) {
    val tabs = listOf(
        TabData(
            text = FormattedResource(R.string.character_description_tab),
            content = {
                CharacterDescriptionTab(
                    characterState = characterState,
                    showCustomDialog = showCustomDialog,
                    hideDialog = hideDialog,
                    inEditMode = viewState.inEditMode,
                    onNameChanged = onNameChanged,
                    onPersonalityTraitsChanged = onPersonalityTraitsChanged,
                    onIdealsChanged = onIdealsChanged,
                    onBondsChanged = onBondsChanged,
                    onFlawsChanged = onFlawsChanged
                )
            }
        ),
        TabData(
            text = FormattedResource(R.string.character_combat_tab),
            content = {
                CharacterCombatTab(
                    characterState = characterState,
                    viewState = viewState,
                    showCustomDialog = showCustomDialog,
                    hideDialog = hideDialog,
                    onCurrentHPChanged = onCurrentHPChanged,
                    onMaxHPChanged = onMaxHPChanged,
                    onTemporaryHPChanged = onTemporaryHPChanged,
                    onDeathSaveFailuresChanged = onDeathSaveFailuresChanged,
                    onDeathSaveSuccessesChanged = onDeathSaveSuccessesChanged,
                    takeDamage = takeDamage,
                    heal = heal,
                    gainTempHP = gainTempHP,
                    onProficiencyBonusChanged = onProficiencyBonusChanged,
                    onArmorClassChanged = onArmorClassChanged,
                    onInitiativeChanged = onInitiativeChanged,
                    onInspirationChanged = onInspirationChanged,
                    onBaseSpeedChanged = onBaseSpeedChanged,
                    onClimbSpeedChanged = onClimbSpeedChanged,
                    onFlySpeedChanged = onFlySpeedChanged,
                    onSwimSpeedChanged = onSwimSpeedChanged,
                    onBurrowSpeedChanged = onBurrowSpeedChanged
                )
            }
        ),
        TabData(
            text = FormattedResource(R.string.character_stats_tab),
            content = {
                CharacterStatsTab(
                    characterState = characterState,
                    inEditMode = viewState.inEditMode,
                    onAbilityScoreChanged = onAbilityScoreChanged
                )
            }
        ),
        TabData(
            text = FormattedResource(R.string.character_skills_tab),
            content = {
                CharacterSkillsTab(
                    characterState = characterState,
                    inEditMode = viewState.inEditMode,
                    onToggleProficiency = onToggleProficiency,
                    onToggleExpertise = onToggleExpertise
                )
            }
        )
    )
    TabScreenLayout(
        tabData = tabs,
        scrollable = true,
        contentPadding = Dimens.Spacing.md,
        indicatorColor = MaterialTheme.colors.primaryVariant
    )
}

/****************************************** Previews **********************************************/

@ExperimentalFoundationApi
@FlowPreview
@ExperimentalPagerApi
@ExperimentalMaterialApi
@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CharacterDetailsScreenPreview() {
    PreviewSurface {
        CharacterDetailsScreenContent(
            characterState = State.Success(DEFAULT_CHARACTERS[0]),
            viewState = CharacterDetailsVM.ViewState(),
            showCustomDialog = {},
            hideDialog = {},
            onNameChanged = {},
            onPersonalityTraitsChanged = {},
            onIdealsChanged = {},
            onBondsChanged = {},
            onFlawsChanged = {},
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
            onBurrowSpeedChanged = {},
            onAbilityScoreChanged = {_,_ ->},
            onToggleProficiency = {},
            onToggleExpertise = {}
        )
    }
}