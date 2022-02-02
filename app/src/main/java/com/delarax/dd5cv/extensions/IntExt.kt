package com.delarax.dd5cv.extensions

fun Int.toBonus(): String = when {
    this == 0 -> "0"
    this < 0 -> "-${this}"
    else -> "+${this}"
}

fun Int?.toBonus(): String = (this ?: 0).toBonus()

