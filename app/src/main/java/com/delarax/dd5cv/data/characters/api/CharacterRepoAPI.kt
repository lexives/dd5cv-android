package com.delarax.dd5cv.data.characters.api

import com.delarax.dd5cv.data.characters.CharacterRepo
import com.delarax.dd5cv.models.State
import com.delarax.dd5cv.models.characters.Character
import com.delarax.dd5cv.models.characters.CharacterSummary
import com.delarax.dd5cv.models.characters.toSummary
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CharacterRepoAPI @Inject constructor(
    private val characterService: CharacterService
) : CharacterRepo {
    override suspend fun getAllCharacters(): State<List<Character>> =
        characterService.getAllCharacters().let { response ->
            if (response.isSuccessful) {
                response.body()?.let {
                    State.Success(it)
                } ?: State.Empty(listOf())
            } else {
                State.Error(Throwable(response.errorBody().toString()))
            }
        }

    override suspend fun getAllCharacterSummaries(): State<List<CharacterSummary>> =
        characterService.getAllCharacters().let { response ->
            if (response.isSuccessful) {
                response.body()?.let {
                    State.Success(it.map { it.toSummary() })
                } ?: State.Empty(listOf())
            } else {
                State.Error(Throwable(response.errorBody().toString()))
            }
        }

    override suspend fun getCharacterById(characterId: String): Character? {
        return null
        TODO("Not yet implemented")
    }

    override suspend fun addCharacter(character: Character): Character? {
        return null
        TODO("Not yet implemented")
    }

    override suspend fun updateCharacter(character: Character): Character? {
        return null
        TODO("Not yet implemented")
    }

    override suspend fun removeCharacter(id: String): Boolean {
        return false
        TODO("Not yet implemented")
    }

}