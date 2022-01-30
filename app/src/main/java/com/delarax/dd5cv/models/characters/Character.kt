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

    val classNamesString: String = classes
        .map { it.name }
        .fold("") { acc, className ->
            className?.let {
                if (acc.isEmpty()) {
                    it
                } else {
                    "${acc}/${it}"
                }
            } ?: acc
        }

    fun toSummary(): CharacterSummary = CharacterSummary(
        id = this.id,
        name = this.name,
        classes = classes
    )

    override fun equals(other: Any?): Boolean {
        (other as? Character)?.let {
            return (
                other.id == this.id &&
                other.name == this.name &&
                other.classes.size == this.classes.size &&
                other.classes.containsAll(this.classes)
            )
        } ?: return false
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + classes.hashCode()
        result = 31 * result + totalLevel
        result = 31 * result + classNamesString.hashCode()
        return result
    }
}