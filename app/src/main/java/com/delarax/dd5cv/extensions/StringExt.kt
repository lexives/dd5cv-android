package com.delarax.dd5cv.extensions

fun String.capitalize(): String = this[0].uppercaseChar() + this.substring(1).lowercase()

fun String.enumCaseToTitleCase(): String = this.split("_")
    .joinToString(separator = " ") {
        it.capitalize()
    }

fun String?.toIntOrDefault(default: Int): Int = this?.toIntOrNull() ?: default

fun String?.toIntOrZero(): Int = this.toIntOrDefault(0)

fun String.formatAsBonus(default: String): String {
    val int = this.toIntOrNull()
    return when {
        int == null -> default
        int > 0 -> "+$this"
        else -> this
    }
}

fun String.formatAsBonus(): String = formatAsBonus(this)
