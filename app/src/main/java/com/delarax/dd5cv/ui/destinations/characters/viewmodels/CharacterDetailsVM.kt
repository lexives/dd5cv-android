package com.delarax.dd5cv.ui.destinations.characters.viewmodels

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
import com.delarax.dd5cv.data.characters.CharacterRepo
import com.delarax.dd5cv.extensions.toStringOrEmpty
import com.delarax.dd5cv.models.characters.Character
import com.delarax.dd5cv.models.data.CacheType
import com.delarax.dd5cv.models.data.State
import com.delarax.dd5cv.models.data.State.Loading
import com.delarax.dd5cv.models.data.State.Success
import com.delarax.dd5cv.models.ui.ButtonData
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
    private val appStateActions: AppStateActions
) : ViewModel() {

    var viewState by mutableStateOf(ViewState())
        private set

    private val _characterStateFlow = MutableStateFlow<State<Character>>(Loading())
    val characterStateFlow = _characterStateFlow

    data class ViewState(
        val inProgressCharacterId: String? = null,
        val isEditModeEnabled: Boolean = false,
        val initiativeString: String = ""
    ) {
        val inEditMode: Boolean = !inProgressCharacterId.isNullOrEmpty()
    }

    init {
        viewModelScope.launch {
            characterRepo.inProgressCharacterIdFlow.collect {
                viewState = viewState.copy(inProgressCharacterId = it)
            }
        }
        viewModelScope.launch {
            characterRepo.characterFlow.collect {
                updateCharacterState(it.second)
                updateIsEditModeEnabled(it.second is Success)
            }
        }
        viewModelScope.launch {
            _characterStateFlow.debounce(5000).collect {
                if (viewState.inEditMode) {
                    cacheManager.saveEdits()
                }
            }
        }
    }

    fun asyncInit(characterId: String?) {
        if (characterId != null) {
            if (characterId == viewState.inProgressCharacterId) {
                cacheManager.loadEdits(characterId)
            } else {
                remoteDataManager.fetchCharacterById(characterId)
                cacheManager.clear()
            }
        }
    }

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

    private fun updateIsEditModeEnabled(isEditModeEnabled: Boolean) {
        viewState = viewState.copy(isEditModeEnabled = isEditModeEnabled)
    }

    private val remoteDataManager = object {
        fun fetchCharacterById(id: String) {
            viewModelScope.launch {
                characterRepo.fetchCharacterById(id)
            }
        }
    }

    private val cacheManager = object {
        fun saveBackup() {
            _characterStateFlow.value.getOrNull()?.let {
                viewModelScope.launch {
                    characterRepo.cacheCharacter(it, CacheType.BACKUP)
                }
            }
        }
        /**
         * There's more logic around this function because it gets called every few seconds
         * while the user is in edit mode.
         */
        fun saveEdits() {
            _characterStateFlow.value.getOrNull()?.let {
                viewModelScope.launch {
                    val edits = characterRepo.getCachedCharacterById(it.id, CacheType.EDITS)
                    // We don't need to save the current edits again if they match the saved edits
                    if (it != edits.getOrNull()) {
                        val backup = characterRepo.getCachedCharacterById(it.id, CacheType.BACKUP)
                        if (it == backup.getOrNull()) {
                            // If the current edits are the same as the saved backup then we can
                            // delete the saved edits, if any
                            characterRepo.deleteCachedCharacterById(it.id, CacheType.EDITS)
                        } else {
                            // Otherwise, save current edits
                            characterRepo.cacheCharacter(it, CacheType.EDITS)
                        }
                    }
                }
            }
        }
        fun loadEdits(id: String) = viewModelScope.launch {
            updateCharacterState(
                characterRepo.getCachedCharacterById(id, CacheType.EDITS)
            )
        }
        fun clear() {
            viewModelScope.launch {
                characterRepo.clearCache()
            }
        }
    }

    private fun beginEditing() {
        cacheManager.saveBackup()
    }

    private fun submitEdits() {
        _characterStateFlow.value.getOrNull()?.let {
            viewModelScope.launch {
                appStateActions.showLoadingIndicator()
                val result = characterRepo.updateCharacter(it)
                if (result is Success) {
                    cacheManager.clear()
                    characterRepo.fetchAllCharacterSummaries()
                    appStateActions.hideLoadingIndicator()
                } else {
                    appStateActions.hideLoadingIndicator()
                    appStateActions.showDialog(
                        title = FormattedResource(
                            R.string.submit_character_edits_error_dialog_title
                        ),
                        message = FormattedResource(
                            R.string.submit_character_edits_error_dialog_message
                        ),
                        mainAction = ButtonData(
                            text = FormattedResource(R.string.close),
                            onClick = { appStateActions.hideDialog() }
                        ),
                        onDismissRequest = { appStateActions.hideDialog() }
                    )
                }
            }
        }
    }

    private suspend fun cancelEdits() {
        appStateActions.showLoadingIndicator()
        updateCharacterState(
            characterRepo.getCachedCharacterById(
                _characterStateFlow.value.getOrNull()!!.id,
                CacheType.BACKUP
            )
        )
        characterRepo.clearCache()
        appStateActions.hideLoadingIndicator()
    }

    private fun showCancelEditsDialog(navBack: (() -> Unit)? = null) {
        appStateActions.showDialog(
            title = FormattedResource(
                R.string.cancel_edits_dialog_title
            ),
            message = FormattedResource(
                R.string.cancel_edits_dialog_message
            ),
            mainAction = ButtonData(
                text = FormattedResource(R.string.yes),
                onClick = {
                    appStateActions.hideDialog()
                    viewModelScope.launch {
                        cancelEdits()
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
    }

    private fun cancelEditsOrShowDialog(navBack: (() -> Unit)? = null) {
        viewModelScope.launch {
            _characterStateFlow.value.getOrNull()?.let {
                val backup = characterRepo.getCachedCharacterById(it.id, CacheType.BACKUP)
                if (backup is Success && backup.value == it) {
                    cancelEdits()
                    navBack?.invoke()
                } else {
                    showCancelEditsDialog(navBack)
                }
            }
        }
    }

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

    fun updateName(name: String) = updateCharacterDataIfPresent {
        it.copy(name = name)
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

    fun updateProficiencyBonus(proficiencyBonusString: String) =updateCharacterDataIfPresent {
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
}