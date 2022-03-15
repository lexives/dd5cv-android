package com.delarax.dd5cv.ui.destinations.characters.viewmodels

import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.delarax.dd5cv.R
import com.delarax.dd5cv.data.characters.CharacterCache
import com.delarax.dd5cv.data.characters.CharacterRepo
import com.delarax.dd5cv.extensions.mutate
import com.delarax.dd5cv.extensions.toIntOrZero
import com.delarax.dd5cv.extensions.toStringOrEmpty
import com.delarax.dd5cv.models.characters.Ability
import com.delarax.dd5cv.models.characters.Character
import com.delarax.dd5cv.models.characters.DeathSave
import com.delarax.dd5cv.models.characters.Proficiency
import com.delarax.dd5cv.models.characters.ProficiencyLevel.EXPERT
import com.delarax.dd5cv.models.characters.ProficiencyLevel.NONE
import com.delarax.dd5cv.models.characters.ProficiencyLevel.PROFICIENT
import com.delarax.dd5cv.models.data.CacheType
import com.delarax.dd5cv.models.data.State
import com.delarax.dd5cv.models.data.State.Loading
import com.delarax.dd5cv.models.data.State.Success
import com.delarax.dd5cv.models.ui.ButtonData
import com.delarax.dd5cv.models.ui.DialogData
import com.delarax.dd5cv.models.ui.FormattedResource
import com.delarax.dd5cv.models.ui.ScaffoldState
import com.delarax.dd5cv.ui.AppStateActions
import com.delarax.dd5cv.ui.components.toppappbar.ActionItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import javax.inject.Inject

