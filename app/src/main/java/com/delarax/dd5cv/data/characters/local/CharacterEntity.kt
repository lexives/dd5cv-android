package com.delarax.dd5cv.data.characters.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.delarax.dd5cv.models.characters.Ability
import com.delarax.dd5cv.models.characters.Character
import com.delarax.dd5cv.models.characters.CharacterClassLevel
import com.delarax.dd5cv.models.characters.DeathSave
import com.delarax.dd5cv.models.characters.Proficiency
import java.util.*

@Entity
internal data class CharacterEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),

    val name: String = "",
    val alignment: String = "",
    val faith: String = "",
    val size: String = "",
    val gender: String = "",
    val age: String = "",
    val height: String = "",
    val weight: String = "",
    val eyes: String = "",
    val skin: String = "",
    val hair: String = "",

    val languages: List<String> = listOf(),

    val personalityTraits: List<String> = listOf(),
    val ideals: List<String> = listOf(),
    val bonds: List<String> = listOf(),
    val flaws: List<String> = listOf(),

    val currentXP: Int? = null,
    val nextLevelXP: Int? = null,

    val inspiration: Boolean = false,

    val abilityScores: Map<Ability, Int?> = mapOf(),
    val savingThrows: List<Proficiency> = listOf(),
    val skills: List<Proficiency> = listOf(),

    val passiveWisdomOverride: Int? = null,
    val armorClassOverride: Int? = null,
    val initiativeOverride: Int? = null,
    val proficiencyBonusOverride: Int? = null,

    val speed: Int? = null,
    val flySpeed: Int? = null,
    val climbSpeed: Int? = null,
    val swimSpeed: Int? = null,
    val burrowSpeed: Int? = null,

    val maxHP: Int? = null,
    val currentHP: Int? = null,
    val temporaryHP: Int? = null,

    val deathSaveSuccesses: DeathSave = DeathSave(),
    val deathSaveFailures: DeathSave = DeathSave(),

    val notes: List<String> = listOf(),

    val classes: List<CharacterClassLevel> = listOf()
) {
    fun toCharacter() = Character(
        id = id,
        name = name,
        alignment = alignment,
        faith = faith,
        size = size,
        gender = gender,
        age = age,
        height = height,
        weight = weight,
        eyes = eyes,
        skin = skin,
        hair = hair,
        languages = languages,
        personalityTraits = personalityTraits,
        ideals = ideals,
        bonds = bonds,
        flaws = flaws,
        currentXP = currentXP,
        nextLevelXP = nextLevelXP,
        inspiration = inspiration,
        abilityScores = abilityScores,
        savingThrows = savingThrows,
        skills = skills,
        passiveWisdomOverride = passiveWisdomOverride,
        armorClassOverride = armorClassOverride,
        initiativeOverride = initiativeOverride,
        proficiencyBonusOverride = proficiencyBonusOverride,
        speed = speed,
        flySpeed = flySpeed,
        climbSpeed = climbSpeed,
        swimSpeed = swimSpeed,
        burrowSpeed = burrowSpeed,
        maxHP = maxHP,
        currentHP = currentHP,
        temporaryHP = temporaryHP,
        deathSaveSuccesses = deathSaveSuccesses,
        deathSaveFailures = deathSaveFailures,
        notes = notes,
        classes = classes
    )

    companion object {
        fun from(character: Character) = CharacterEntity(
            id = character.id,
            name = character.name,
            alignment = character.alignment,
            faith = character.faith,
            size = character.size,
            gender = character.gender,
            age = character.age,
            height = character.height,
            weight = character.weight,
            eyes = character.eyes,
            skin = character.skin,
            hair = character.hair,
            languages = character.languages,
            personalityTraits = character.personalityTraits,
            ideals = character.ideals,
            bonds = character.bonds,
            flaws = character.flaws,
            currentXP = character.currentXP,
            nextLevelXP = character.nextLevelXP,
            inspiration = character.inspiration,
            abilityScores = character.abilityScores,
            savingThrows = character.savingThrows,
            skills = character.skills,
            passiveWisdomOverride = character.passiveWisdomOverride,
            armorClassOverride = character.armorClassOverride,
            initiativeOverride = character.initiativeOverride,
            proficiencyBonusOverride = character.proficiencyBonusOverride,
            speed = character.speed,
            flySpeed = character.flySpeed,
            climbSpeed = character.climbSpeed,
            swimSpeed = character.swimSpeed,
            burrowSpeed = character.burrowSpeed,
            maxHP = character.maxHP,
            currentHP = character.currentHP,
            temporaryHP = character.temporaryHP,
            deathSaveSuccesses = character.deathSaveSuccesses,
            deathSaveFailures = character.deathSaveFailures,
            notes = character.notes,
            classes = character.classes
        )
    }
}
