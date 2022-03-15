package com.delarax.dd5cv.extensions

import com.delarax.dd5cv.models.characters.Proficiency
import com.delarax.dd5cv.models.characters.ProficiencyLevel

fun Proficiency.isProficient(): Boolean =
    this.proficiencyLevel == ProficiencyLevel.PROFICIENT
        || this.proficiencyLevel == ProficiencyLevel.EXPERT

fun Proficiency.isExpert(): Boolean = this.proficiencyLevel == ProficiencyLevel.EXPERT

fun Proficiency.getTotalBonus(proficiencyBonus: Int?): Int = when {
    proficiencyBonus == null -> 0
    this.isExpert() -> proficiencyBonus * 2
    this.isProficient() -> proficiencyBonus
    else -> 0
}