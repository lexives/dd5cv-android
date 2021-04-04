package com.delarax.dd5cv.data

import com.delarax.dd5cv.models.Character

interface CharacterRepo {
    fun getAllCharacters(): List<Character>

    fun addCharacter(character: Character) : Character?

    fun updateCharacter(character: Character) : Character?

    fun removeCharacter(id: String) : Boolean
}