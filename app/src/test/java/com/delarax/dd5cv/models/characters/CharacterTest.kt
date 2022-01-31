package com.delarax.dd5cv.models.characters

import org.junit.Assert.assertTrue
import org.junit.Assert.assertFalse
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CharacterTest {
    @Test
    fun `equals method returns true for default character with same id`() {
        val character1 = Character()
        val character2 = Character(id = character1.id)

        assertTrue(character1 == character2)
    }

    @Test
    fun `equals method returns true for exactly equal characters with all info`() {
        val character1 = Character(
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
        val character2 = Character(
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

        assertTrue(character1 == character2)
    }

    @Test
    fun `equals method returns true for characters with lists equal but in different orders`() {
        val character1 = Character(
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
        val character2 = Character(
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
            languages = listOf("language 2", "language 1"),
            personalityTraits = listOf("trait 2", "trait 1"),
            ideals = listOf("ideal 2", "ideal 1") ,
            bonds = listOf("bond 2", "bond 1"),
            flaws = listOf("flaw 2", "flaw 1"),
            currentXP = 10,
            nextLevelXP = 20,
            inspiration = true,
            abilityScores = mapOf(
                (Ability("ability 2", "abbrev 2") to 15),
                (Ability("ability 1", "abbrev 1") to 10)
            ),
            savingThrows = listOf(
                Proficiency(
                    name = "save 2",
                    ability = Ability("ability 2", "abbrev 2"),
                    isProficient = true,
                    isExpert = true,
                    override = "+10"
                ),
                Proficiency(
                    name = "save 1",
                    ability = Ability("ability 1", "abbrev 1"),
                    isProficient = true,
                    isExpert = true,
                    override = "+5"
                )
            ),
            skills = listOf(
                Proficiency(
                    name = "skill 2",
                    ability = Ability("ability 2", "abbrev 2"),
                    isProficient = true,
                    isExpert = true,
                    override = "+10"
                ),
                Proficiency(
                    name = "skill 1",
                    ability = Ability("ability 1", "abbrev 1"),
                    isProficient = true,
                    isExpert = true,
                    override = "+5"
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
            notes = listOf("note 2", "note 1"),
            classes = listOf(
                CharacterClassLevel("class 2", 2),
                CharacterClassLevel("class 1", 1)
            ),
        )

        assertTrue(character1 == character2)
    }

    @Test
    fun `equals method returns true for characters with different ids`() {
        val character1 = Character(
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
        val character2 = character1.copy(
            id = "different id"
        )

        assertFalse(character1 == character2)
    }
}