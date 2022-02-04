package com.delarax.dd5cv.data.characters

import com.delarax.dd5cv.data.characters.remote.RemoteCharacterDataSourceMocked
import com.delarax.dd5cv.models.characters.Character
import com.delarax.dd5cv.models.characters.CharacterClassLevel
import com.delarax.dd5cv.models.data.CacheType
import com.delarax.dd5cv.models.data.State.Error
import com.delarax.dd5cv.models.data.State.Success
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import javax.inject.Inject

@HiltAndroidTest
@RunWith(MockitoJUnitRunner::class)
class CharacterCacheTest {

    @Rule
    @JvmField
    var hiltRule = HiltAndroidRule(this)

    @Inject lateinit var characterCache: CharacterCache

    @Before
    fun setup() {
        hiltRule.inject()
    }
    
    @Test
    fun cacheCharacter_BACKUP_and_getCachedCharacterById_BACKUP() = runBlocking {
        val character = RemoteCharacterDataSourceMocked.DEFAULT_CHARACTERS.first()

        assertNull(characterCache.inProgressCharacterIdFlow.value)

        val cacheResult = characterCache.cacheCharacter(character, CacheType.BACKUP)
        assertTrue(cacheResult is Success)

        assertNull(characterCache.inProgressCharacterIdFlow.value)

        val getResult = characterCache.getCharacterById(character.id, CacheType.BACKUP)
        assertTrue(getResult is Success)
        assertEquals(character, getResult.getOrNull())
    }

    @Test
    fun cacheCharacter_BACKUP_backupAlreadyExists() = runBlocking {
        val character = RemoteCharacterDataSourceMocked.DEFAULT_CHARACTERS.first()
        characterCache.cacheCharacter(character, CacheType.BACKUP)

        val updatedCharacter = character.copy(name = "some other name")
        val cacheResult = characterCache.cacheCharacter(updatedCharacter, CacheType.BACKUP)
        assertTrue(cacheResult is Success)

        val getResult = characterCache.getCharacterById(updatedCharacter.id, CacheType.BACKUP)
        assertTrue(getResult is Success)
        assertEquals(updatedCharacter, getResult.getOrNull())
    }

    @Test
    fun cacheCharacter_EDITS_and_getCachedCharacterById_EDITS() = runBlocking {
        val character = RemoteCharacterDataSourceMocked.DEFAULT_CHARACTERS.first()

        assertNull(characterCache.inProgressCharacterIdFlow.value)

        val cacheResult = characterCache.cacheCharacter(character, CacheType.EDITS)
        assertTrue(cacheResult is Success)

        assertEquals(character.id, characterCache.inProgressCharacterIdFlow.value)

        val getResult = characterCache.getCharacterById(character.id, CacheType.EDITS)
        assertTrue(getResult is Success)
        assertEquals(character, getResult.getOrNull())
    }

    @Test
    fun cacheCharacter_EDITS_editsAlreadyExist() = runBlocking {
        val character = RemoteCharacterDataSourceMocked.DEFAULT_CHARACTERS.first()
        characterCache.cacheCharacter(character, CacheType.EDITS)

        val updatedCharacter = character.copy(name = "some other name")
        val cacheResult = characterCache.cacheCharacter(updatedCharacter, CacheType.EDITS)
        assertTrue(cacheResult is Success)

        val getResult = characterCache.getCharacterById(updatedCharacter.id, CacheType.EDITS)
        assertTrue(getResult is Success)
        assertEquals(updatedCharacter, getResult.getOrNull())
    }

    @Test
    fun getCharacterById_BACKUP_backupDoesNotExist() = runBlocking {
        val result = characterCache.getCharacterById("fake id", CacheType.BACKUP)

        assertTrue(result is Error)
        assertEquals(404, (result as Error).statusCode)
    }

    @Test
    fun getCharacterById_BACKUP_editsExistButBackupDoesNot() = runBlocking {
        val character = RemoteCharacterDataSourceMocked.DEFAULT_CHARACTERS.first()
        characterCache.cacheCharacter(character, CacheType.EDITS)

        val result = characterCache.getCharacterById(character.id, CacheType.BACKUP)
        assertTrue(result is Error)
        assertEquals(404, (result as Error).statusCode)
    }

    @Test
    fun getCharacterById_EDITS_editsDoNotExist() = runBlocking {
        val result = characterCache.getCharacterById("fake id", CacheType.EDITS)

        assertTrue(result is Error)
        assertEquals(404, (result as Error).statusCode)
    }

    @Test
    fun getCharacterById_EDITS_backupExistsButEditsDoNot() = runBlocking {
        val character = RemoteCharacterDataSourceMocked.DEFAULT_CHARACTERS.first()
        characterCache.cacheCharacter(character, CacheType.BACKUP)

        val result = characterCache.getCharacterById(character.id, CacheType.EDITS)
        assertTrue(result is Error)
        assertEquals(404, (result as Error).statusCode)
    }

    @Test
    fun getAllBackups_getAllEdits_cacheHasNoData() = runBlocking {
        val backupsResult = characterCache.getAllBackups()
        val editsResult = characterCache.getAllEdits()

        assertTrue(backupsResult is Success)
        assertTrue((backupsResult as Success).value.isEmpty())

        assertTrue(editsResult is Success)
        assertTrue((editsResult as Success).value.isEmpty())
    }

