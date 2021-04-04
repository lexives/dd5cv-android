package com.delarax.dd5cv.data

import com.delarax.dd5cv.models.Character
import javax.inject.Inject

class CharacterRepoMockData @Inject constructor() : CharacterRepo {
    private var characterList: List<Character> = DEFAULT_CHARACTERS

    override fun getAllCharacters(): List<Character> {
        return characterList
    }

    override fun addCharacter(character: Character): Character? {
        return if (!characterListContainsId(character.id)) {
            characterList.toMutableList().also {
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
            characterList.toMutableList().also {
                it[indexOfCharacter] = character
            }
            character
        } else { null }
    }

    override fun removeCharacter(id: String): Boolean {
        return if (characterListContainsId(id)) {
            characterWithId(id)?.let { character ->
                characterList.toMutableList().also {
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
            Character(name = "Holdrum"),
            Character(name = "Delarax"),
            Character(name = "Elissa")
        )
    }
}