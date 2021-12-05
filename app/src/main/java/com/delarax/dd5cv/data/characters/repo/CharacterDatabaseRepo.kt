package com.delarax.dd5cv.data.characters.repo

import com.delarax.dd5cv.data.characters.room.CharacterEntity
import com.delarax.dd5cv.data.characters.room.ClassLevelEntity
import com.delarax.dd5cv.data.database.AppDatabase
import com.delarax.dd5cv.models.characters.Character
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CharacterDatabaseRepo @Inject constructor(
    private val database: AppDatabase
) {
    fun getCharacterById(characterId: String): Character? {
        val classes = database.classLevelDAO()
            .getAllForCharacter(characterId)
            .map { it.toClassLevel() }
        return database.characterDAO().getById(characterId)?.toCharacter(classes)
    }

    fun insertCharacter(character: Character) {
        database.classLevelDAO().insertMany(
            *character.classes.map {
                ClassLevelEntity.from(classLevel = it, characterId = character.id)
            }.toTypedArray()
        )
        database.characterDAO().insert(
            CharacterEntity.from(character)
        )
    }

    fun updateCharacter(character: Character) {
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
    }

    fun deleteCharacter(characterId: String) {
        database.classLevelDAO().deleteAllForCharacter(characterId)
        database.characterDAO().deleteCharacterById(characterId)
    }
}