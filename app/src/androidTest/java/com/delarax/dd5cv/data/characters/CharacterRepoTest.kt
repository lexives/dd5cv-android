package com.delarax.dd5cv.data.characters

import app.cash.turbine.test
import com.delarax.dd5cv.data.characters.remote.RemoteCharacterDataSourceMocked
import com.delarax.dd5cv.models.CacheType
import com.delarax.dd5cv.models.State.Error
import com.delarax.dd5cv.models.State.Loading
import com.delarax.dd5cv.models.State.Success
import com.delarax.dd5cv.models.characters.Character
import com.delarax.dd5cv.models.characters.CharacterClassLevel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import javax.inject.Inject

@HiltAndroidTest
@RunWith(MockitoJUnitRunner::class)
class CharacterRepoTest {

    @Rule
    @JvmField
    var hiltRule = HiltAndroidRule(this)

    @Inject lateinit var characterRepo: CharacterRepo

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun fetchAllCharacters() = runBlocking {
        characterRepo.allCharactersFlow.test {
            expectNoEvents()

            characterRepo.fetchAllCharacters()
            assertTrue(awaitItem() is Loading)

            val result = awaitItem()
            assertTrue(result is Success)
            assertEquals(RemoteCharacterDataSourceMocked.DEFAULT_CHARACTERS, result.getOrNull())

            expectNoEvents()
        }
    }

    @Test
    fun fetchAllCharacterSummaries() = runBlocking {
        characterRepo.allSummariesFlow.test {
            expectNoEvents()

            characterRepo.fetchAllCharacterSummaries()
            assertTrue(awaitItem() is Loading)

            val result = awaitItem()
            val expectedResult = RemoteCharacterDataSourceMocked.DEFAULT_CHARACTERS.map {
                it.toSummary()
            }
            assertTrue(result is Success)
            assertEquals(expectedResult, result.getOrNull())

            expectNoEvents()
        }
    }

    @Test
    fun fetchCharacterById_characterExists() = runBlocking {
        characterRepo.characterFlow.test {
            val characterId = RemoteCharacterDataSourceMocked.DEFAULT_CHARACTERS.first().id
            expectNoEvents()

            characterRepo.fetchCharacterById(characterId)

            val firstItem = awaitItem()
            assertEquals(characterId, firstItem.first)
            assertTrue(firstItem.second is Loading)

            val secondItem = awaitItem()
            assertEquals(characterId, secondItem.first)
            assertTrue(secondItem.second is Success)
            assertEquals(characterId, secondItem.second.getOrNull()?.id)

            expectNoEvents()
        }
    }

    @Test
    fun fetchCharacterById_characterDoesNotExist() = runBlocking {
        characterRepo.characterFlow.test {
            val characterId = "not a real id"
            expectNoEvents()

            characterRepo.fetchCharacterById(characterId)

            val firstItem = awaitItem()
            assertEquals(characterId, firstItem.first)
            assertTrue(firstItem.second is Loading)

            val secondItem = awaitItem()
            assertEquals(characterId, secondItem.first)
            assertTrue(secondItem.second is Error)
            assertEquals(404, (secondItem.second as Error).statusCode)

            expectNoEvents()
        }
    }

    @Test
    fun getCharacterById_characterExists() = runBlocking {
        val characterId = RemoteCharacterDataSourceMocked.DEFAULT_CHARACTERS.first().id

        val result = characterRepo.getCharacterById(characterId)

        assertTrue(result is Success)
        assertEquals(characterId, result.getOrNull()?.id)
    }

    @Test
    fun getCharacterById_characterDoesNotExist() = runBlocking {
        val characterId = "not a real id"

        val result = characterRepo.getCharacterById(characterId)

        assertTrue(result is Error)
        assertEquals(404, (result as Error).statusCode)
    }

    @Test
    fun addCharacter_characterExists() = runBlocking {
        val character = RemoteCharacterDataSourceMocked.DEFAULT_CHARACTERS.first()

        val result = characterRepo.addCharacter(character)

        assertTrue(result is Error)
        assertEquals(400, (result as Error).statusCode)
    }

    @Test
    fun addCharacter_characterDoesNotExist() = runBlocking {
        val classes = listOf(
            CharacterClassLevel(name = "first", level = 1),
            CharacterClassLevel(name = "second", level = 2),
        )
        val character = Character(name = "new character", classes = classes)

        val result = characterRepo.addCharacter(character)

        assertTrue(result is Success)
        assertEquals(character, result.getOrNull())

        characterRepo.characterFlow.test {
            characterRepo.fetchCharacterById(character.id)

            val lastItem = expectMostRecentItem()
            assertEquals(character.id, lastItem.first)
            assertTrue(lastItem.second is Success)
            assertEquals(character.id, lastItem.second.getOrNull()?.id)
        }
    }

