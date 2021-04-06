package com.delarax.dd5cv.models

import java.util.*

data class Character (
    val id: String = UUID.randomUUID().toString(),
    val name: String? = null
)

fun Character.toSummary(): CharacterSummary = CharacterSummary(
    id = this.id,
    name = this.name
)

fun List<Character>.toCharacterSummaryList(): List<CharacterSummary> =
    this.map { it.toSummary() }