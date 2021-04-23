package com.delarax.dd5cv.models.characters

import java.util.*

data class Character (
    val id: String = UUID.randomUUID().toString(),
    val name: String? = null,
    val classes: List<CharacterClassLevel> = listOf()
)

fun Character.toSummary(): CharacterSummary = CharacterSummary(
    id = this.id,
    name = this.name,
    classes = classes
)

fun List<Character>.toCharacterSummaryList(): List<CharacterSummary> =
    this.map { it.toSummary() }