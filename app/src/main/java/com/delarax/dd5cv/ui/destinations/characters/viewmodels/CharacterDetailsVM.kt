package com.delarax.dd5cv.ui.destinations.characters.viewmodels

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import com.delarax.dd5cv.models.CacheType
import com.delarax.dd5cv.models.FormattedResource
import com.delarax.dd5cv.models.State
import com.delarax.dd5cv.models.State.Loading
import com.delarax.dd5cv.models.State.Success
import com.delarax.dd5cv.models.characters.Character
import com.delarax.dd5cv.models.ui.ButtonData
import com.delarax.dd5cv.models.ui.ScaffoldState
import com.delarax.dd5cv.ui.AppStateActions
import com.delarax.dd5cv.ui.components.ActionItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class CharacterDetailsVM @Inject constructor(
    private val characterRepo: CharacterRepo,
    private val appStateActions: AppStateActions
) : ViewModel() {

    var viewState by mutableStateOf(ViewState())
        private set

    data class ViewState(
        val characterState: State<Character> = Loading(),
        val inProgressCharacterId: String? = null
    ) {
        val inEditMode: Boolean = !inProgressCharacterId.isNullOrEmpty()
        val isEditModeEnabled: Boolean = characterState is Success
    }

    init {
        viewModelScope.launch {
            characterRepo.inProgressCharacterIdFlow.collect {
                viewState = viewState.copy(inProgressCharacterId = it)
            }
        }
    }

    fun asyncInit(characterId: String?) {
        if (characterId != null) {
            if (characterId == viewState.inProgressCharacterId) {
                cacheManager.loadEdits(characterId)
            } else {
                remoteDataManager.getCharacterById(characterId)
                cacheManager.clear()
            }
        }
    }

    private fun updateCharacterState(newState: State<Character>) {
        viewState = viewState.copy(characterState = newState)
    }

    private fun updateCharacterDataIfPresent(mapper: (Character) -> Character) {
        viewState = viewState.copy(
            characterState = viewState.characterState.mapSuccess(mapper)
        )
    }

    private val remoteDataManager = object {
        fun getCharacterById(id: String) {
            viewModelScope.launch {
                updateCharacterState(characterRepo.getCharacterById(id))
            }
        }
    }

    private val cacheManager = object {
        fun saveBackup() = cacheCharacter(CacheType.BACKUP)
        fun saveEdits() = cacheCharacter(CacheType.EDITS)

        private fun cacheCharacter(type: CacheType) {
            viewState.characterState.getOrNull()?.let {
                viewModelScope.launch {
                    characterRepo.cacheCharacter(it, type)
                }
            }
        }
        fun loadEdits(id: String) = viewModelScope.launch {
            updateCharacterState(
                characterRepo.getCachedCharacterById(id, CacheType.BACKUP)
            )
        }
        fun loadBackupAndClear(id: String) = viewModelScope.launch {
            updateCharacterState(
                characterRepo.getCachedCharacterById(id, CacheType.BACKUP)
            )
            characterRepo.clearCache()
        }

        fun clear() {
            viewModelScope.launch {
                characterRepo.clearCache()
            }
        }
    }

    private fun beginEditing() {
        cacheManager.saveBackup()
        cacheManager.saveEdits()
    }

    private fun submitEdits() {
        // TODO: show loading indicator
        viewState.characterState.getOrNull()?.let {
            runBlocking {
                viewModelScope.launch {
                    val result = characterRepo.updateCharacter(it)
                    if (result is Success) {
                        cacheManager.clear()
                        characterRepo.fetchAllCharacterSummaries()
                    } else {
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
        // TODO: remove loading indicator
    }

    private fun cancelEdits() {
        // TODO: show loading indicator
            cacheManager.loadBackupAndClear(viewState.characterState.getOrNull()!!.id)
        // TODO: remove loading indicator
    }

    private fun showCancelEditsDialog() {
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
                    cancelEdits()
                }
            ),
            secondaryAction = ButtonData(
                text = FormattedResource(R.string.no),
                onClick = { appStateActions.hideDialog() }
            ),
        )
    }

    fun updateScaffoldState(navBack: () -> Unit) = appStateActions.updateScaffold(
        ScaffoldState(
            title = viewState.characterState.getOrNull()?.let {
                it.name?.let { name ->
                    FormattedResource(
                        resId = R.string.single_arg,
                        values = listOf(name)
                    )
                } ?: FormattedResource(R.string.default_character_name)

            } ?: FormattedResource(R.string.destination_characters_title),
            leftActionItem = ActionItem(
                name = FormattedResource(R.string.action_item_back),
                icon = Icons.Default.ArrowBack,
                onClick = navBack // TODO: handle pressing back button when in edit mode
            ),
            actionMenu = when {
                !viewState.isEditModeEnabled -> { listOf() }
                viewState.inEditMode -> {
                    listOf(
                        ActionItem(
                            name = FormattedResource(R.string.action_item_cancel_edits),
                            icon = Icons.Default.Clear,
                            onClick = { showCancelEditsDialog() }
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
            }
        )
    )

    fun saveEdits() = cacheManager.saveEdits()

    fun updateName(name: String) = updateCharacterDataIfPresent {
        it.copy(name = name)
    }
}