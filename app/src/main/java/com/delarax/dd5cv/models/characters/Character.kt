package com.delarax.dd5cv.models.characters

import com.delarax.dd5cv.extensions.calculateBonusOrZero
import com.delarax.dd5cv.extensions.containsExactly
import com.google.gson.annotations.SerializedName
import java.util.*

data class Character (
    @SerializedName("_id")
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

    val abilityScores: Map<Ability, Int?> = CharacterDefaults.abilities.associateWith { null },
    val savingThrows: List<Proficiency> = CharacterDefaults.savingThrows,
    val skills: List<Proficiency> = CharacterDefaults.skills,

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
    val totalLevel: Int = classes
        .map { it.level }
        .fold(0) { acc, level -> acc + level }

    val classNamesString: String = classes
        .map { it.name }
        .fold("") { acc, className ->
            val classNameToDisplay = className.ifEmpty { "Unnamed Class" }
            when {
                acc.isEmpty() ->  classNameToDisplay
                else ->  "${acc}/${classNameToDisplay}"
            }
        }

    val skillBonuses: List<Int> = skills.map {
        val proficiencyBonus = proficiencyBonusOverride ?: 0
        it.override ?: abilityScores[it.ability].calculateBonusOrZero() + when {
            it.isExpert -> proficiencyBonus * 2
            it.isProficient -> proficiencyBonus
            else -> 0
        }
    }

    fun toSummary(): CharacterSummary = CharacterSummary(
        id = this.id,
        name = this.name,
        classes = classes
    )

    override fun equals(other: Any?): Boolean {
        (other as? Character)?.let {
            return (
                this.id == other.id &&
                this.name == other.name &&
                this.alignment == other.alignment &&
                this.faith == other.faith &&
                this.size == other.size &&
                this.gender == other.gender &&
                this.age == other.age &&
                this.height == other.height &&
                this.weight == other.weight &&
                this.eyes == other.eyes &&
                this.skin == other.skin &&
                this.hair == other.hair &&
                this.languages.containsExactly(other.languages) &&
                this.personalityTraits.containsExactly(other.personalityTraits) &&
                this.ideals.containsExactly(other.ideals) &&
                this.bonds.containsExactly(other.bonds) &&
                this.flaws.containsExactly(other.flaws) &&
                this.currentXP == other.currentXP &&
                this.nextLevelXP == other.nextLevelXP &&
                this.inspiration == other.inspiration &&
                this.abilityScores == other.abilityScores &&
                this.savingThrows.containsExactly(other.savingThrows) &&
                this.skills.containsExactly(other.skills) &&
                this.passiveWisdomOverride == other.passiveWisdomOverride &&
                this.armorClassOverride == other.armorClassOverride &&
                this.initiativeOverride == other.initiativeOverride &&
                this.proficiencyBonusOverride == other.proficiencyBonusOverride &&
                this.speed == other.speed &&
                this.flySpeed == other.flySpeed &&
                this.climbSpeed == other.climbSpeed &&
                this.swimSpeed == other.swimSpeed &&
                this.burrowSpeed == other.burrowSpeed &&
                this.maxHP == other.maxHP &&
                this.currentHP == other.currentHP &&
                this.temporaryHP == other.temporaryHP &&
                this.deathSaveSuccesses == other.deathSaveSuccesses &&
                this.deathSaveFailures == other.deathSaveFailures &&
                this.notes.containsExactly(other.notes) &&
                this.classes.containsExactly(other.classes)
            )
        } ?: return false
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + alignment.hashCode()
        result = 31 * result + faith.hashCode()
        result = 31 * result + size.hashCode()
        result = 31 * result + gender.hashCode()
        result = 31 * result + age.hashCode()
        result = 31 * result + height.hashCode()
        result = 31 * result + weight.hashCode()
        result = 31 * result + eyes.hashCode()
        result = 31 * result + skin.hashCode()
        result = 31 * result + hair.hashCode()
        result = 31 * result + languages.hashCode()
        result = 31 * result + personalityTraits.hashCode()
        result = 31 * result + ideals.hashCode()
        result = 31 * result + bonds.hashCode()
        result = 31 * result + flaws.hashCode()
        result = 31 * result + (currentXP ?: 0)
        result = 31 * result + (nextLevelXP ?: 0)
        result = 31 * result + inspiration.hashCode()
        result = 31 * result + abilityScores.hashCode()
        result = 31 * result + savingThrows.hashCode()
        result = 31 * result + skills.hashCode()
        result = 31 * result + (passiveWisdomOverride ?: 0)
        result = 31 * result + (armorClassOverride ?: 0)
        result = 31 * result + (initiativeOverride?.hashCode() ?: 0)
        result = 31 * result + (proficiencyBonusOverride?.hashCode() ?: 0)
        result = 31 * result + (speed ?: 0)
        result = 31 * result + (flySpeed ?: 0)
        result = 31 * result + (climbSpeed ?: 0)
        result = 31 * result + (swimSpeed ?: 0)
        result = 31 * result + (burrowSpeed ?: 0)
        result = 31 * result + (maxHP ?: 0)
        result = 31 * result + (currentHP ?: 0)
        result = 31 * result + (temporaryHP ?: 0)
        result = 31 * result + deathSaveSuccesses.hashCode()
        result = 31 * result + deathSaveFailures.hashCode()
        result = 31 * result + notes.hashCode()
        result = 31 * result + classes.hashCode()
        result = 31 * result + totalLevel
        result = 31 * result + classNamesString.hashCode()
        return result
    }
}