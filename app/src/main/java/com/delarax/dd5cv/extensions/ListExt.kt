package com.delarax.dd5cv.extensions

import com.delarax.dd5cv.models.characters.Character
import com.delarax.dd5cv.models.characters.CharacterSummary

fun List<Character>.toCharacterSummaryList(): List<CharacterSummary> =
    this.map { it.toSummary() }