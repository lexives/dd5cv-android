package com.delarax.dd5cv.data

import com.delarax.dd5cv.models.Character
import com.delarax.dd5cv.models.CharacterClassLevel
import com.delarax.dd5cv.models.CharacterSummary
import com.delarax.dd5cv.models.toCharacterSummaryList
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CharacterRepoMockData @Inject constructor() : CharacterRepo {
    private var characterList: List<Character> = DEFAULT_CHARACTERS

    override fun getAllCharacters(): List<Character> {
        return characterList
    }

    override fun getAllCharacterSummaries(): List<CharacterSummary> {
        return characterList.toCharacterSummaryList()
    }

    override fun getCharacterById(characterId: String): Character? {
        return characterWithId(characterId)
    }

    override fun addCharacter(character: Character): Character? {
        return if (!characterListContainsId(character.id)) {
            characterList = characterList.toMutableList().also {
                it.add(character)
            }
            character
        } else { null }
    }

    override fun updateCharacter(character: Character): Character? {
        val indexOfCharacter = characterList.indexOfFirst {
            it.id == character.id
        }
        return if (indexOfCharacter != -1) {
            characterList = characterList.toMutableList().also {
                it[indexOfCharacter] = character
            }
            character
        } else { null }
    }

    override fun removeCharacter(id: String): Boolean {
        return if (characterListContainsId(id)) {
            characterWithId(id)?.let { character ->
                characterList = characterList.toMutableList().also {
                    it.remove(character)
                }
                true
            } ?: false
        } else { true }
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
                        className = "Fighter",
                        level = 6
                    ),
                    CharacterClassLevel(
                        className = "Bard",
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
                        className = "Ranger"
                    ),
                    CharacterClassLevel(
                        level = 1
                    )
                )
            )
        )
    }
}