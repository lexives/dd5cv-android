package com.delarax.dd5cv.models.characters

data class Proficiency(
    val name: String,
    val ability: Ability,
    val proficiencyLevel: ProficiencyLevel = ProficiencyLevel.NONE,
    val override: Int? = null
)

enum class ProficiencyLevel { NONE, PROFICIENT, EXPERT }
