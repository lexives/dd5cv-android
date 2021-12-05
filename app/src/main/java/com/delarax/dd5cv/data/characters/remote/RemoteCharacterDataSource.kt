package com.delarax.dd5cv.data.characters.remote

import com.delarax.dd5cv.models.State
import com.delarax.dd5cv.models.characters.Character
import com.delarax.dd5cv.models.characters.CharacterSummary

internal interface RemoteCharacterDataSource {
    suspend fun getAllCharacters(): State<List<Character>>

    suspend fun getAllCharacterSummaries(): State<List<CharacterSummary>>

    suspend fun getCharacterById(id: String): State<Character>

    suspend fun addCharacter(character: Character): State<Character>

    suspend fun updateCharacter(character: Character): State<Character>

    suspend fun removeCharacterById(id: String): State<Unit>
}