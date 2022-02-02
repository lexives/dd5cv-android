package com.delarax.dd5cv.extensions

fun Int?.toStringOrDefault(default: String): String = this?.toString() ?: default

fun Int?.toStringOrEmpty(): String = toStringOrDefault("")

fun Int.toBonus(): String = when {
    this > 0 -> "+${this}"
    else -> this.toString()
}

fun Int?.toBonusOrDefault(default: String): String = this?.toBonus() ?: default

fun Int?.toBonusOrEmpty(): String = this.toBonusOrDefault("")