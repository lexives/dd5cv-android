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
import com.delarax.dd5cv.models.FormattedResource
import com.delarax.dd5cv.models.State
import com.delarax.dd5cv.models.State.Loading
import com.delarax.dd5cv.models.State.Success
import com.delarax.dd5cv.models.characters.Character
import com.delarax.dd5cv.models.navigation.CustomScaffoldState
import com.delarax.dd5cv.ui.components.ActionItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
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

    fun asyncInit(characterId: String?) {
        viewModelScope.launch {
            characterRepo.characterFlow
//                .filter { it.first == characterId }
                .collect {
                    updateCharacterState(it.second, it.first)
                }
        }
        fetchCharacterById(characterId)
        // TODO: maybe save to cache?
    }

    private fun updateCharacterState(newState: State<Character>, id: String) {
        viewState = viewState.copy(characterState = newState)
    }

    private fun fetchCharacterById(id: String?) {
        // TODO: maybe check cache first?
        id?.let {
            if (id != viewState.characterState.getOrNull()?.id) {
                remoteStorageManager.fetchCharacterById(id)
            }
        }
    }

    fun updateName(name: String) {
        (viewState.characterState as? Success)?.value?.let { character ->
            viewState = viewState.copy(
                characterState = Success(
                    character.copy(
                        name = name
                    )
                )
            )
        }
    }

    private val remoteStorageManager = object {
        fun fetchCharacterById(id: String) {
            viewModelScope.launch {
                characterRepo.fetchCharacterById(id)
            }
        }

        fun updateCharacter() {
            viewState.characterState.getOrNull()?.let {
                viewModelScope.launch {
//                    remoteCharacterDataSource.updateCharacter(it)
                }
            }
        }
    }

    private val localStorageManager = object {
        fun insertCharacter() {
            viewState.characterState.getOrNull()?.let {
                viewModelScope.launch {
//                    characterDatabaseRepo.insertCharacter(it)
                }
            }
        }
        fun updateCharacter() {
            viewState.characterState.getOrNull()?.let {
                viewModelScope.launch {
//                    characterDatabaseRepo.updateCharacter(it)
                }
            }
        }
        fun deleteAllCharacters() {
            viewModelScope.launch {
//                characterDatabaseRepo.deleteAll()
            }
        }
        fun handleAppShutdown() {
            if (viewState.inEditMode) {
                updateCharacter()
            } else {
                deleteAllCharacters()
            }
        }
    }

    /**************************************** Scaffold ********************************************/

    private fun turnOnEditMode() {
        localStorageManager.insertCharacter()
        viewState = viewState.copy(inEditMode = true)
    }

    private fun submitChanges() {
        // Submit changes to server and clear edits
        remoteStorageManager.updateCharacter()
        localStorageManager.deleteAllCharacters()
    }

    private fun cancelChanges() {
        // TODO: are you sure you want to cancel?

        // Re-load character data from server and clear edits
        viewState.characterState.getOrNull()?.let {
            remoteStorageManager.fetchCharacterById(it.id)
        }
        localStorageManager.deleteAllCharacters()
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

    // TODO: this needs testing
    override fun onCleared() {
        super.onCleared()
        localStorageManager.handleAppShutdown()
    }


    // TODO: this needs testing
    override fun uncaughtException(t: Thread, e: Throwable) {
        localStorageManager.handleAppShutdown()
    }
}