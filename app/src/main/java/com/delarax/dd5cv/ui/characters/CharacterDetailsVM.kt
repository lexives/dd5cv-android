package com.delarax.dd5cv.ui.characters

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.delarax.dd5cv.data.characters.CharacterRepo
import com.delarax.dd5cv.models.characters.Character
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterDetailsVM @Inject constructor(
    private val characterRepo: CharacterRepo
) : ViewModel() {
    // public state
    var character: Character by mutableStateOf(Character())
        private set

    fun fetchCharacterById(characterId: String?) {
        characterId?.let {
            viewModelScope.launch {
                characterRepo.getCharacterById(characterId)?.let {
                    character = it
                }
            }
        }
    }
}