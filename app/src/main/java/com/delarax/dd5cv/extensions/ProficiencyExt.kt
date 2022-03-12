package com.delarax.dd5cv.extensions

import com.delarax.dd5cv.models.characters.Proficiency
import com.delarax.dd5cv.models.characters.ProficiencyLevel.EXPERT
import com.delarax.dd5cv.models.characters.ProficiencyLevel.PROFICIENT

fun Proficiency.isProficient(): Boolean =
    this.proficiencyLevel == PROFICIENT || this.proficiencyLevel == EXPERT

fun Proficiency.isExpert(): Boolean = this.proficiencyLevel == EXPERT