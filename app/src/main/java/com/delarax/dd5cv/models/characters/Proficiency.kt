package com.delarax.dd5cv.models.characters

data class Proficiency(
    val name: String,
    val ability: Ability,
    val isProficient: Boolean = false,
    val isExpert: Boolean = false,
    val override: Int? = null
)
