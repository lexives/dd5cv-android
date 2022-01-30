package com.delarax.dd5cv.data.characters

import com.delarax.dd5cv.models.data.CacheType
import com.delarax.dd5cv.models.data.State
import com.delarax.dd5cv.models.characters.Character
import com.delarax.dd5cv.models.characters.CharacterSummary
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface CharacterRepo {
    /**
     * For interfacing with a remote data source
     */
    val allCharactersFlow: SharedFlow<State<List<Character>>>
    val allSummariesFlow: SharedFlow<State<List<CharacterSummary>>>
    val characterFlow: SharedFlow<Pair<String, State<Character>>>
    suspend fun fetchAllCharacters()
    suspend fun fetchAllCharacterSummaries()
    suspend fun fetchCharacterById(id: String)
    suspend fun addCharacter(character: Character): State<Character>
    suspend fun updateCharacter(character: Character): State<Character>
    suspend fun removeCharacterById(id: String): State<Unit>

    /**
     * For interfacing with a local data source
     */
    val inProgressCharacterIdFlow: StateFlow<String?>
    suspend fun cacheCharacter(character: Character, type: CacheType): State<Unit>
    suspend fun getCachedCharacterById(id: String, type: CacheType): State<Character>
    suspend fun getAllCachedCharacters(): State<List<Character>>
    suspend fun deleteCachedCharacterById(id: String, type: CacheType): State<Unit>
    suspend fun clearCache(): State<Unit>
}