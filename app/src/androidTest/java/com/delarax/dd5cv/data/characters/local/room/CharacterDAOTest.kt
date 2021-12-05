package com.delarax.dd5cv.data.characters.local.room

import com.delarax.dd5cv.data.database.AppDatabaseTest
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

internal class CharacterDAOTest : AppDatabaseTest() {

    private val characterEntity = CharacterEntity(name = "test character")

    @Test
    @Throws(Exception::class)
    fun getCharacterThatDoesNotExist() = runBlocking {
        val result = characterDAO.getById(characterEntity.id)
        assertEquals(null, result)
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetCharacter() = runBlocking {
        characterDAO.insert(characterEntity)
        val result = characterDAO.getById(characterEntity.id)

        assertEquals(characterEntity, result)
    }

    @Test
    @Throws(Exception::class)
    fun insertCharacterThatAlreadyExists() = runBlocking {
        val sameCharacterDifferentName = characterEntity.copy(name = "something else")

        characterDAO.insert(characterEntity)
        characterDAO.insert(sameCharacterDifferentName)
        val result = characterDAO.getById(characterEntity.id)

        assertEquals(characterEntity, result)
    }

    @Test
    @Throws(Exception::class)
    fun updateCharacter() = runBlocking {
        val expectedCharacterEntity = characterEntity.copy(name = "something else")

        characterDAO.insert(characterEntity)
        characterDAO.update(expectedCharacterEntity)
        val result = characterDAO.getById(characterEntity.id)

        assertEquals(expectedCharacterEntity, result)
    }

    @Test
    @Throws(Exception::class)
    fun updateCharacterThatDoesNotExist() = runBlocking {
        characterDAO.update(characterEntity)
        val result = characterDAO.getById(characterEntity.id)

        assertEquals(null, result)
    }

    @Test
    @Throws(Exception::class)
    fun deleteCharacter() = runBlocking {
        characterDAO.insert(characterEntity)
        characterDAO.deleteCharacterById(characterEntity.id)
        val result = characterDAO.getById(characterEntity.id)

        assertEquals(null, result)
    }

    @Test
    @Throws(Exception::class)
    fun deleteCharacterThatDoesNotExist() = runBlocking {
        characterDAO.deleteCharacterById(characterEntity.id)
        val result = characterDAO.getById(characterEntity.id)

        assertEquals(null, result)
    }

    @Test
    @Throws(Exception::class)
    fun deleteAll() = runBlocking {
        characterDAO.insert(characterEntity)
        characterDAO.insert(characterEntity.copy(id = "different id"))
        characterDAO.deleteAll()

        val result = characterDAO.getById(characterEntity.id)

        assertEquals(null, result)
    }
}