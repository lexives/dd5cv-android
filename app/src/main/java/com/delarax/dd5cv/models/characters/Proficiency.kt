package com.delarax.dd5cv.models.characters

import com.delarax.dd5cv.models.characters.ProficiencyLevel.EXPERT
import com.delarax.dd5cv.models.characters.ProficiencyLevel.PROFICIENT

data class Proficiency(
    val name: String,
    val ability: Ability,
    val proficiencyLevel: ProficiencyLevel = ProficiencyLevel.NONE,
    val override: Int? = null
) {
    @Transient
    val isProficient: Boolean = proficiencyLevel == PROFICIENT || proficiencyLevel == EXPERT
    @Transient
    val isExpert: Boolean = proficiencyLevel == EXPERT
}

enum class ProficiencyLevel { NONE, PROFICIENT, EXPERT }
