package com.delarax.dd5cv.data.characters.local

import com.delarax.dd5cv.data.characters.local.room.CharacterEntity
import com.delarax.dd5cv.data.characters.local.room.ClassLevelEntity
import com.delarax.dd5cv.models.State
import com.delarax.dd5cv.models.characters.Character
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class LocalCharacterDataSourceMocked @Inject constructor() : LocalCharacterDataSource {
    private val characterEntities: MutableMap<String, CharacterEntity> = mutableMapOf()
    private val classLevelEntities: MutableList<ClassLevelEntity> = mutableListOf()

    override suspend fun getCharacterById(characterId: String): State<Character> {
        val classes = classLevelEntities
            .filter { it.characterId == characterId }
            .map { it.toClassLevel() }

        return characterEntities[characterId]?.let {
            val character = it.toCharacter(classes)
            State.Success(character)
        } ?: State.Error(
            Throwable("Could not find character with id $characterId"),
            statusCode = 404
        )
    }

    override suspend fun insertCharacter(character: Character): State<Unit> {
        character.classes.forEach { classLevel ->
            val alreadyAdded: Boolean = classLevelEntities.find {
                it.characterId == character.id && it.name == classLevel.name
            } != null

            if (!alreadyAdded) {
                classLevelEntities.add(ClassLevelEntity.from(classLevel, character.id))
            }
        }
        characterEntities[character.id] = CharacterEntity.from(character)
        return State.Success(Unit)
    }

    override suspend fun updateCharacter(character: Character): State<Unit> {
        character.classes.forEach { classLevel ->
            val existingClassLevelEntity: ClassLevelEntity? = classLevelEntities.find {
                it.characterId == character.id && it.name == classLevel.name
            }
            existingClassLevelEntity?.let {
                classLevelEntities.remove(it)
            }
            classLevelEntities.add(ClassLevelEntity.from(classLevel, character.id))
        }
        characterEntities[character.id] = CharacterEntity.from(character)
        return State.Success(Unit)
    }

    override suspend fun deleteCharacterById(characterId: String): State<Unit> {
        classLevelEntities.removeAll {
            it.characterId == characterId
        }
        characterEntities.remove(characterId)
        return State.Success(Unit)
    }

    override suspend fun deleteAll(): State<Unit> {
        characterEntities.clear()
        characterEntities.clear()
        return State.Success(Unit)
    }
}