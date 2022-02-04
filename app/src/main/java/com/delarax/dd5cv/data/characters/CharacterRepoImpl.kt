package com.delarax.dd5cv.data.characters

import com.delarax.dd5cv.data.characters.local.LocalCharacterDataSource
import com.delarax.dd5cv.data.characters.remote.RemoteCharacterDataSource
import com.delarax.dd5cv.models.characters.Character
import com.delarax.dd5cv.models.characters.CharacterSummary
import com.delarax.dd5cv.models.data.CacheType
import com.delarax.dd5cv.models.data.State
import com.delarax.dd5cv.models.data.State.Loading
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class CharacterRepoImpl @Inject constructor(
    private val remoteDataSource: RemoteCharacterDataSource,
    private val localDataSource: LocalCharacterDataSource
) : CharacterRepo {
    /**************************************** Remote **********************************************/
    private val _allCharactersFlow: MutableSharedFlow<State<List<Character>>> = MutableSharedFlow()
    private val _allSummariesFlow: MutableSharedFlow<State<List<CharacterSummary>>> =
        MutableSharedFlow()
    private val _characterFlow: MutableSharedFlow<Pair<String, State<Character>>> =
        MutableSharedFlow()

    override val allCharactersFlow: SharedFlow<State<List<Character>>> = _allCharactersFlow
    override val allSummariesFlow: SharedFlow<State<List<CharacterSummary>>> = _allSummariesFlow
    override val characterFlow: SharedFlow<Pair<String, State<Character>>> = _characterFlow

    override  suspend fun fetchAllCharacters() {
        _allCharactersFlow.emit(Loading())
        _allCharactersFlow.emit(remoteDataSource.getAllCharacters())
    }

    override suspend fun fetchAllCharacterSummaries() {
        _allSummariesFlow.emit(Loading())
        _allSummariesFlow.emit(remoteDataSource.getAllCharacterSummaries())
    }

    override suspend fun fetchCharacterById(id: String) {
        _characterFlow.emit(id to Loading())
        _characterFlow.emit(id to remoteDataSource.getCharacterById(id))
    }

    override suspend fun addCharacter(character: Character): State<Character> {
        return remoteDataSource.addCharacter(character)
    }

    override suspend fun updateCharacter(character: Character): State<Character> {
        return remoteDataSource.updateCharacter(character)
    }

    override suspend fun removeCharacterById(id: String): State<Unit> {
        return remoteDataSource.removeCharacterById(id)
    }

    /**************************************** Local ***********************************************/
    private val _inProgressCharacterIdFlow: MutableStateFlow<String?> = MutableStateFlow(null)
    override val inProgressCharacterIdFlow: StateFlow<String?> = _inProgressCharacterIdFlow

    init {
        runBlocking {
            coroutineScope {
                _inProgressCharacterIdFlow.emit(
                    getAllCachedCharacterEdits().getOrNull()?.firstOrNull()?.id
                )
            }
        }
    }

    override suspend fun cacheCharacter(character: Character, type: CacheType): State<Unit> {
        val cacheId = getCacheId(character.id, type)
        val characterToCache = character.copy(id = cacheId)

        val characterAlreadyCached: Boolean =
            localDataSource.getCharacterById(cacheId) is State.Success

        val result = if (characterAlreadyCached) {
            localDataSource.updateCharacter(characterToCache)
        } else {
            localDataSource.insertCharacter(characterToCache)
        }

        return if (result is State.Error) {
            State.Error(Throwable("Error caching character with id ${character.id}"))
        } else {
            if (type == CacheType.EDITS) { _inProgressCharacterIdFlow.emit(character.id) }
            State.Success(Unit)
        }
    }

    override suspend fun getCachedCharacterById(id: String, type: CacheType): State<Character> {
        val cacheId = getCacheId(id, type)
        val character = localDataSource.getCharacterById(cacheId)
        return character.mapSuccess {
            it.copy(id = getIdFromCacheId(cacheId))
        }
    }

    override suspend fun getAllCachedCharacterBackups(): State<List<Character>> =
        getAllCachedCharacters(CacheType.BACKUP)

    override suspend fun getAllCachedCharacterEdits(): State<List<Character>> =
        getAllCachedCharacters(CacheType.EDITS)

    private suspend fun getAllCachedCharacters(type: CacheType): State<List<Character>> {
        return localDataSource.getAllCharacters().mapSuccess { list ->
            list.filter {
                it.id.startsWith(type.toString())
            }.map {
                it.copy(id = getIdFromCacheId(it.id))
            }
        }
    }

    override suspend fun deleteCachedCharacterById(id: String, type: CacheType): State<Unit> {
        val cacheId = getCacheId(id, type)
        val result = localDataSource.deleteCharacterById(cacheId)

        if (getAllCachedCharacterEdits().getOrDefault(listOf()).isEmpty()) {
            _inProgressCharacterIdFlow.emit(null)
        }

        return result
    }

    override suspend fun clearCache(): State<Unit> {
        val result = localDataSource.deleteAll()

        if (result is State.Success) {
            _inProgressCharacterIdFlow.emit(null)
        }

        return result
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