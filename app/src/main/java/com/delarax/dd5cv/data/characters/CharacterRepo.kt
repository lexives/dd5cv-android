package com.delarax.dd5cv.data.characters

import com.delarax.dd5cv.data.characters.local.LocalCharacterDataSource
import com.delarax.dd5cv.data.characters.remote.RemoteCharacterDataSource
import com.delarax.dd5cv.models.CacheType
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

    suspend fun getCharacterById(id: String): State<Character> {
        return remoteDataSource.getCharacterById(id)
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

    suspend fun cacheCharacter(character: Character, type: CacheType): State<Unit> {
        val cacheId = getCacheId(character.id, type)
        val characterToCache = character.copy(id = cacheId)

        // TODO: if already exists in cache then update, otherwise insert
        val insertResult = localDataSource.insertCharacter(characterToCache)
        val updateResult = localDataSource.updateCharacter(characterToCache)

        return if (insertResult is State.Error || updateResult is State.Error) {
            State.Error(Throwable("Error caching character with id ${character.id}"))
        } else State.Success(Unit)
    }

    suspend fun getCachedCharacterById(id: String, type: CacheType): State<Character> {
        val cacheId = getCacheId(id, type)
        val character = localDataSource.getCharacterById(cacheId)
        return character.mapSuccess {
            it.copy(id = getIdFromCacheId(cacheId))
        }
    }

    suspend fun getAllCachedCharacters(): State<List<Character>> {
        return localDataSource.getAllCharacters().mapSuccess { list ->
            list.map {
                it.copy(id = getIdFromCacheId(it.id))
            }
        }
    }

    suspend fun deleteCachedCharacterById(id: String, type: CacheType): State<Unit> {
        val cacheId = getCacheId(id, type)
        return localDataSource.deleteCharacterById(cacheId)
    }

    suspend fun clearCache(): State<Unit> {
        return localDataSource.deleteAll()
    }

    private fun getCacheId(id: String, type: CacheType): String {
        return "${type.name}-${id}"
    }

    private fun getIdFromCacheId(cacheId: String): String {
        val prefix = if (cacheId.startsWith(CacheType.BACKUP.name)) {
            "${CacheType.BACKUP.name}-"
        } else {
            "${CacheType.EDITS.name}-"
        }
        return cacheId.removePrefix(prefix)
    }
}