package com.delarax.dd5cv.data.characters

import com.delarax.dd5cv.models.characters.Character
import com.delarax.dd5cv.models.characters.CharacterSummary
import com.delarax.dd5cv.models.data.State
import kotlinx.coroutines.flow.SharedFlow

/**
 * For interfacing with a remote data source
 */
interface CharacterRepo {
    val allCharactersFlow: SharedFlow<State<List<Character>>>
    val allSummariesFlow: SharedFlow<State<List<CharacterSummary>>>
    val characterFlow: SharedFlow<Pair<String, State<Character>>>
    suspend fun fetchAllCharacters()
    suspend fun fetchAllCharacterSummaries()
    suspend fun fetchCharacterById(id: String)
    suspend fun addCharacter(character: Character): State<Character>
    suspend fun updateCharacter(character: Character): State<Character>
    suspend fun removeCharacterById(id: String): State<Unit>
}