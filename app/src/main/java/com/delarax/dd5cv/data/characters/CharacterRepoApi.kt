package com.delarax.dd5cv.data.characters

import com.delarax.dd5cv.models.State
import com.delarax.dd5cv.models.characters.Character
import com.delarax.dd5cv.models.characters.CharacterSummary
import com.delarax.dd5cv.retrofit.mapToState
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CharacterRepoApi @Inject constructor(
    private val characterService: CharacterService
) : CharacterRepo {
    override suspend fun getAllCharacters(): State<List<Character>> =
        characterService.getAllCharacters().mapToState().flatMapSuccess {
            if (it.isNullOrEmpty()) {
                State.Empty(it)
            } else {
                State.Success(it)
            }
        }

    override suspend fun getAllCharacterSummaries(): State<List<CharacterSummary>> =
        getAllCharacters().mapSuccess { characterList ->
            characterList.map { character ->
                character.toSummary()
            }
        }

    override suspend fun getCharacterById(id: String): State<Character> =
        characterService.getCharacter(id).mapToState()

    override suspend fun addCharacter(character: Character): State<Character> =
        characterService.postCharacter(character).mapToState()

    override suspend fun updateCharacter(character: Character): State<Character> =
        characterService.patchCharacter(character).mapToState()

    override suspend fun removeCharacter(id: String): State<Unit> =
        characterService.deleteCharacter(id).mapToState()

}