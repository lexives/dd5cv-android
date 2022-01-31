package com.delarax.dd5cv.data.characters.local

import android.database.sqlite.SQLiteException
import com.delarax.dd5cv.data.database.AppDatabase
import com.delarax.dd5cv.models.data.State.Error
import com.delarax.dd5cv.models.data.State.Success
import com.delarax.dd5cv.models.characters.Character
import com.delarax.dd5cv.models.characters.CharacterClassLevel
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
internal class LocalCharacterDataSourceRoomTest {
    @Mock
    private lateinit var database: AppDatabase
    @Mock
    private lateinit var characterDAO: CharacterDAO

    private lateinit var localCharacterDataSourceRoom: LocalCharacterDataSourceRoom

    private val classes = listOf(
        CharacterClassLevel(name = "first", level = 1),
        CharacterClassLevel(name = "second", level = 2)
    )
    private val character = Character(name = "test character", classes = classes)

    private val characterEntity = CharacterEntity.from(character)

    @Before
    fun setup() {
        localCharacterDataSourceRoom = LocalCharacterDataSourceRoom(database)
        Mockito.`when`(database.characterDAO()).thenReturn(characterDAO)
    }

    @Test
    fun getAllCharactersSuccessNoData() = runBlocking {
        Mockito.`when`(characterDAO.getAll()).thenReturn(listOf())

        val result = localCharacterDataSourceRoom.getAllCharacters()

        assertTrue(result is Success)
        assertTrue((result as Success).value.isEmpty())
    }

    @Test
    fun getAllCharactersSuccessHasData() = runBlocking {
        val classes2 = listOf(
            CharacterClassLevel(name = "2 first", level = 3),
            CharacterClassLevel(name = "2 second", level = 4)
        )
        val character2 = Character(name = "test character 2", classes = classes2)

        val characterEntity2 = CharacterEntity.from(character2)

        Mockito.`when`(characterDAO.getAll()).thenReturn(listOf(characterEntity, characterEntity2))

        val result = localCharacterDataSourceRoom.getAllCharacters()

        assertTrue(result is Success)
        assertEquals(2, (result as Success).value.size)
        assertTrue(result.value.containsAll(listOf(character, character2)))
    }

    @Test
    fun getAllCharactersDatabaseError() = runBlocking {
        val result = localCharacterDataSourceRoom.getAllCharacters()

        assertTrue(result is Error) // Message is null
    }

    @Test
    fun getCharacterByIdSuccess() = runBlocking {
        Mockito.`when`(characterDAO.getById(character.id)).thenReturn(characterEntity)

        val result = localCharacterDataSourceRoom.getCharacterById(character.id)

        assertTrue(result is Success)
        assertEquals(character, result.getOrNull())
    }

    @Test
    fun getCharacterByIdNotFound() = runBlocking {
        Mockito.`when`(characterDAO.getById(character.id)).thenReturn(null)

        val result = localCharacterDataSourceRoom.getCharacterById(character.id)

        assertTrue(result is Error)
        assertEquals(404, (result as Error).statusCode)
        assertEquals("Could not find character with id ${character.id}", result.throwable.message)
    }

    @Test
    fun getCharacterByIdDatabaseError() = runBlocking {
        val result = localCharacterDataSourceRoom.getCharacterById(character.id)

        assertTrue(result is Error) // Message is null
    }

    @Test
    fun insertCharacterSuccess() = runBlocking {
        Mockito.`when`(characterDAO.insert(characterEntity)).thenReturn(Unit)

        val result = localCharacterDataSourceRoom.insertCharacter(character)

        assertTrue(result is Success)
    }

    @Test
    fun insertCharacterDatabaseError() = runBlocking {
        Mockito.`when`(characterDAO.insert(characterEntity))
            .thenThrow(SQLiteException("some error"))

        val result = localCharacterDataSourceRoom.insertCharacter(character)

        assertTrue(result is Error) // Message is null
    }

    @Test
    fun updateCharacterSuccess() = runBlocking {
        val updatedClasses = listOf(
            classes.first(),
            CharacterClassLevel(name = "new class", level = 4),
            classes.last().copy(level = 99)
        )
        val updatedCharacter = character.copy(name = "updated name", classes = updatedClasses)
        val updatedCharacterEntity = CharacterEntity.from(updatedCharacter)

        Mockito.`when`(characterDAO.update(updatedCharacterEntity)).thenReturn(Unit)

        val result = localCharacterDataSourceRoom.updateCharacter(updatedCharacter)

        assertTrue(result is Success)
    }

    @Test
    fun updateCharacterDatabaseError() = runBlocking {
        val updatedClasses = listOf(
            classes.first(),
            CharacterClassLevel(name = "new class", level = 4),
            classes.last().copy(level = 99)
        )
        val updatedCharacter = character.copy(name = "updated name", classes = updatedClasses)
        val updatedCharacterEntity = CharacterEntity.from(updatedCharacter)

        Mockito.`when`(characterDAO.update(updatedCharacterEntity))
            .thenThrow(SQLiteException("some error"))

        val result = localCharacterDataSourceRoom.updateCharacter(updatedCharacter)

        assertTrue(result is Error) // Message is null
    }

    @Test
    fun deleteCharacterByIdSuccess() = runBlocking {
        Mockito.`when`(characterDAO.deleteCharacterById(characterEntity.id)).thenReturn(Unit)

        val result = localCharacterDataSourceRoom.deleteCharacterById(character.id)

        assertTrue(result is Success)
    }

    @Test
    fun deleteCharacterByIdDatabaseError() = runBlocking {
        Mockito.`when`(characterDAO.deleteCharacterById(characterEntity.id))
            .thenThrow(SQLiteException("some error"))

        val result = localCharacterDataSourceRoom.deleteCharacterById(character.id)

        assertTrue(result is Error) // Message is null
    }

    @Test
    fun deleteAllSuccess() = runBlocking {
        Mockito.`when`(characterDAO.deleteAll()).thenReturn(Unit)

        val result = localCharacterDataSourceRoom.deleteAll()

        assertTrue(result is Success)
    }

    @Test
    fun deleteAllDatabaseError() = runBlocking {
        Mockito.`when`(characterDAO.deleteAll()).thenThrow(SQLiteException("some error"))

        val result = localCharacterDataSourceRoom.deleteAll()

        assertTrue(result is Error) // Message is null
    }
}