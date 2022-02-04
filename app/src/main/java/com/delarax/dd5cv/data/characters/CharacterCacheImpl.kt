package com.delarax.dd5cv.data.characters

import com.delarax.dd5cv.data.characters.local.LocalCharacterDataSource
import com.delarax.dd5cv.models.characters.Character
import com.delarax.dd5cv.models.data.CacheType
import com.delarax.dd5cv.models.data.State
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class CharacterCacheImpl @Inject constructor(
    private val localDataSource: LocalCharacterDataSource
) : CharacterCache {
    private val _inProgressCharacterIdFlow: MutableStateFlow<String?> = MutableStateFlow(null)
    override val inProgressCharacterIdFlow: StateFlow<String?> = _inProgressCharacterIdFlow

    init {
        runBlocking {
            coroutineScope {
                _inProgressCharacterIdFlow.emit(
                    getAllEdits().getOrNull()?.firstOrNull()?.id
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

    override suspend fun getCharacterById(id: String, type: CacheType): State<Character> {
        val cacheId = getCacheId(id, type)
        val character = localDataSource.getCharacterById(cacheId)
        return character.mapSuccess {
            it.copy(id = getIdFromCacheId(cacheId))
        }
    }

    override suspend fun getAllBackups(): State<List<Character>> =
        getAllCachedCharacters(CacheType.BACKUP)

    override suspend fun getAllEdits(): State<List<Character>> =
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

    override suspend fun deleteCharacterById(id: String, type: CacheType): State<Unit> {
        val cacheId = getCacheId(id, type)
        val result = localDataSource.deleteCharacterById(cacheId)

        if (getAllEdits().getOrDefault(listOf()).isEmpty()) {
            _inProgressCharacterIdFlow.emit(null)
        }

        return result
    }

    override suspend fun clear(): State<Unit> {
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