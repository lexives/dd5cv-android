package com.delarax.dd5cv.data.characters

import com.delarax.dd5cv.data.characters.local.LocalCharacterDataSource
import com.delarax.dd5cv.data.characters.remote.RemoteCharacterDataSource
import com.delarax.dd5cv.models.State
import com.delarax.dd5cv.models.characters.Character
import com.delarax.dd5cv.models.characters.CharacterSummary
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CharacterRepo @Inject constructor() {

    @Inject internal lateinit var remoteDataSource: RemoteCharacterDataSource
    @Inject internal lateinit var localDataSource: LocalCharacterDataSource

    /**************************************** Remote **********************************************/
    suspend fun getAllCharacters(): Flow<State<List<Character>>> = flow {
        TODO()
    }

    suspend fun getAllCharacterSummaries(): Flow<State<List<CharacterSummary>>> = flow {
        TODO()
    }

    suspend fun getCharacterById(id: String): Flow<State<Character>> = flow {
        TODO()
    }

    suspend fun addCharacter(character: Character): State<Character> {
        TODO()
    }

    suspend fun updateCharacter(character: Character): State<Character> {
        TODO()
    }

    suspend fun removeCharacterById(id: String): State<Unit> {
        TODO()
    }

    /**************************************** Local ***********************************************/

    suspend fun getCachedCharacterById(id: String): State<Character> {
        TODO()
    }

    suspend fun cacheCharacterBackup(character: Character): State<Unit> {
        TODO()
    }

    suspend fun cacheCharacterEdits(character: Character): State<Unit> {
        TODO()
    }

    suspend fun deleteCachedCharacterById(id: String): State<Unit> {
        TODO()
    }

    suspend fun clearCache(): State<Unit> {
        TODO()
    }
}