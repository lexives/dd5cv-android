package com.delarax.dd5cv.ui.characters

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.delarax.dd5cv.data.characters.CharacterRepo
import com.delarax.dd5cv.models.characters.Character
import com.delarax.dd5cv.models.characters.CharacterSummary
import com.delarax.dd5cv.utils.State
import com.delarax.dd5cv.utils.State.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class CharacterListVM @Inject constructor(
    private val characterRepo: CharacterRepo
): ViewModel() {

    // public state
    var characterListState: State<List<CharacterSummary>> by mutableStateOf(Loading(0))
        private set

    init {
        refreshCharacters()
    }

    fun createNewCharacter(goToCharacterDetails: (String) -> Unit) {
        runBlocking {
            val newCharacter = Character(name = "New Character")
            val job = viewModelScope.launch {
                characterRepo.addCharacter(newCharacter)
            }
            job.join()
            refreshCharacters()
            goToCharacterDetails(newCharacter.id)
        }
    }

    private fun refreshCharacters() = viewModelScope.launch {
        characterListState = characterRepo.getAllCharacterSummaries()
    }
}