    @Test
    fun updateCharacter_characterExists() = runBlocking {
        val character = RemoteCharacterDataSourceMocked.DEFAULT_CHARACTERS.first().copy(
            name = "updated name"
        )

        val result = characterRepo.updateCharacter(character)

        assertTrue(result is Success)
        assertEquals(character, result.getOrNull())

        characterRepo.characterFlow.test {
            characterRepo.fetchCharacterById(character.id)

            val lastItem = expectMostRecentItem()
            assertEquals(character.id, lastItem.first)
            assertTrue(lastItem.second is Success)
            assertEquals(character.name, lastItem.second.getOrNull()?.name)
        }
    }

    @Test
    fun updateCharacter_characterDoesNotExist() = runBlocking {
        val character = Character(name = "new character")

        val result = characterRepo.updateCharacter(character)

        assertTrue(result is Error)
        assertEquals(400, (result as Error).statusCode)
    }

    @Test
    fun removeCharacterById_characterExists() = runBlocking {
        val characterId = RemoteCharacterDataSourceMocked.DEFAULT_CHARACTERS.first().id

        val result = characterRepo.removeCharacterById(characterId)
        assertTrue(result is Success)

        characterRepo.characterFlow.test {
            characterRepo.fetchCharacterById(characterId)

            val lastItem = expectMostRecentItem()
            assertEquals(characterId, lastItem.first)
            assertTrue(lastItem.second is Error)
            assertEquals(404, (lastItem.second as Error).statusCode)
        }
    }

    @Test
    fun removeCharacterById_characterDoesNotExist() = runBlocking {
        val characterId = "not a real id"

        val result = characterRepo.removeCharacterById(characterId)
        assertTrue(result is Error)
        assertEquals(400, (result as Error).statusCode)
    }

    @Test
    fun cacheCharacter_BACKUP_and_getCachedCharacterById_BACKUP() = runBlocking {
        val character = RemoteCharacterDataSourceMocked.DEFAULT_CHARACTERS.first()

        val cacheResult = characterRepo.cacheCharacter(character, CacheType.BACKUP)
        assertTrue(cacheResult is Success)

        val getResult = characterRepo.getCachedCharacterById(character.id, CacheType.BACKUP)
        assertTrue(getResult is Success)
        assertEquals(character, getResult.getOrNull())
    }

    @Test
    fun cacheCharacter_BACKUP_backupAlreadyExists() = runBlocking {
        val character = RemoteCharacterDataSourceMocked.DEFAULT_CHARACTERS.first()
        characterRepo.cacheCharacter(character, CacheType.BACKUP)

        val updatedCharacter = character.copy(name = "some other name")
        val cacheResult = characterRepo.cacheCharacter(updatedCharacter, CacheType.BACKUP)
        assertTrue(cacheResult is Success)

        val getResult = characterRepo.getCachedCharacterById(updatedCharacter.id, CacheType.BACKUP)
        assertTrue(getResult is Success)
        assertEquals(updatedCharacter, getResult.getOrNull())
    }

    @Test
    fun cacheCharacter_EDITS_and_getCachedCharacterById_EDITS() = runBlocking {
        val character = RemoteCharacterDataSourceMocked.DEFAULT_CHARACTERS.first()

        val cacheResult = characterRepo.cacheCharacter(character, CacheType.EDITS)
        assertTrue(cacheResult is Success)

        val getResult = characterRepo.getCachedCharacterById(character.id, CacheType.EDITS)
        assertTrue(getResult is Success)
        assertEquals(character, getResult.getOrNull())
    }

    @Test
    fun cacheCharacter_EDITS_editsAlreadyExist() = runBlocking {
        val character = RemoteCharacterDataSourceMocked.DEFAULT_CHARACTERS.first()
        characterRepo.cacheCharacter(character, CacheType.EDITS)

        val updatedCharacter = character.copy(name = "some other name")
        val cacheResult = characterRepo.cacheCharacter(updatedCharacter, CacheType.EDITS)
        assertTrue(cacheResult is Success)

        val getResult = characterRepo.getCachedCharacterById(updatedCharacter.id, CacheType.EDITS)
        assertTrue(getResult is Success)
        assertEquals(updatedCharacter, getResult.getOrNull())
    }

