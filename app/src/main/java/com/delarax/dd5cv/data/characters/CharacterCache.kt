package com.delarax.dd5cv.data.characters

import com.delarax.dd5cv.models.characters.Character
import com.delarax.dd5cv.models.data.CacheType
import com.delarax.dd5cv.models.data.State
import kotlinx.coroutines.flow.StateFlow

/**
 * For interfacing with a local data source
 */
interface CharacterCache {
    val inProgressCharacterIdFlow: StateFlow<String?>
    suspend fun cacheCharacter(character: Character, type: CacheType): State<Unit>
    suspend fun getCharacterById(id: String, type: CacheType): State<Character>
    suspend fun getAllBackups(): State<List<Character>>
    suspend fun getAllEdits(): State<List<Character>>
    suspend fun deleteCharacterById(id: String, type: CacheType): State<Unit>
    suspend fun clear(): State<Unit>
}