    @Test
    fun getAllBackups_getAllEdits_cacheHasBackupAndEdits() = runBlocking {
        val classes = listOf(
            CharacterClassLevel(name = "first", level = 1),
            CharacterClassLevel(name = "second", level = 2),
        )
        val character = Character(name = "character", classes = classes)
        val character2 = Character(name = "character 2", classes = classes)

        characterCache.cacheCharacter(character, CacheType.BACKUP)
        characterCache.cacheCharacter(character, CacheType.EDITS)
        characterCache.cacheCharacter(character2, CacheType.BACKUP)

        val backupsResult = characterCache.getAllBackups()
        val editsResult = characterCache.getAllEdits()

        assertTrue(backupsResult is Success)
        assertEquals(2, (backupsResult as Success).value.size)
        assertTrue(backupsResult.value.containsAll(listOf(character, character2)))

        assertTrue(editsResult is Success)
        assertEquals(1, (editsResult as Success).value.size)
        assertTrue(editsResult.value.contains(character))
    }

    @Test
    fun getAllBackups_getAllEdits_cacheHasBackupButNoEdits() = runBlocking {
        val classes = listOf(
            CharacterClassLevel(name = "first", level = 1),
            CharacterClassLevel(name = "second", level = 2),
        )
        val character = Character(name = "character", classes = classes)

        characterCache.cacheCharacter(character, CacheType.BACKUP)

        val backupsResult = characterCache.getAllBackups()
        val editsResult = characterCache.getAllEdits()

        assertTrue(backupsResult is Success)
        assertEquals(1, (backupsResult as Success).value.size)
        assertTrue(backupsResult.value.contains(character))

        assertTrue(editsResult is Success)
        assertTrue((editsResult as Success).value.isEmpty())
    }

    @Test
    fun getAllBackups_getAllEdits_cacheHasEditsButNoBackup() = runBlocking {
        val classes = listOf(
            CharacterClassLevel(name = "first", level = 1),
            CharacterClassLevel(name = "second", level = 2),
        )
        val character = Character(name = "character", classes = classes)

        characterCache.cacheCharacter(character, CacheType.EDITS)

        val backupsResult = characterCache.getAllBackups()
        val editsResult = characterCache.getAllEdits()

        assertTrue(backupsResult is Success)
        assertTrue((backupsResult as Success).value.isEmpty())

        assertTrue(editsResult is Success)
        assertEquals(1, (editsResult as Success).value.size)
        assertTrue(editsResult.value.contains(character))
    }

    @Test
    fun deleteCharacterById_BACKUP() = runBlocking {
        val character = RemoteCharacterDataSourceMocked.DEFAULT_CHARACTERS.first()
        characterCache.cacheCharacter(character, CacheType.BACKUP)

        assertNull(characterCache.inProgressCharacterIdFlow.value)

        val deleteResult = characterCache.deleteCharacterById(character.id, CacheType.BACKUP)
        assertTrue(deleteResult is Success)

        assertNull(characterCache.inProgressCharacterIdFlow.value)

        val getResult = characterCache.getCharacterById(character.id, CacheType.BACKUP)
        assertTrue(getResult is Error)
        assertEquals(404, (getResult as Error).statusCode)
    }

    @Test
    fun deleteCharacterById_BACKUP_backupDoesNotExist() = runBlocking {
        val result = characterCache.deleteCharacterById("fake id", CacheType.BACKUP)
        assertTrue(result is Success)
    }

    @Test
    fun deleteCharacterById_EDITS() = runBlocking {
        val character = RemoteCharacterDataSourceMocked.DEFAULT_CHARACTERS.first()
        characterCache.cacheCharacter(character, CacheType.EDITS)

        assertEquals(character.id, characterCache.inProgressCharacterIdFlow.value)

        val deleteResult = characterCache.deleteCharacterById(character.id, CacheType.EDITS)
        assertTrue(deleteResult is Success)

        assertNull(characterCache.inProgressCharacterIdFlow.value)

        val getResult = characterCache.getCharacterById(character.id, CacheType.EDITS)
        assertTrue(getResult is Error)
        assertEquals(404, (getResult as Error).statusCode)
    }

    @Test
    fun deleteCharacterById_EDITS_editsDoNotExist() = runBlocking {
        val result = characterCache.deleteCharacterById("fake id", CacheType.EDITS)
        assertTrue(result is Success)
    }

    @Test
    fun clear() = runBlocking {
        RemoteCharacterDataSourceMocked.DEFAULT_CHARACTERS.subList(0, 2).forEach {
            characterCache.cacheCharacter(it, CacheType.BACKUP)
            characterCache.cacheCharacter(it, CacheType.EDITS)
        }

        assertEquals(
            RemoteCharacterDataSourceMocked.DEFAULT_CHARACTERS[1].id,
            characterCache.inProgressCharacterIdFlow.value
        )

        val clearResult = characterCache.clear()
        assertTrue(clearResult is Success)

        assertNull(characterCache.inProgressCharacterIdFlow.value)

        RemoteCharacterDataSourceMocked.DEFAULT_CHARACTERS.subList(0, 3).forEach {
            val getBackupResult = characterCache.getCharacterById(it.id, CacheType.BACKUP)
            assertTrue(getBackupResult is Error)
            assertEquals(404, (getBackupResult as Error).statusCode)

            val getEditsResult = characterCache.getCharacterById(it.id, CacheType.EDITS)
            assertTrue(getEditsResult is Error)
            assertEquals(404, (getEditsResult as Error).statusCode)
        }
    }
}