@FlowPreview
@HiltViewModel
class CharacterDetailsVM @Inject constructor(
    private val characterRepo: CharacterRepo,
    private val characterCache: CharacterCache,
    private val appStateActions: AppStateActions
) : ViewModel() {

    /**
     * Public view state and character state
     */
    var viewState by mutableStateOf(ViewState())
        private set

    // Represents the current character data on the screen. This means that this will change
    // whenever the user edits the sheet, but also when we reload character data from either
    // the remote data store or the cache.
    private val _characterStateFlow = MutableStateFlow<State<Character>>(Loading())
    val characterStateFlow = _characterStateFlow

    data class ViewState(
        val inEditMode: Boolean = false,
        val isEditModeEnabled: Boolean = false,
        val initiativeString: String = ""
    )

    /**
     * Init and async init
     */
    init {
        viewModelScope.launch {
            characterRepo.characterFlow.collect {
                updateCharacterState(it.second)
                viewState = viewState.copy(isEditModeEnabled = it.second is Success)
            }
        }
        viewModelScope.launch {
            _characterStateFlow.debounce(3000).collect {
                if (viewState.inEditMode) {
                    saveEdits()
                }
            }
        }
    }

    fun asyncInit(characterId: String?) {
        if (characterId != null) {
            viewModelScope.launch {
                val inProgressCharacterId = characterCache.inProgressCharacterIdFlow.value
                if (characterId == inProgressCharacterId) {
                    loadEdits(characterId)
                    viewState = viewState.copy(inEditMode = true)
                } else {
                    characterCache.clear()
                    characterRepo.fetchCharacterById(characterId)
                }
            }
        }
    }

    /**
     * Private functions that update view state
     */
    private fun updateCharacterState(newState: State<Character>) {
        _characterStateFlow.value = newState
        (newState as? Success)?.value?.let {
            viewState = viewState.copy(
                initiativeString = it.initiativeOverride.toStringOrEmpty(),
            )
        }
    }

    private fun updateCharacterDataIfPresent(mapper: (Character) -> Character) {
        _characterStateFlow.value = _characterStateFlow.value.mapSuccess(mapper)
    }

    private fun updateAbilityScoreIfPresent(ability: Ability, newValue: Int?) {
        _characterStateFlow.value.getOrNull()?.abilityScores?.let { abilityScores ->
            // Make sure that the ability score exists
            if (abilityScores.containsKey(ability)) {
                val newAbilityScores = abilityScores.mutate {
                    this[ability] = newValue
                }
                updateCharacterDataIfPresent { it.copy(abilityScores = newAbilityScores) }
            }
        }
    }

    private fun updateSavingThrowIfPresent(index: Int, newValue: Proficiency) {
        _characterStateFlow.value.getOrNull()?.savingThrows?.let { savingThrows ->
            // Make sure that there's an element at the specified index
            if (savingThrows.getOrNull(index) !== null) {
                val newSavingThrows = savingThrows.mutate {
                    this[index] = newValue
                }
                updateCharacterDataIfPresent { it.copy(savingThrows = newSavingThrows) }
            }
        }
    }

    private fun updateSkillIfPresent(index: Int, newValue: Proficiency) {
        _characterStateFlow.value.getOrNull()?.skills?.let { skills ->
            // Make sure that there's an element at the specified index
            if (skills.getOrNull(index) !== null) {
                val newSkillList = skills.mutate {
                    this[index] = newValue
                }
                updateCharacterDataIfPresent { it.copy(skills = newSkillList) }
            }
        }
    }

    /**
     * Private functions to handle remote data storage
     */
    // This function is necessary to save any changes that weren't made while in edit mode. The
    // default implementation will do nothing on success and show a generic error message on error.
    private fun submitChangesToRemoteStorage(
        onSuccess: () -> Unit = {},
        onError: (FormattedResource) -> Unit = {
            viewModelScope.launch {
                appStateActions.showToast(it, Toast.LENGTH_SHORT)
            }
        },
        errorMessage: FormattedResource? = null
    ) {
        (_characterStateFlow.value as? Success)?.let {
            viewModelScope.launch {
                val result = characterRepo.updateCharacter(it.value)
                if (result is Success) {
                    onSuccess()
                } else if (result is State.Error) {
                    val message = errorMessage
                        ?: FormattedResource(R.string.save_character_default_error_message)
                    onError(message)
                    // TODO: allow retry button
                }
            }
        } ?: onError(FormattedResource(R.string.no_character_to_save_error_message))
    }

    /**
     * Private functions to handle groups of cache operations that need to happen in order
     */
     // There's more logic around this function because it gets called every few seconds
     // while the user is in edit mode.
    private fun saveEdits() {
        _characterStateFlow.value.getOrNull()?.let {
            viewModelScope.launch {
                val edits = characterCache.getCharacterById(it.id, CacheType.EDITS)
                // We don't need to save the current edits again if they match the saved edits
                if (it != edits.getOrNull()) {
                    val backup = characterCache.getCharacterById(it.id, CacheType.BACKUP)
                    if (it == backup.getOrNull()) {
                        // If the current edits are the same as the saved backup then we can
                        // delete the saved edits, if any
                        characterCache.deleteCharacterById(it.id, CacheType.EDITS)
                    } else {
                        // Otherwise, save current edits
                        characterCache.cacheCharacter(it, CacheType.EDITS)
                    }
                }
            }
        }
    }
    private fun loadEdits(id: String) = viewModelScope.launch {
        updateCharacterState(
            characterCache.getCharacterById(id, CacheType.EDITS)
        )
        viewState = viewState.copy(isEditModeEnabled = _characterStateFlow.value is Success)
    }

    /**
     * Private functions for turning edit mode on and off
     */
    private fun beginEditing() {
        _characterStateFlow.value.getOrNull()?.let {
            viewModelScope.launch {
                // attempt to save a backup
                val result = characterCache.cacheCharacter(it, CacheType.BACKUP)
                if (result is State.Error) {
                    appStateActions.showToast(
                        message = FormattedResource(R.string.begin_editing_error_message),
                        duration = Toast.LENGTH_SHORT
                    )
                } else if (result is Success) {
                    viewState = viewState.copy(inEditMode = true)
                }
            }
        }
    }

    private fun submitEdits() {
        _characterStateFlow.value.getOrNull()?.let {
            // TODO: remove empty strings from lists like notes

            viewModelScope.launch {
                appStateActions.showLoadingIndicator()
                val result = characterRepo.updateCharacter(it)
                if (result is Success) {
                    characterCache.clear()
                    characterRepo.fetchAllCharacterSummaries()
                    viewState = viewState.copy(inEditMode = false)
                    appStateActions.hideLoadingIndicator()
                } else {
                    appStateActions.hideLoadingIndicator()
                    appStateActions.showDialog(
                        DialogData.MessageDialog(
                            title = FormattedResource(
                                R.string.submit_character_edits_error_dialog_title
                            ),
                            message = FormattedResource(
                                R.string.submit_character_edits_error_dialog_message
                            ),
                            mainAction = ButtonData(
                                text = FormattedResource(R.string.close),
                                onClick = { appStateActions.hideDialog() }
                            )
                        ) { appStateActions.hideDialog() }
                    )
                }
            }
        }
    }

    private fun cancelEditsOrShowDialog(navBack: (() -> Unit)? = null) {
        viewModelScope.launch {
            _characterStateFlow.value.getOrNull()?.let {
                val backup = characterCache.getCharacterById(it.id, CacheType.BACKUP)
                if (backup is Success && backup.value == it) {
                    cancelEdits(showLoading = false)
                    navBack?.invoke()
                } else {
                    showCancelEditsDialog(navBack)
                }
            }
        }
    }

    private suspend fun cancelEdits(showLoading: Boolean) {
        if (showLoading)  { appStateActions.showLoadingIndicator() }
        updateCharacterState(
            characterCache.getCharacterById(
                _characterStateFlow.value.getOrNull()!!.id,
                CacheType.BACKUP
            )
        )
        characterCache.clear()
        viewState = viewState.copy(inEditMode = false)
        if (showLoading)  { appStateActions.hideLoadingIndicator() }
    }

    private fun showCancelEditsDialog(navBack: (() -> Unit)? = null) {
        appStateActions.showDialog(
            DialogData.MessageDialog(
                title = FormattedResource(R.string.cancel_edits_dialog_title),
                message = FormattedResource(R.string.cancel_edits_dialog_message),
                mainAction = ButtonData(
                    text = FormattedResource(R.string.yes),
                    onClick = {
                        appStateActions.hideDialog()
                        viewModelScope.launch {
                            cancelEdits(showLoading = true)
                        }.invokeOnCompletion {
                            navBack?.invoke()
                        }
                    }
                ),
                secondaryAction = ButtonData(
                    text = FormattedResource(R.string.no),
                    onClick = { appStateActions.hideDialog() }
                ),
            )
        )
    }

    /**
     * Public functions to set external UI elements
     */
    fun updateScaffoldState(navBack: () -> Unit) = appStateActions.updateScaffold(
        ScaffoldState(
            title = _characterStateFlow.value.getOrNull()?.let {
                if (it.name.isEmpty()) {
                    FormattedResource(R.string.default_character_name)
                } else {
                    FormattedResource(
                        resId = R.string.single_arg,
                        values = listOf(it.name)
                    )
                }
            } ?: FormattedResource(R.string.destination_characters_title),
            subtitle = _characterStateFlow.value.getOrNull()?.let {
                FormattedResource(
                    resId = R.string.character_details_screen_subtitle,
                    values = listOf(it.totalLevel, it.classNamesString)
                )
            },
            actionMenu = when {
                !viewState.isEditModeEnabled -> { listOf() }
                viewState.inEditMode -> {
                    listOf(
                        ActionItem(
                            name = FormattedResource(R.string.action_item_cancel_edits),
                            icon = Icons.Default.Clear,
                            onClick = { cancelEditsOrShowDialog() }
                        ),
                        ActionItem(
                            name = FormattedResource(R.string.action_item_confirm_edits),
                            icon = Icons.Default.Done,
                            onClick = { submitEdits() }
                        )
                    )
                }
                else -> {
                    listOf(
                        ActionItem(
                            name = FormattedResource(R.string.action_item_turn_on_edit_mode),
                            icon = Icons.Default.Edit,
                            onClick = { beginEditing() }
                        )
                    )
                }
            },
            onBackPressed = if (viewState.inEditMode) {
                { cancelEditsOrShowDialog(navBack) }
            } else null
        )
    )

    fun showCustomDialog(customDialog: DialogData.CustomDialog) {
        appStateActions.showDialog(customDialog)
    }

    fun hideDialog() {
        appStateActions.hideDialog()
    }

    /**
     * Public functions to update character view state
     */
    fun updateName(name: String) = updateCharacterDataIfPresent {
        it.copy(name = name)
    }

    fun updatePersonalityTraits(personalityTraits: List<String>) = updateCharacterDataIfPresent {
        it.copy(personalityTraits = personalityTraits)
    }

    fun updateIdeals(ideals: List<String>) = updateCharacterDataIfPresent {
        it.copy(ideals = ideals)
    }

    fun updateBonds(bonds: List<String>) = updateCharacterDataIfPresent {
        it.copy(bonds = bonds)
    }

    fun updateFlaws(flaws: List<String>) = updateCharacterDataIfPresent {
        it.copy(flaws = flaws)
    }

    fun updateCurrentHP(currentHPString: String) = updateCharacterDataIfPresent {
        it.copy(currentHP = currentHPString.toIntOrNull())
    }

    fun updateMaxHP(maxHPString: String) = updateCharacterDataIfPresent {
        it.copy(maxHP = maxHPString.toIntOrNull())
    }

    fun updateTemporaryHP(temporaryHPString: String) = updateCharacterDataIfPresent {
        it.copy(temporaryHP = temporaryHPString.toIntOrNull())
    }

    fun updateDeathSaveFailures(failures: DeathSave) {
        updateCharacterDataIfPresent {
            it.copy(deathSaveFailures = failures)
        }
        if (!viewState.inEditMode) { submitChangesToRemoteStorage() }
    }

    fun updateDeathSaveSuccesses(successes: DeathSave) {
        updateCharacterDataIfPresent {
            it.copy(deathSaveSuccesses = successes)
        }
        if (!viewState.inEditMode) { submitChangesToRemoteStorage() }
    }

    fun takeDamage(damageString: String) {
        _characterStateFlow.value = _characterStateFlow.value.mapSuccess { character ->
            val currentHP: Int = character.currentHP ?: 0
            val tempHP: Int = character.temporaryHP ?: 0

            val damage: Int = damageString.toIntOrZero()
            val newTempHP: Int = maxOf(tempHP - damage, 0)
            val leftoverDamageAfterTemp: Int = damage - (tempHP - newTempHP)
            val newCurrentHP = maxOf(currentHP - leftoverDamageAfterTemp, 0)

            character.copy(
                temporaryHP = if (character.temporaryHP == null && newTempHP == 0) {
                    null
                } else { newTempHP },
                currentHP = if (character.currentHP == null && newCurrentHP == 0) {
                    null
                } else newCurrentHP
            )
        }
        if (!viewState.inEditMode) { submitChangesToRemoteStorage() }
    }

    fun heal(hpToHealString: String) {
        _characterStateFlow.value = _characterStateFlow.value.mapSuccess { character ->
            val currentHP: Int = character.currentHP ?: 0
            val maxHP: Int = character.maxHP ?: 0

            val hpToHeal: Int = hpToHealString.toIntOrZero()
            val newCurrentHP: Int = minOf(currentHP + hpToHeal, maxHP)

            character.copy(
                currentHP = if (character.currentHP == null && newCurrentHP == 0) {
                    null
                } else newCurrentHP,
                deathSaveFailures = DeathSave.None,
                deathSaveSuccesses = DeathSave.None,
            )
        }
        if (!viewState.inEditMode) { submitChangesToRemoteStorage() }
    }

    fun gainTempHP(tempHPToGainString: String) {
        _characterStateFlow.value = _characterStateFlow.value.mapSuccess { character ->
            val tempHP: Int = character.temporaryHP ?: 0
            val tempHPToGain: Int = tempHPToGainString.toIntOrZero()
            val newTempHP = tempHP + tempHPToGain

            character.copy(temporaryHP = newTempHP)
        }
        if (!viewState.inEditMode) { submitChangesToRemoteStorage() }
    }

    fun updateProficiencyBonus(proficiencyBonusString: String) = updateCharacterDataIfPresent {
        it.copy(proficiencyBonusOverride = proficiencyBonusString.toIntOrNull())
    }

    fun updateArmorClass(armorClassString: String) = updateCharacterDataIfPresent {
        it.copy(armorClassOverride = armorClassString.toIntOrNull())
    }

    fun updateInitiative(initiativeString: String) {
        viewState = viewState.copy(initiativeString = initiativeString)
        updateCharacterDataIfPresent {
            it.copy(initiativeOverride = initiativeString.toIntOrNull())
        }
    }

    fun updateInspiration(inspiration: Boolean) {
        updateCharacterDataIfPresent {
            it.copy(inspiration = inspiration)
        }
        if (!viewState.inEditMode) { submitChangesToRemoteStorage() }
    }

    fun updateBaseSpeed(walkSpeed: String) = updateCharacterDataIfPresent {
        it.copy(speed = walkSpeed.toIntOrNull())
    }

    fun updateClimbSpeed(climbSpeed: String) = updateCharacterDataIfPresent {
        it.copy(climbSpeed = climbSpeed.toIntOrNull())
    }

    fun updateFlySpeed(flySpeed: String) = updateCharacterDataIfPresent {
        it.copy(flySpeed = flySpeed.toIntOrNull())
    }

    fun updateSwimSpeed(SwimSpeed: String) = updateCharacterDataIfPresent {
        it.copy(swimSpeed = SwimSpeed.toIntOrNull())
    }

    fun updateBurrowSpeed(burrowSpeed: String) = updateCharacterDataIfPresent {
        it.copy(burrowSpeed = burrowSpeed.toIntOrNull())
    }

    fun updateAbilityScore(ability: Ability, newScore: Int?) =
        updateAbilityScoreIfPresent(ability, newScore)

    fun toggleSavingThrowProficiency(savingThrow: Proficiency) {
        _characterStateFlow.value.getOrNull()?.savingThrows?.indexOf(savingThrow)?.let { index ->
            val newProficiencyLevel = if (savingThrow.proficiencyLevel == PROFICIENT) {
                NONE
            } else {
                PROFICIENT
            }
            updateSavingThrowIfPresent(
                index,
                savingThrow.copy(proficiencyLevel = newProficiencyLevel)
            )
        }
    }

    fun toggleSkillProficiency(skill: Proficiency) {
        _characterStateFlow.value.getOrNull()?.skills?.indexOf(skill)?.let { index ->
            val newProficiencyLevel = when (skill.proficiencyLevel) {
                PROFICIENT -> NONE
                NONE, EXPERT ->PROFICIENT
            }
            updateSkillIfPresent(index, skill.copy(proficiencyLevel = newProficiencyLevel))
        }
    }

    fun toggleSkillExpertise(skill: Proficiency) {
        _characterStateFlow.value.getOrNull()?.skills?.indexOf(skill)?.let { index ->
            val newProficiencyLevel = when (skill.proficiencyLevel) {
                NONE, PROFICIENT -> EXPERT
                EXPERT -> NONE
            }
            updateSkillIfPresent(index, skill.copy(proficiencyLevel = newProficiencyLevel))
        }
    }

    fun updateNotes(notes: List<String>) = updateCharacterDataIfPresent {
        it.copy(notes = notes)
    }
}