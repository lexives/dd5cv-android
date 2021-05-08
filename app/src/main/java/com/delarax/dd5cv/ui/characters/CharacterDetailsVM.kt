package com.delarax.dd5cv.ui.characters

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.delarax.dd5cv.data.characters.CharacterRepo
import com.delarax.dd5cv.models.characters.Character
import com.delarax.dd5cv.utils.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterDetailsVM @Inject constructor(
    private val characterRepo: CharacterRepo
) : ViewModel() {
    // public state
    var characterState: State<Character> by mutableStateOf(State.Loading(0))
        private set

    fun fetchCharacterById(id: String?) {
        id?.let {
            viewModelScope.launch {
                characterState = characterRepo.getCharacterById(id)
            }
        }
    }
}