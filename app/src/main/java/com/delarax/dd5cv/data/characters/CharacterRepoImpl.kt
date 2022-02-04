package com.delarax.dd5cv.data.characters

import com.delarax.dd5cv.data.characters.remote.RemoteCharacterDataSource
import com.delarax.dd5cv.models.characters.Character
import com.delarax.dd5cv.models.characters.CharacterSummary
import com.delarax.dd5cv.models.data.State
import com.delarax.dd5cv.models.data.State.Loading
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class CharacterRepoImpl @Inject constructor(
    private val remoteDataSource: RemoteCharacterDataSource
) : CharacterRepo {
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
}