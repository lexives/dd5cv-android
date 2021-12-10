package com.delarax.dd5cv.ui.destinations.characters.viewmodels

import androidx.compose.material.icons.Icons
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
import com.delarax.dd5cv.models.characters.Character
import com.delarax.dd5cv.models.characters.CharacterSummary
import com.delarax.dd5cv.models.navigation.CustomScaffoldState
import com.delarax.dd5cv.models.navigation.FloatingActionButtonState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class CharacterListVM @Inject constructor(
    private val characterRepo: CharacterRepo
): ViewModel() {
    var characterListState: State<List<CharacterSummary>> by mutableStateOf(Loading(0))
        private set

    var viewState by mutableStateOf(ViewState())
        private set

    init {
        viewModelScope.launch {
            characterRepo.inProgressCharacterIdFlow.collect { inProgressCharacterId ->
                // TODO: show popup asking to resume edits
                updateInProgressCharacterId(inProgressCharacterId)
            }
        }
        viewModelScope.launch {
            characterRepo.allSummariesFlow.collect {
                updateCharacterListState(it)
            }
        }
        refreshCharacters()
    }

    data class ViewState(
        val inProgressCharacterId: String? = null
    )

    private fun createNewCharacter(goToCharacterDetails: (String) -> Unit) {
        runBlocking {
            val newCharacter = Character(name = "New Character")
            viewModelScope.launch {
                characterRepo.addCharacter(newCharacter)
            }.join() // join blocks the main thread until this coroutine has completed
            goToCharacterDetails(newCharacter.id)
            refreshCharacters()
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

    /**************************************** Scaffold ********************************************/

    fun provideCustomScaffoldState(
        goToCharacterDetails: (String) -> Unit
    ) = CustomScaffoldState(
        title = FormattedResource(R.string.destination_characters_title),
        floatingActionButtonState = FloatingActionButtonState(
            icon = Icons.Default.Edit,
            contentDescription = FormattedResource(R.string.add_character_content_desc),
            onClick = {
                createNewCharacter(goToCharacterDetails = goToCharacterDetails)
            }
        )
    )

    fun resumeEdits(goToCharacterDetails: (String) -> Unit) {
        val characterId = viewState.inProgressCharacterId!!
        goToCharacterDetails(characterId)
    }
}