package com.delarax.dd5cv.data.characters

import com.delarax.dd5cv.models.characters.Character
import com.delarax.dd5cv.models.characters.CharacterClassLevel
import com.delarax.dd5cv.models.characters.CharacterSummary
import com.delarax.dd5cv.models.characters.toCharacterSummaryList
import com.delarax.dd5cv.models.State
import com.delarax.dd5cv.models.State.Success
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CharacterRepoMockData @Inject constructor() : CharacterRepo {
    private var characterList: List<Character> = DEFAULT_CHARACTERS

    override suspend fun getAllCharacters(): State<List<Character>> {
        return if (characterList.isEmpty()) {
            State.Empty(listOf())
        } else {
            Success(characterList)
        }
    }

    override suspend fun getAllCharacterSummaries(): State<List<CharacterSummary>> {
        return if (characterList.isEmpty()) {
            State.Empty(listOf())
        } else {
            Success(characterList.toCharacterSummaryList())
        }
    }

    override suspend fun getCharacterById(id: String): State<Character> {
        return characterWithId(id)?.let {
            Success(it)
        } ?: State.Error(Throwable("Could not find character with id: $id"))
    }

    override suspend fun addCharacter(character: Character): State<Character> {
        return if (!characterListContainsId(character.id)) {
            characterList = characterList.toMutableList().also {
                it.add(character)
            }
            Success(character)
        } else {
            State.Error(Throwable("Character with id ${character.id} already exists"))
        }
    }

    override suspend fun updateCharacter(character: Character): State<Character> {
        val indexOfCharacter = characterList.indexOfFirst {
            it.id == character.id
        }
        return if (indexOfCharacter != -1) {
            characterList = characterList.toMutableList().also {
                it[indexOfCharacter] = character
            }
            Success(character)
        } else {
            State.Error(Throwable("Could not find character with id: ${character.id}"))
        }
    }

    override suspend fun removeCharacter(id: String): State<Unit> {
        return if (characterListContainsId(id)) {
            characterWithId(id)?.let { character ->
                characterList = characterList.toMutableList().also {
                    it.remove(character)
                }
                Success(Unit)
            } ?: State.Error(Throwable("Could not find character with id: $id"))
        } else { Success(Unit) }
    }

    private fun characterListContainsId(id: String): Boolean {
        return characterList.map { it.id }.contains(id)
    }

    private fun characterWithId(id: String): Character? {
        return characterList.find { it.id == id }
    }

    companion object {
        val DEFAULT_CHARACTERS = listOf(
            Character(
                name = "Holdrum",
                classes = listOf(
                    CharacterClassLevel(
                        name = "Fighter",
                        level = 6
                    ),
                    CharacterClassLevel(
                        name = "Bard",
                        level = 2
                    )
                )
            ),
            Character(
                name = "Delarax"
            ),
            Character(
                name = "Elissa",
                classes = listOf(
                    CharacterClassLevel(
                        name = "Ranger"
                    ),
                    CharacterClassLevel(
                        level = 1
                    )
                )
            )
        )
    }
}