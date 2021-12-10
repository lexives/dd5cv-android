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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class CharacterDetailsVM @Inject constructor(
    private val characterRepo: CharacterRepo
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
            if (characterId == viewState.inProgressCharacterId) {
                cacheManager.loadEdits(characterId)
            } else {
                remoteDataManager.getCharacterById(characterId)
                cacheManager.clear()
            }
        }
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
        fun loadBackup(id: String) = loadCharacter(id, CacheType.BACKUP)
        fun loadEdits(id: String) = loadCharacter(id, CacheType.EDITS)
        fun clear() {
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
        fun loadCharacter(id: String, type: CacheType) {
            viewModelScope.launch {
                updateCharacterState(
                    characterRepo.getCachedCharacterById(id, type)
                )
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
                        TODO("show popup that there was an error")
                    }
                }
            }
        }
        // TODO: remove loading indicator
    }

    private fun cancelEdits() {
        // TODO: show popup to confirm the cancel
        // TODO: show loading indicator
        viewModelScope.launch {
            cacheManager.loadBackup(viewState.characterState.getOrNull()!!.id)
        }
        cacheManager.clear()
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
                        onClick = { cancelEdits() }
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

    fun saveEdits() = cacheManager.saveEdits()

    fun updateName(name: String) = updateCharacterDataIfPresent {
        it.copy(name = name)
    }
}