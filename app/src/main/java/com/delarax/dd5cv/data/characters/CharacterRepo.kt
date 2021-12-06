package com.delarax.dd5cv.data.characters

import com.delarax.dd5cv.data.characters.local.LocalCharacterDataSource
import com.delarax.dd5cv.data.characters.remote.RemoteCharacterDataSource
import com.delarax.dd5cv.models.State
import com.delarax.dd5cv.models.State.Loading
import com.delarax.dd5cv.models.characters.Character
import com.delarax.dd5cv.models.characters.CharacterSummary
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CharacterRepo @Inject constructor() {

    @Inject internal lateinit var remoteDataSource: RemoteCharacterDataSource
    @Inject internal lateinit var localDataSource: LocalCharacterDataSource

    /**************************************** Remote **********************************************/
    private val _allCharactersFlow: MutableSharedFlow<State<List<Character>>> = MutableSharedFlow()
    private val _allSummariesFlow: MutableSharedFlow<State<List<CharacterSummary>>> =
        MutableSharedFlow()
    private val _characterFlow: MutableSharedFlow<Pair<String, State<Character>>> =
        MutableSharedFlow()

    val allCharactersFlow: SharedFlow<State<List<Character>>> = _allCharactersFlow
    val allSummariesFlow: SharedFlow<State<List<CharacterSummary>>> = _allSummariesFlow
    val characterFlow: SharedFlow<Pair<String, State<Character>>> = _characterFlow

    suspend fun fetchAllCharacters() {
        _allCharactersFlow.emit(Loading())
        _allCharactersFlow.emit(remoteDataSource.getAllCharacters())
    }

    suspend fun fetchAllCharacterSummaries() {
        _allSummariesFlow.emit(Loading())
        _allSummariesFlow.emit(remoteDataSource.getAllCharacterSummaries())
    }

    suspend fun fetchCharacterById(id: String) {
        _characterFlow.emit(id to Loading())
        _characterFlow.emit(id to remoteDataSource.getCharacterById(id))
    }

    suspend fun addCharacter(character: Character): State<Character> {
        return remoteDataSource.addCharacter(character)
    }

    suspend fun updateCharacter(character: Character): State<Character> {
        return remoteDataSource.updateCharacter(character)
    }

    suspend fun removeCharacterById(id: String): State<Unit> {
        return remoteDataSource.removeCharacterById(id)
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