package com.delarax.dd5cv.models.characters

data class DeathSave(
    val first: Boolean = false,
    val second: Boolean = false,
    val third: Boolean = false
) {
    companion object {
        val None = DeathSave()
        val First = DeathSave(first = true)
        val Second = DeathSave(first = true, second = true)
        val Third = DeathSave(first = true, second = true, third = true)
    }
}
