package com.delarax.dd5cv.extensions

fun String.capitalize(): String = this[0].uppercaseChar() + this.substring(1).lowercase()

fun String.enumCaseToTitleCase(): String = this.split("_")
    .joinToString(separator = " ") {
        it.capitalize()
    }

fun String?.toIntOrZero(): Int = this?.toIntOrNull() ?: 0
