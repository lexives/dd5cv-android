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
import com.delarax.dd5cv.models.navigation.CustomScaffoldState
import com.delarax.dd5cv.ui.components.ActionItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterDetailsVM @Inject constructor(
    private val characterRepo: CharacterRepo
) : ViewModel(), Thread.UncaughtExceptionHandler {

    var viewState by mutableStateOf(ViewState())
        private set

    data class ViewState(
        val characterState: State<Character> = Loading(),
        val inEditMode: Boolean = false
    ) {
        val isEditModeEnabled: Boolean = characterState is Success
    }

    private fun updateCharacterState(newState: State<Character>) {
        viewState = viewState.copy(characterState = newState)
    }

    private fun updateCharacterDataIfPresent(mapper: (Character) -> Character) {
        viewState = viewState.copy(
            characterState = viewState.characterState.mapSuccess(mapper)
        )
    }

    fun asyncInit(characterId: String?) {
        if (characterId != null && characterId != viewState.characterState.getOrNull()?.id) {
            remoteDataManager.getCharacterById(characterId)
        }
    }

    private val remoteDataManager = object {
        fun getCharacterById(id: String) {
            viewModelScope.launch {
                updateCharacterState(characterRepo.getCharacterById(id))
            }
        }
    }

    private val localDataManager = object {
        fun saveBackup() = cacheCharacter(CacheType.BACKUP)
        fun saveEdits() = cacheCharacter(CacheType.EDITS)
        fun loadBackup() {
            viewModelScope.launch {
                viewState.characterState.getOrNull()?.let {
                    updateCharacterState(
                        characterRepo.getCachedCharacterById(it.id, CacheType.BACKUP)
                    )
                }
            }
        }
        fun clearCache() {
            viewModelScope.launch {
                characterRepo.clearCache()
            }
        }
        private fun cacheCharacter(type: CacheType) {
            viewState.characterState.getOrNull()?.let {
                viewModelScope.launch {
                    characterRepo.cacheCharacter(it, type)
                }
            }
        }
        fun handleAppShutdown() {
            if (viewState.inEditMode) {
                saveEdits()
                // TODO: save some indicator that the user has in-progress character edits
            }
        }
    }

    private fun turnOnEditMode() {
        localDataManager.saveBackup()
        viewState = viewState.copy(inEditMode = true)
    }

    private fun submitChanges() {
        // TODO: show loading indicator
        viewState.characterState.getOrNull()?.let {
            viewModelScope.launch {
                val result = characterRepo.updateCharacter(it)
                if (result is Success) {
                    viewState = viewState.copy(inEditMode = false)
                    localDataManager.clearCache()
                    characterRepo.fetchAllCharacterSummaries()
                } else {
                    TODO("show popup that there was an error")
                }
            }
        }
        // TODO: remove loading indicator
    }

    private fun cancelChanges() {
        // TODO: show popup to confirm the cancel
        // TODO: show loading indicator
        viewModelScope.launch {
            localDataManager.loadBackup()
            viewState = viewState.copy(inEditMode = false)
        }
        localDataManager.clearCache()
        // TODO: remove loading indicator
    }

    fun provideCustomScaffoldState(onBackPress: () -> Unit) = CustomScaffoldState(
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
            onClick = onBackPress // TODO: handle pressing back button when in edit mode
        ),
        actionMenu = when {
            !viewState.isEditModeEnabled -> { listOf() }
            viewState.inEditMode -> {
                listOf(
                    ActionItem(
                        name = FormattedResource(R.string.action_item_cancel_edits),
                        icon = Icons.Default.Clear,
                        onClick = { cancelChanges() }
                    ),
                    ActionItem(
                        name = FormattedResource(R.string.action_item_confirm_edits),
                        icon = Icons.Default.Done,
                        onClick = { submitChanges() }
                    )
                )
            }
            else -> {
                listOf(
                    ActionItem(
                        name = FormattedResource(R.string.action_item_turn_on_edit_mode),
                        icon = Icons.Default.Edit,
                        onClick = { turnOnEditMode() }
                    )
                )
            }
        }
    )

    fun updateName(name: String) {
        updateCharacterDataIfPresent {
            it.copy(name = name)
        }
    }

    // TODO: this needs testing -> DIDN"T WORK
    override fun onCleared() {
        super.onCleared()
        localDataManager.handleAppShutdown()
    }


    // TODO: this needs testing
    override fun uncaughtException(t: Thread, e: Throwable) {
        localDataManager.handleAppShutdown()
    }
}