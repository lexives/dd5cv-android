package com.delarax.dd5cv.ui.destinations.characters.viewmodels

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.delarax.dd5cv.R
import com.delarax.dd5cv.data.characters.CharacterCache
import com.delarax.dd5cv.data.characters.CharacterRepo
import com.delarax.dd5cv.models.characters.Character
import com.delarax.dd5cv.models.characters.CharacterSummary
import com.delarax.dd5cv.models.data.State
import com.delarax.dd5cv.models.data.State.Loading
import com.delarax.dd5cv.models.ui.ButtonData
import com.delarax.dd5cv.models.ui.FloatingActionButtonState
import com.delarax.dd5cv.models.ui.FormattedResource
import com.delarax.dd5cv.models.ui.ScaffoldState
import com.delarax.dd5cv.ui.AppStateActions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterListVM @Inject constructor(
    private val characterRepo: CharacterRepo,
    private val characterCache: CharacterCache,
    private val appStateActions: AppStateActions
): ViewModel() {
    var characterListState: State<List<CharacterSummary>> by mutableStateOf(Loading(0))
        private set

    var viewState by mutableStateOf(ViewState())
        private set

    init {
        viewModelScope.launch {
            characterCache.inProgressCharacterIdFlow.collect { inProgressCharacterId ->
                updateInProgressCharacterId(inProgressCharacterId)
            }
        }
        viewModelScope.launch {
            characterRepo.allSummariesFlow.collect {
                updateCharacterListState(it)
            }
        }
    }

    fun asyncInit(goToCharacterDetails: (String) -> Unit) {
        refreshCharacters()
        if (viewState.inProgressCharacterId != null) {
            showResumeEditsDialog(goToCharacterDetails)
        }
    }

    data class ViewState(
        val inProgressCharacterId: String? = null
    )

    private fun createNewCharacter(navToCharacterDetails: (String) -> Unit) {
        val newCharacter = Character(name = "New Character")
        viewModelScope.launch {
            appStateActions.showLoadingIndicator()
            when (characterRepo.addCharacter(newCharacter)) {
                is State.Success -> {
                    appStateActions.hideLoadingIndicator()
                    navToCharacterDetails(newCharacter.id)
                }
                is State.Error -> {
                    appStateActions.hideLoadingIndicator()
                    appStateActions.showDialog(
                        title = FormattedResource(R.string.create_character_error_dialog_title),
                        message = FormattedResource(R.string.create_character_error_dialog_message),
                        mainAction = ButtonData(
                            text = FormattedResource(R.string.close),
                            onClick = { appStateActions.hideDialog() }
                        ),
                        onDismissRequest = { appStateActions.hideDialog() }
                    )
                }
                else -> {}
            }

        }
    }

    private fun updateCharacterListState(newState: State<List<CharacterSummary>>) {
        characterListState = newState
    }

    private fun refreshCharacters() = viewModelScope.launch {
        characterRepo.fetchAllCharacterSummaries()
    }

    private fun updateInProgressCharacterId(id: String?) {
        viewState = viewState.copy(inProgressCharacterId = id)
    }

    private fun resumeEdits(goToCharacterDetails: (String) -> Unit) {
        val characterId = viewState.inProgressCharacterId!!
        goToCharacterDetails(characterId)
    }

    private fun discardEdits() {
        viewModelScope.launch {
            appStateActions.showLoadingIndicator()
            characterCache.clear()
            appStateActions.hideLoadingIndicator()
        }
    }

    private fun showResumeEditsDialog(goToCharacterDetails: (String) -> Unit) {
        appStateActions.showDialog(
            title = FormattedResource(R.string.resume_edits_dialog_title),
            message = FormattedResource(R.string.resume_edits_dialog_message),
            mainAction = ButtonData(
                text = FormattedResource(R.string.resume),
                onClick = {
                    appStateActions.hideDialog()
                    resumeEdits(goToCharacterDetails)
                }
            ),
            secondaryAction = ButtonData(
                text = FormattedResource(R.string.discard),
                onClick = { showConfirmDiscardDialog(goToCharacterDetails) }
            )
        )
    }

    private fun showConfirmDiscardDialog(goToCharacterDetails: (String) -> Unit) {
        appStateActions.showDialog(
            title = FormattedResource(R.string.confirm_discard_dialog_title),
            message = FormattedResource(R.string.confirm_discard_dialog_message),
            mainAction = ButtonData(
                text = FormattedResource(R.string.yes),
                onClick = {
                    appStateActions.hideDialog()
                    discardEdits()
                }
            ),
            secondaryAction = ButtonData(
                text = FormattedResource(R.string.no),
                onClick = { showResumeEditsDialog(goToCharacterDetails) }
            )
        )
    }

    fun updateScaffoldState(
        navToCharacterDetails: (String) -> Unit
    ) = appStateActions.updateScaffold(
        ScaffoldState(
            title = FormattedResource(R.string.destination_characters_title),
            floatingActionButtonState = FloatingActionButtonState(
                icon = Icons.Default.Edit,
                contentDescription = FormattedResource(R.string.add_character_content_desc),
                onClick = {
                    createNewCharacter(navToCharacterDetails = navToCharacterDetails)
                }
            )
        )
    )



}