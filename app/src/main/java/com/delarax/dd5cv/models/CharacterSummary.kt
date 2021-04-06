package com.delarax.dd5cv.models

data class CharacterSummary(
    val id: String,
    val name: String? = null,
    val classes: List<CharacterClassLevel> = listOf(),
)