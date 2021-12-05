package com.delarax.dd5cv.data.characters.repo

import com.delarax.dd5cv.data.characters.room.CharacterEntity
import com.delarax.dd5cv.data.characters.room.ClassLevelEntity
import com.delarax.dd5cv.data.database.AppDatabase
import com.delarax.dd5cv.models.State
import com.delarax.dd5cv.models.State.Error
import com.delarax.dd5cv.models.State.Success
import com.delarax.dd5cv.models.characters.Character
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CharacterDatabaseRepo @Inject constructor(
    private val database: AppDatabase
) {
    suspend fun getCharacterById(characterId: String): State<Character> = try {
        val classes = database.classLevelDAO()
            .getAllForCharacter(characterId)
            .map { it.toClassLevel() }

        val character = database.characterDAO().getById(characterId)?.toCharacter(classes)

        character?.let {
            Success(it)
        } ?: Error(Throwable("Could not find character with id $characterId"), statusCode = 404)

    } catch (e: Exception) {
        Error(e)
    }

    suspend fun insertCharacter(character: Character) : State<Unit> = try {
        database.classLevelDAO().insertMany(
            *character.classes.map {
                ClassLevelEntity.from(classLevel = it, characterId = character.id)
            }.toTypedArray()
        )
        database.characterDAO().insert(
            CharacterEntity.from(character)
        )

        Success(Unit)
    } catch (e: Exception) {
        Error(e)
    }

    suspend fun updateCharacter(character: Character) : State<Unit> = try {
       val mappedClasses =  character.classes.map {
           ClassLevelEntity.from(classLevel = it, characterId = character.id)
       }

        // First update classes that already exist
        database.classLevelDAO().updateMany(*mappedClasses.toTypedArray())

        // Then insert new classes
        database.classLevelDAO().insertMany(*mappedClasses.toTypedArray())

        database.characterDAO().update(
            CharacterEntity.from(character)
        )

        Success(Unit)
    } catch (e: Exception) {
        Error(e)
    }

    suspend fun deleteCharacterById(characterId: String) : State<Unit> = try {
        database.classLevelDAO().deleteAllForCharacter(characterId)
        database.characterDAO().deleteCharacterById(characterId)
        Success(Unit)
    } catch (e: Exception) {
        Error(e)
    }
}