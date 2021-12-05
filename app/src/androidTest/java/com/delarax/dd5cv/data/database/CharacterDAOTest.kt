package com.delarax.dd5cv.data.database

import com.delarax.dd5cv.data.characters.room.CharacterEntity
import org.junit.Assert.assertEquals
import org.junit.Test

internal class CharacterDAOTest : AppDatabaseTest() {

    private val characterEntity = CharacterEntity(name = "test character")

    @Test
    @Throws(Exception::class)
    fun getCharacterThatDoesNotExist() {
        val result = characterDAO.getById(characterEntity.id)
        assertEquals(null, result)
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetCharacter() {
        characterDAO.insert(characterEntity)
        val result = characterDAO.getById(characterEntity.id)

        assertEquals(characterEntity, result)
    }

    @Test
    @Throws(Exception::class)
    fun insertCharacterThatAlreadyExists() {
        val sameCharacterDifferentName = characterEntity.copy(name = "something else")

        characterDAO.insert(characterEntity)
        characterDAO.insert(sameCharacterDifferentName)
        val result = characterDAO.getById(characterEntity.id)

        assertEquals(characterEntity, result)
    }

    @Test
    @Throws(Exception::class)
    fun updateCharacter() {
        val expectedCharacterEntity = characterEntity.copy(name = "something else")

        characterDAO.insert(characterEntity)
        characterDAO.update(expectedCharacterEntity)
        val result = characterDAO.getById(characterEntity.id)

        assertEquals(expectedCharacterEntity, result)
    }

    @Test
    @Throws(Exception::class)
    fun updateCharacterThatDoesNotExist() {
        characterDAO.update(characterEntity)
        val result = characterDAO.getById(characterEntity.id)

        assertEquals(null, result)
    }

    @Test
    @Throws(Exception::class)
    fun deleteCharacter() {
        characterDAO.insert(characterEntity)
        characterDAO.deleteCharacterById(characterEntity.id)
        val result = characterDAO.getById(characterEntity.id)
        assertEquals(null, result)
    }

    @Test
    @Throws(Exception::class)
    fun deleteCharacterThatDoesNotExist() {
        characterDAO.deleteCharacterById(characterEntity.id)
        val result = characterDAO.getById(characterEntity.id)
        assertEquals(null, result)
    }
}