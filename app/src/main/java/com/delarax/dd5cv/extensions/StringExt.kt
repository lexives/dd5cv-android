package com.delarax.dd5cv.extensions

import kotlin.math.min

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

/**
 * returns the first n characters of the string that are digits, where n is
 * the number of digits provided.
 *
 * If includeNegatives = true then if the string starts with a dash (-)
 * it will be ignored.
 */
fun String.filterToInt(maxDigits: Int, includeNegatives: Boolean = true): String {
    val subStringOfOnlyDigits = this.filter { it.isDigit() }
    val lastIndex = min(subStringOfOnlyDigits.length, maxDigits)
    val truncatedSubstring = subStringOfOnlyDigits.substring(0, lastIndex)

    return if (includeNegatives && this.startsWith("-")) {
        "-$truncatedSubstring"
    } else {
        truncatedSubstring
    }

}
