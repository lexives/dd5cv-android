package com.delarax.dd5cv.data.characters.local

import com.delarax.dd5cv.data.database.AppDatabase
import com.delarax.dd5cv.models.characters.Character
import com.delarax.dd5cv.models.data.State
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class LocalCharacterDataSourceRoom @Inject constructor(
    private val database: AppDatabase
) : LocalCharacterDataSource {
    override suspend fun getAllCharacters(): State<List<Character>> = try {
        val characters = database.characterDAO().getAll().map { characterEntity ->
            characterEntity.toCharacter()
        }
        State.Success(characters)
    } catch (e: Exception) {
        State.Error(e)
    }

    override suspend fun getCharacterById(characterId: String): State<Character> = try {
        database.characterDAO().getById(characterId)?.toCharacter()?.let {
            State.Success(it)
        } ?: State.Error(
            Throwable("Could not find character with id $characterId"),
            statusCode = 404
        )
    } catch (e: Exception) {
        State.Error(e)
    }

    override suspend fun insertCharacter(character: Character) : State<Unit> = try {
        database.characterDAO().insert(
            CharacterEntity.from(character)
        )
        State.Success(Unit)
    } catch (e: Exception) {
        State.Error(e)
    }

    override suspend fun updateCharacter(character: Character) : State<Unit> = try {
        database.characterDAO().update(
            CharacterEntity.from(character)
        )
        State.Success(Unit)
    } catch (e: Exception) {
        State.Error(e)
    }

    override suspend fun deleteCharacterById(characterId: String) : State<Unit> = try {
        database.characterDAO().deleteCharacterById(characterId)
        State.Success(Unit)
    } catch (e: Exception) {
        State.Error(e)
    }

    override suspend fun deleteAll() : State<Unit> = try {
        database.characterDAO().deleteAll()
        State.Success(Unit)
    } catch (e: Exception) {
        State.Error(e)
    }
}