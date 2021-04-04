package com.delarax.dd5cv.ui.characters

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.delarax.dd5cv.data.CharacterRepo
import com.delarax.dd5cv.models.Character
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterListVM @Inject constructor(
    private val characterRepo: CharacterRepo
): ViewModel() {

    // public state
    var characterList: List<Character> by mutableStateOf(listOf())
        private set

    init {
        viewModelScope.launch {
            // TODO: this seems too simple
            val characters = characterRepo.getAllCharacters()
            characterList = characters
        }
    }

    fun addCharacter(character: Character) {
        characterList = characterList + character
    }

    fun removeCharacter(character: Character) {
        characterList = characterList.toMutableList().also {
            it.remove(character)
        }
    }
}