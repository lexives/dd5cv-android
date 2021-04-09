package com.delarax.dd5cv.data.characters

import com.delarax.dd5cv.models.characters.Character
import com.delarax.dd5cv.models.characters.CharacterSummary

interface CharacterRepo {
    suspend fun getAllCharacters(): List<Character>

    suspend fun getAllCharacterSummaries(): List<CharacterSummary>

    suspend fun getCharacterById(characterId: String): Character?

    suspend fun addCharacter(character: Character) : Character?

    suspend fun updateCharacter(character: Character) : Character?

    suspend fun removeCharacter(id: String) : Boolean
}