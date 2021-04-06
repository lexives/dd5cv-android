package com.delarax.dd5cv.data

import com.delarax.dd5cv.models.Character
import com.delarax.dd5cv.models.CharacterSummary

interface CharacterRepo {
    fun getAllCharacters(): List<Character>

    fun getAllCharacterSummaries(): List<CharacterSummary>

    fun getCharacterById(characterId: String): Character?

    fun addCharacter(character: Character) : Character?

    fun updateCharacter(character: Character) : Character?

    fun removeCharacter(id: String) : Boolean
}