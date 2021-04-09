package com.delarax.dd5cv.data.characters.api

import com.delarax.dd5cv.data.characters.CharacterRepo
import com.delarax.dd5cv.models.characters.Character
import com.delarax.dd5cv.models.characters.CharacterSummary
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CharacterRepoAPI @Inject constructor(
    private val characterService: CharacterService
) : CharacterRepo {
    override suspend fun getAllCharacters(): List<Character> =
        characterService.getAllCharacters()

    override suspend fun getAllCharacterSummaries(): List<CharacterSummary> {
        TODO("Not yet implemented")
    }

    override suspend fun getCharacterById(characterId: String): Character? {
        TODO("Not yet implemented")
    }

    override suspend fun addCharacter(character: Character): Character? {
        TODO("Not yet implemented")
    }

    override suspend fun updateCharacter(character: Character): Character? {
        TODO("Not yet implemented")
    }

    override suspend fun removeCharacter(id: String): Boolean {
        TODO("Not yet implemented")
    }

}