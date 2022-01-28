package com.delarax.dd5cv.models.characters

import com.google.gson.annotations.SerializedName
import java.util.*

data class Character (
    @SerializedName("_id")
    val id: String = UUID.randomUUID().toString(),
    val name: String? = null,
    val classes: List<CharacterClassLevel> = listOf()
) {
    val totalLevel: Int = classes
        .map { it.level ?: 0 }
        .fold(0) { acc, level -> acc + level }

    fun toSummary(): CharacterSummary = CharacterSummary(
        id = this.id,
        name = this.name,
        classes = classes
    )
}