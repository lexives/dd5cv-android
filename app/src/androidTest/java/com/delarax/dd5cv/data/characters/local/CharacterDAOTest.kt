package com.delarax.dd5cv.data.characters.local

import com.delarax.dd5cv.data.database.AppDatabaseTest
import com.delarax.dd5cv.models.characters.Ability
import com.delarax.dd5cv.models.characters.CharacterClassLevel
import com.delarax.dd5cv.models.characters.DeathSave
import com.delarax.dd5cv.models.characters.Proficiency
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

internal class CharacterDAOTest : AppDatabaseTest() {

    private val characterEntity = CharacterEntity(
        id = "id",
        name = "name",
        alignment = "alignment",
        faith = "faith",
        size = "size",
        gender = "gender",
        age = "age",
        height = "height",
        weight = "weight",
        eyes = "eyes",
        skin = "skin",
        hair = "heir",
        languages = listOf("language 1", "language 2"),
        personalityTraits = listOf("trait 1", "trait 2"),
        ideals = listOf("ideal 1", "ideal 2") ,
        bonds = listOf("bond 1", "bond 2"),
        flaws = listOf("flaw 1", "flaw 2"),
        currentXP = 10,
        nextLevelXP = 20,
        inspiration = true,
        abilityScores = mapOf(
            (Ability("ability 1", "abbrev 1") to 10),
            (Ability("ability 2", "abbrev 2") to 15)
        ),
        savingThrows = listOf(
            Proficiency(
                name = "save 1",
                ability = Ability("ability 1", "abbrev 1"),
                isProficient = true,
                isExpert = true,
                override = "+5"
            ),
            Proficiency(
                name = "save 2",
                ability = Ability("ability 2", "abbrev 2"),
                isProficient = true,
                isExpert = true,
                override = "+10"
            )
        ),
        skills = listOf(
            Proficiency(
                name = "skill 1",
                ability = Ability("ability 1", "abbrev 1"),
                isProficient = true,
                isExpert = true,
                override = "+5"
            ),
            Proficiency(
                name = "skill 2",
                ability = Ability("ability 2", "abbrev 2"),
                isProficient = true,
                isExpert = true,
                override = "+10"
            )
        ),
        passiveWisdomOverride = 10,
        armorClassOverride = 20,
        initiativeOverride = "+5",
        speed = 30,
        flySpeed = 20,
        climbSpeed = 10,
        swimSpeed = 20,
        burrowSpeed = 10,
        maxHP = 50,
        currentHP = 40,
        temporaryHP = 10,
        deathSaveSuccesses = DeathSave(
            first = true,
            second = false,
            third = true
        ),
        deathSaveFailures = DeathSave(
            first = true,
            second = false,
            third = false
        ),
        notes = listOf("note 1", "note 2"),
        classes = listOf(
            CharacterClassLevel("class 1", 1),
            CharacterClassLevel("class 2", 2)
        ),
    )

    @Test
    @Throws(Exception::class)
    fun getCharacterThatDoesNotExist() = runBlocking {
        val result = characterDAO.getById(characterEntity.id)
        assertEquals(null, result)
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetCharacter() = runBlocking {
        characterDAO.insert(characterEntity)
        val result = characterDAO.getById(characterEntity.id)

        assertEquals(characterEntity, result)
    }

    @Test
    @Throws(Exception::class)
    fun getAll_hasNoData() = runBlocking {
        val result = characterDAO.getAll()

        assertTrue(result.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun getAll_hasData() = runBlocking {
        val characterEntity2 = CharacterEntity(name = "test character 2")
        characterDAO.insert(characterEntity)
        characterDAO.insert(characterEntity2)
        val expectedResult = listOf(characterEntity, characterEntity2)

        val result = characterDAO.getAll()

        assertEquals(2, result.size)
        assertTrue(result.containsAll(expectedResult))
    }

    @Test
    @Throws(Exception::class)
    fun insertCharacterThatAlreadyExists() = runBlocking {
        val sameCharacterDifferentName = characterEntity.copy(name = "something else")

        characterDAO.insert(characterEntity)
        characterDAO.insert(sameCharacterDifferentName)
        val result = characterDAO.getById(characterEntity.id)

        assertEquals(characterEntity, result)
    }

    @Test
    @Throws(Exception::class)
    fun updateCharacter() = runBlocking {
        val expectedCharacterEntity = characterEntity.copy(name = "something else")

        characterDAO.insert(characterEntity)
        characterDAO.update(expectedCharacterEntity)
        val result = characterDAO.getById(characterEntity.id)

        assertEquals(expectedCharacterEntity, result)
    }

    @Test
    @Throws(Exception::class)
    fun updateCharacterThatDoesNotExist() = runBlocking {
        characterDAO.update(characterEntity)
        val result = characterDAO.getById(characterEntity.id)

        assertEquals(null, result)
    }

    @Test
    @Throws(Exception::class)
    fun deleteCharacter() = runBlocking {
        characterDAO.insert(characterEntity)
        characterDAO.deleteCharacterById(characterEntity.id)
        val result = characterDAO.getById(characterEntity.id)

        assertEquals(null, result)
    }

    @Test
    @Throws(Exception::class)
    fun deleteCharacterThatDoesNotExist() = runBlocking {
        characterDAO.deleteCharacterById(characterEntity.id)
        val result = characterDAO.getById(characterEntity.id)

        assertEquals(null, result)
    }

    @Test
    @Throws(Exception::class)
    fun deleteAll() = runBlocking {
        characterDAO.insert(characterEntity)
        characterDAO.insert(characterEntity.copy(id = "different id"))
        characterDAO.deleteAll()

        val result = characterDAO.getById(characterEntity.id)

        assertEquals(null, result)
    }
}