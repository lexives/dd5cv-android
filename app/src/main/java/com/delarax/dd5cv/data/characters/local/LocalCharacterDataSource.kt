package com.delarax.dd5cv.data.characters.local

import com.delarax.dd5cv.models.State
import com.delarax.dd5cv.models.characters.Character

internal interface LocalCharacterDataSource {
    suspend fun getCharacterById(characterId: String): State<Character>

    suspend fun insertCharacter(character: Character) : State<Unit>

    suspend fun updateCharacter(character: Character) : State<Unit>

    suspend fun deleteCharacterById(characterId: String) : State<Unit>

    suspend fun deleteAll() : State<Unit>
}