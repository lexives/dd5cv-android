package com.delarax.dd5cv.extensions

fun Int?.toStringOrEmpty(): String = this?.toString() ?: ""

fun Int?.toStringOrDefault(default: String): String = this?.toString() ?: default

fun Int.toBonus(): String = when {
    this == 0 -> "0"
    this < 0 -> "-${this}"
    else -> "+${this}"
}

fun Int?.toBonusOrEmpty(): String = this?.toBonus() ?: ""