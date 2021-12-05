package com.delarax.dd5cv.data.characters.local.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.delarax.dd5cv.models.characters.Character
import com.delarax.dd5cv.models.characters.CharacterClassLevel
import java.util.*

@Entity
internal data class CharacterEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val name: String? = null
) {
    fun toCharacter(classes: List<CharacterClassLevel>) = Character(
        id = id,
        name = name,
        classes = classes
    )

    companion object {
        fun from(character: Character) = CharacterEntity(
            id = character.id,
            name = character.name
        )
    }
}
