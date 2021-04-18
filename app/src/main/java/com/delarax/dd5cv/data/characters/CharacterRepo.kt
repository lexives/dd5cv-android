package com.delarax.dd5cv.data.characters

import com.delarax.dd5cv.models.State
import com.delarax.dd5cv.models.characters.Character
import com.delarax.dd5cv.models.characters.CharacterSummary

interface CharacterRepo {
    suspend fun getAllCharacters(): State<List<Character>>

    suspend fun getAllCharacterSummaries(): State<List<CharacterSummary>>

    suspend fun getCharacterById(characterId: String): Character?

    suspend fun addCharacter(character: Character) : Character?

    suspend fun updateCharacter(character: Character) : Character?

    suspend fun removeCharacter(id: String) : Boolean
}