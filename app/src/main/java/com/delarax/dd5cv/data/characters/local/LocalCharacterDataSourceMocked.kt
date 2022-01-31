package com.delarax.dd5cv.data.characters.local

import com.delarax.dd5cv.models.characters.Character
import com.delarax.dd5cv.models.data.State
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class LocalCharacterDataSourceMocked @Inject constructor() : LocalCharacterDataSource {
    private val characterEntities: MutableMap<String, CharacterEntity> = mutableMapOf()

    override suspend fun getAllCharacters(): State<List<Character>> {
        val characters: List<Character> = characterEntities.map { mapEntry ->
            mapEntry.value.toCharacter()
        }
        return State.Success(characters)
    }

    override suspend fun getCharacterById(characterId: String): State<Character> {
        return characterEntities[characterId]?.let {
            State.Success(it.toCharacter())
        } ?: State.Error(
            Throwable("Could not find character with id $characterId"),
            statusCode = 404
        )
    }

    override suspend fun insertCharacter(character: Character): State<Unit> {
        characterEntities[character.id] = CharacterEntity.from(character)
        return State.Success(Unit)
    }

    override suspend fun updateCharacter(character: Character): State<Unit> {
        characterEntities[character.id] = CharacterEntity.from(character)
        return State.Success(Unit)
    }

    override suspend fun deleteCharacterById(characterId: String): State<Unit> {
        characterEntities.remove(characterId)
        return State.Success(Unit)
    }

    override suspend fun deleteAll(): State<Unit> {
        characterEntities.clear()
        characterEntities.clear()
        return State.Success(Unit)
    }
}