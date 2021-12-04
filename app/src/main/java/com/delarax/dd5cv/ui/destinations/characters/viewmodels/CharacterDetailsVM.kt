package com.delarax.dd5cv.ui.destinations.characters.viewmodels

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterDetailsVM @Inject constructor(
    private val characterRepo: CharacterRepo
) : ViewModel() {

    var viewState by mutableStateOf(ViewState())
        private set

    data class ViewState(
        val characterState: State<Character> = Loading(),
        val inEditMode: Boolean = false
    ) {
        val isEditModeEnabled: Boolean = characterState is Success
    }

    fun fetchCharacterById(id: String?) {
        id?.let {
            if (id != viewState.characterState.getOrNull()?.id) {
                viewModelScope.launch {
                    viewState = viewState.copy(
                        characterState = characterRepo.getCharacterById(id)
                    )
                }
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

    /**************************************** Scaffold ********************************************/

    private fun turnOnEditMode() { viewState = viewState.copy(inEditMode = true) }

    private fun turnOffEditMode() { viewState = viewState.copy(inEditMode = false) }

    fun submitChanges() {
        // TODO
    }

    fun cancelChanges() {
        // TODO
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
            onClick = onBackPress
        ),
        actionMenu = when {
            !viewState.isEditModeEnabled -> { listOf() }
            viewState.inEditMode -> {
                listOf(
                    ActionItem(
                        name = FormattedResource(R.string.action_item_turn_off_edit_mode),
                        icon = Icons.Default.Done,
                        onClick = { turnOffEditMode() }
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
}