package com.delarax.dd5cv.extensions

fun <T, U> Map<T, U>.mutate(block: MutableMap<T, U>.() -> Unit): Map<T, U> =
    this.toMutableMap().apply(block).toMap()