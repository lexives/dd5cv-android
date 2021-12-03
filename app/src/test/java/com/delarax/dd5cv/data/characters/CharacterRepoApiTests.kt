package com.delarax.dd5cv.data.characters

import com.delarax.dd5cv.models.ErrorModel
import com.delarax.dd5cv.models.characters.Character
import com.delarax.dd5cv.models.State
import com.delarax.dd5cv.models.characters.toSummary
import com.delarax.dd5cv.retrofit.ServiceResponse
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CharacterRepoApiTests {

    @Mock
    lateinit var characterService: CharacterService

    lateinit var characterRepoApi: CharacterRepoApi

    private val characters = CharacterRepoMockData.DEFAULT_CHARACTERS

    @Before
    fun setup() {
        characterRepoApi = CharacterRepoApi(characterService)
    }

    @Test
    fun `getAllCharacters Success`() = runBlocking {
        // GIVEN
        Mockito.`when`(characterService.getAllCharacters())
            .thenReturn(ServiceResponse.ServiceSuccess(characters))

        // WHEN
        val result = characterRepoApi.getAllCharacters()

        // THEN
        assertTrue(result is State.Success)
        assertEquals(characters, result.getOrNull())
    }

    @Test
    fun `getAllCharacters Empty`() = runBlocking {
        // GIVEN
        Mockito.`when`(characterService.getAllCharacters())
            .thenReturn(ServiceResponse.ServiceSuccess(listOf()))

        // WHEN
        val result = characterRepoApi.getAllCharacters()

        // THEN
        assertTrue(result is State.Empty)
    }

    @Test
    fun `getAllCharacters Error`() = runBlocking {
        // GIVEN
        Mockito.`when`(characterService.getAllCharacters())
            .thenReturn(ServiceResponse.ServiceError(
                body = ErrorModel("not found"),
                code = 404
            ))

        // WHEN
        val result = characterRepoApi.getAllCharacters()

        // THEN
        assertTrue(result is State.Error)
        assertEquals("not found", (result as State.Error).throwable.message)
        assertEquals(404, result.statusCode)
    }

    @Test
    fun `getAllCharacterSummaries Success`() = runBlocking {
        // GIVEN
        Mockito.`when`(characterService.getAllCharacters())
            .thenReturn(ServiceResponse.ServiceSuccess(characters))

        // WHEN
        val result = characterRepoApi.getAllCharacterSummaries()

        // THEN
        assertTrue(result is State.Success)
        assertEquals(characters.map{ it.toSummary() }, result.getOrNull())
    }

    @Test
    fun `getAllCharacterSummaries Empty`() = runBlocking {
        // GIVEN
        Mockito.`when`(characterService.getAllCharacters())
            .thenReturn(ServiceResponse.ServiceSuccess(listOf()))

        // WHEN
        val result = characterRepoApi.getAllCharacterSummaries()

        // THEN
        assertTrue(result is State.Empty)
    }

    @Test
    fun `getAllCharacterSummaries Error`() = runBlocking {
        // GIVEN
        Mockito.`when`(characterService.getAllCharacters())
            .thenReturn(ServiceResponse.ServiceError(
                body = ErrorModel("not found"),
                code = 404
            ))

        // WHEN
        val result = characterRepoApi.getAllCharacterSummaries()

        // THEN
        assertTrue(result is State.Error)
        assertEquals("not found", (result as State.Error).throwable.message)
        assertEquals(404, result.statusCode)
    }

    @Test
    fun `getCharacterById Success`() = runBlocking {
        // GIVEN
        Mockito.`when`(characterService.getCharacter(characters.first().id))
            .thenReturn(ServiceResponse.ServiceSuccess(characters.first()))

        // WHEN
        val result = characterRepoApi.getCharacterById(characters.first().id)

        // THEN
        assertTrue(result is State.Success)
        assertEquals(characters.first(), result.getOrNull())
    }

    @Test
    fun `getCharacterById Error`() = runBlocking {
        // GIVEN
        Mockito.`when`(characterService.getCharacter(characters.first().id))
            .thenReturn(ServiceResponse.ServiceError(
                body = ErrorModel("not found"),
                code = 404
            ))

        // WHEN
        val result = characterRepoApi.getCharacterById(characters.first().id)

        // THEN
        assertTrue(result is State.Error)
        assertEquals("not found", (result as State.Error).throwable.message)
        assertEquals(404, result.statusCode)
    }

    @Test
    fun `addCharacter Success`() = runBlocking {
        // GIVEN
        val newCharacter = Character(name = "test")
        Mockito.`when`(characterService.postCharacter(newCharacter))
            .thenReturn(ServiceResponse.ServiceSuccess(newCharacter))

        // WHEN
        val result = characterRepoApi.addCharacter(newCharacter)

        // THEN
        assertTrue(result is State.Success)
        assertEquals(newCharacter, result.getOrNull())
    }

    @Test
    fun `addCharacter Error`() = runBlocking {
        // GIVEN
        val newCharacter = Character(name = "test")
        Mockito.`when`(characterService.postCharacter(newCharacter))
            .thenReturn(ServiceResponse.ServiceError(
                body = ErrorModel("duplicate key error"),
                code = 500
            ))

        // WHEN
        val result = characterRepoApi.addCharacter(newCharacter)

        // THEN
        assertTrue(result is State.Error)
        assertEquals("duplicate key error", (result as State.Error).throwable.message)
        assertEquals(500, result.statusCode)
    }

    @Test
    fun `updateCharacter Success`() = runBlocking {
        // GIVEN
        val editedCharacter = characters.first().copy(name = "different name")
        Mockito.`when`(characterService.patchCharacter(editedCharacter))
            .thenReturn(ServiceResponse.ServiceSuccess(editedCharacter))

        // WHEN
        val result = characterRepoApi.updateCharacter(editedCharacter)

        // THEN
        assertTrue(result is State.Success)
        assertEquals(editedCharacter, result.getOrNull())
    }

    @Test
    fun `updateCharacter Error`() = runBlocking {
        // GIVEN
        val editedCharacter = characters.first().copy(name = "different name")
        Mockito.`when`(characterService.patchCharacter(editedCharacter))
            .thenReturn(ServiceResponse.ServiceError(
                body = ErrorModel("not found"),
                code = 404
            ))

        // WHEN
        val result = characterRepoApi.updateCharacter(editedCharacter)

        // THEN
        assertTrue(result is State.Error)
        assertEquals("not found", (result as State.Error).throwable.message)
        assertEquals(404, result.statusCode)
    }

    @Test
    fun `removeCharacter Success`() = runBlocking {
        // GIVEN
        Mockito.`when`(characterService.deleteCharacter(characters.first().id))
            .thenReturn(ServiceResponse.ServiceSuccess(Unit))

        // WHEN
        val result = characterRepoApi.removeCharacter(characters.first().id)

        // THEN
        assertTrue(result is State.Success)
    }

    @Test
    fun `removeCharacter Error`() = runBlocking {
        // GIVEN
        Mockito.`when`(characterService.deleteCharacter(characters.first().id))
            .thenReturn(ServiceResponse.ServiceError(
                body = ErrorModel("not found"),
                code = 404
            ))

        // WHEN
        val result = characterRepoApi.removeCharacter(characters.first().id)

        // THEN
        assertTrue(result is State.Error)
        assertEquals("not found", (result as State.Error).throwable.message)
        assertEquals(404, result.statusCode)
    }
}