    @Test
    fun getCachedCharacterById_BACKUP_backupDoesNotExist() = runBlocking {
        val result = characterRepo.getCachedCharacterById("fake id", CacheType.BACKUP)

        assertTrue(result is Error)
        assertEquals(404, (result as Error).statusCode)
    }

    @Test
    fun getCachedCharacterById_BACKUP_editsExistButBackupDoesNot() = runBlocking {
        val character = RemoteCharacterDataSourceMocked.DEFAULT_CHARACTERS.first()
        characterRepo.cacheCharacter(character, CacheType.EDITS)

        val result = characterRepo.getCachedCharacterById(character.id, CacheType.BACKUP)
        assertTrue(result is Error)
        assertEquals(404, (result as Error).statusCode)
    }

    @Test
    fun getCachedCharacterById_EDITS_editsDoNotExist() = runBlocking {
        val result = characterRepo.getCachedCharacterById("fake id", CacheType.EDITS)

        assertTrue(result is Error)
        assertEquals(404, (result as Error).statusCode)
    }

    @Test
    fun getCachedCharacterById_EDITS_backupExistsButEditsDoNot() = runBlocking {
        val character = RemoteCharacterDataSourceMocked.DEFAULT_CHARACTERS.first()
        characterRepo.cacheCharacter(character, CacheType.BACKUP)

        val result = characterRepo.getCachedCharacterById(character.id, CacheType.EDITS)
        assertTrue(result is Error)
        assertEquals(404, (result as Error).statusCode)
    }

    @Test
    fun deleteCachedCharacterById_BACKUP() = runBlocking {
        val character = RemoteCharacterDataSourceMocked.DEFAULT_CHARACTERS.first()
        characterRepo.cacheCharacter(character, CacheType.BACKUP)

        val deleteResult = characterRepo.deleteCachedCharacterById(character.id, CacheType.BACKUP)
        assertTrue(deleteResult is Success)

        val getResult = characterRepo.getCachedCharacterById(character.id, CacheType.BACKUP)
        assertTrue(getResult is Error)
        assertEquals(404, (getResult as Error).statusCode)
    }

    @Test
    fun deleteCachedCharacterById_BACKUP_backupDoesNotExist() = runBlocking {
        val result = characterRepo.deleteCachedCharacterById("fake id", CacheType.BACKUP)
        assertTrue(result is Success)
    }

    @Test
    fun deleteCachedCharacterById_EDITS() = runBlocking {
        val character = RemoteCharacterDataSourceMocked.DEFAULT_CHARACTERS.first()
        characterRepo.cacheCharacter(character, CacheType.EDITS)

        val deleteResult = characterRepo.deleteCachedCharacterById(character.id, CacheType.EDITS)
        assertTrue(deleteResult is Success)

        val getResult = characterRepo.getCachedCharacterById(character.id, CacheType.EDITS)
        assertTrue(getResult is Error)
        assertEquals(404, (getResult as Error).statusCode)
    }

    @Test
    fun deleteCachedCharacterById_EDITS_editsDoNotExist() = runBlocking {
        val result = characterRepo.deleteCachedCharacterById("fake id", CacheType.EDITS)
        assertTrue(result is Success)
    }

    @Test
    fun clearCache() = runBlocking {
        RemoteCharacterDataSourceMocked.DEFAULT_CHARACTERS.subList(0, 2).forEach {
            characterRepo.cacheCharacter(it, CacheType.BACKUP)
            characterRepo.cacheCharacter(it, CacheType.EDITS)
        }

        val clearResult = characterRepo.clearCache()
        assertTrue(clearResult is Success)

        RemoteCharacterDataSourceMocked.DEFAULT_CHARACTERS.subList(0, 3).forEach {
            val getBackupResult = characterRepo.getCachedCharacterById(it.id, CacheType.BACKUP)
            assertTrue(getBackupResult is Error)
            assertEquals(404, (getBackupResult as Error).statusCode)

            val getEditsResult = characterRepo.getCachedCharacterById(it.id, CacheType.EDITS)
            assertTrue(getEditsResult is Error)
            assertEquals(404, (getEditsResult as Error).statusCode)
        }
    }

    @Test
    fun cacheHasData_cacheHasNoData() = runBlocking {
        val result = characterRepo.cacheHasData()

        assertTrue(result is Success)
        assertFalse((result as Success).value)
    }

    @Test
    fun cacheHasData_cacheHasData() = runBlocking {
        val character = RemoteCharacterDataSourceMocked.DEFAULT_CHARACTERS.first()
        characterRepo.cacheCharacter(character, CacheType.BACKUP)

        val result = characterRepo.cacheHasData()

        assertTrue(result is Success)
        assertTrue((result as Success).value)
    }
}