package com.delarax.dd5cv.ui.characters

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CharacterListVM @Inject constructor(): ViewModel() {

    // public state
    var characterList: List<String> by mutableStateOf(listOf())
        private set

    fun addCharacter(character: String) {
        characterList = characterList + character
    }

    fun removeCharacter(character: String) {
        characterList = characterList.toMutableList().also {
            it.remove(character)
        }
    }
}