package com.delarax.dd5cv.data.characters.local.room

import androidx.room.Entity
import com.delarax.dd5cv.models.characters.CharacterClassLevel

@Entity(primaryKeys = ["characterId", "name"])
data class ClassLevelEntity(
    val characterId: String,
    val name: String,
    val level: Int?
) {
    fun toClassLevel() = CharacterClassLevel(name, level)

    companion object {
        fun from(classLevel: CharacterClassLevel, characterId: String) = ClassLevelEntity(
            characterId = characterId,
            name = classLevel.name ?: "null",
            level = classLevel.level
        )
    }
}
