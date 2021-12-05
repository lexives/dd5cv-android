package com.delarax.dd5cv.data.database

import com.delarax.dd5cv.data.characters.room.CharacterEntity
import com.delarax.dd5cv.data.characters.room.ClassLevelEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

internal class ClassLevelDAOTest : AppDatabaseTest() {

    private val characterEntity = CharacterEntity(name = "test character")
    private val classes = listOf(
        ClassLevelEntity(characterId = characterEntity.id, name = "first", level = 1),
        ClassLevelEntity(characterId = characterEntity.id, name = "second", level = 2),
        ClassLevelEntity(characterId = characterEntity.id, name = "third", level = 3)
    )

    @Test
    @Throws(Exception::class)
    fun getByCharacterIdThatDoesNotExist() {
        val result = classLevelDAO.getAllForCharacter(characterEntity.id)
        assertTrue(result.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun insertManyAndGetByCharacterId() {
        classLevelDAO.insertMany(*classes.toTypedArray())
        val result = classLevelDAO.getAllForCharacter(characterEntity.id)

        assertEquals(3, result.size)
        assertTrue(result.containsAll(classes))
    }

    @Test
    @Throws(Exception::class)
    fun insertManyThatAlreadyExist() {
        val sameClassesDifferentLevels = classes.map { it.copy(level = 99) }

        classLevelDAO.insertMany(*classes.toTypedArray())
        classLevelDAO.insertMany(*sameClassesDifferentLevels.toTypedArray())
        val result = classLevelDAO.getAllForCharacter(characterEntity.id)

        assertEquals(3, result.size)
        assertTrue(result.containsAll(classes))
    }

    @Test
    @Throws(Exception::class)
    fun updateManySomeExistSomeDoNot() {
        val classesToUpdate = listOf(
            classes.first().copy(level = 99),
            ClassLevelEntity(characterId = characterEntity.id, name = "new class", level = 4),
            classes.last().copy(level = 88)
        )

        val expectedClasses = listOf(
            classesToUpdate.first(),
            classes[1],
            classesToUpdate.last()
        )

        classLevelDAO.insertMany(*classes.toTypedArray())
        classLevelDAO.updateMany(*classesToUpdate.toTypedArray())
        val result = classLevelDAO.getAllForCharacter(characterEntity.id)

        assertEquals(3, result.size)
        assertTrue(result.containsAll(expectedClasses))
    }

    @Test
    @Throws(Exception::class)
    fun deleteAllForCharacter() {
        classLevelDAO.insertMany(*classes.toTypedArray())
        classLevelDAO.deleteAllForCharacter(characterEntity.id)
        val result = classLevelDAO.getAllForCharacter(characterEntity.id)

        assertTrue(result.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun deleteAllForCharacterThatDoesNotExist() {
        classLevelDAO.deleteAllForCharacter(characterEntity.id)
        val result = classLevelDAO.getAllForCharacter(characterEntity.id)

        assertTrue(result.isEmpty())
    }
}