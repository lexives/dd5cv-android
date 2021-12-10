package com.delarax.dd5cv.data.characters.local

import android.database.sqlite.SQLiteException
import com.delarax.dd5cv.data.characters.local.room.CharacterDAO
import com.delarax.dd5cv.data.characters.local.room.CharacterEntity
import com.delarax.dd5cv.data.characters.local.room.ClassLevelDAO
import com.delarax.dd5cv.data.characters.local.room.ClassLevelEntity
import com.delarax.dd5cv.data.database.AppDatabase
import com.delarax.dd5cv.models.State.Error
import com.delarax.dd5cv.models.State.Success
import com.delarax.dd5cv.models.characters.Character
import com.delarax.dd5cv.models.characters.CharacterClassLevel
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
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
    @Mock
    private lateinit var classLevelDAO: ClassLevelDAO

    private lateinit var localCharacterDataSourceRoom: LocalCharacterDataSourceRoom

    private val classes = listOf(
        CharacterClassLevel(name = "first", level = 1),
        CharacterClassLevel(name = "second", level = 2)
    )
    private val character = Character(name = "test character", classes = classes)

    private val characterEntity = CharacterEntity.from(character)
    private val classLevelEntities = classes.map { ClassLevelEntity.from(it, characterEntity.id) }

    @Before
    fun setup() {
        localCharacterDataSourceRoom = LocalCharacterDataSourceRoom(database)
        Mockito.`when`(database.characterDAO()).thenReturn(characterDAO)
        Mockito.`when`(database.classLevelDAO()).thenReturn(classLevelDAO)
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
        val classLevelEntities2 = classes2.map { ClassLevelEntity.from(it, characterEntity2.id) }

        Mockito.`when`(characterDAO.getAll()).thenReturn(listOf(characterEntity, characterEntity2))
        Mockito.`when`(classLevelDAO.getAllForCharacter(character.id))
            .thenReturn(classLevelEntities)
        Mockito.`when`(classLevelDAO.getAllForCharacter(character2.id))
            .thenReturn(classLevelEntities2)

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
        Mockito.`when`(classLevelDAO.getAllForCharacter(character.id))
            .thenReturn(classLevelEntities)

        val result = localCharacterDataSourceRoom.getCharacterById(character.id)

        assertTrue(result is Success)
        assertEquals(character, result.getOrNull())
    }

    @Test
    fun getCharacterByIdNotFound() = runBlocking {
        Mockito.`when`(characterDAO.getById(character.id)).thenReturn(null)
        Mockito.`when`(classLevelDAO.getAllForCharacter(character.id)).thenReturn(listOf())

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
        Mockito.`when`(classLevelDAO.insertMany(*classLevelEntities.toTypedArray()))
            .thenReturn(Unit)

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
        val updatedClassLevelEntities = updatedClasses.map {
            ClassLevelEntity.from(it, characterEntity.id)
        }

        Mockito.`when`(characterDAO.update(updatedCharacterEntity)).thenReturn(Unit)
        Mockito.`when`(classLevelDAO.updateMany(*updatedClassLevelEntities.toTypedArray()))
            .thenReturn(Unit)

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
        Mockito.`when`(classLevelDAO.deleteAllForCharacter(characterEntity.id)).thenReturn(Unit)

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
        Mockito.`when`(classLevelDAO.deleteAll()).thenReturn(Unit)

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