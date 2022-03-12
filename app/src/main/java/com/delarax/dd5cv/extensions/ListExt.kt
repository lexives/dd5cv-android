package com.delarax.dd5cv.extensions

import com.delarax.dd5cv.models.characters.Character
import com.delarax.dd5cv.models.characters.CharacterSummary

fun List<Character>.toCharacterSummaryList(): List<CharacterSummary> =
    this.map { it.toSummary() }

fun <T> List<T>.containsExactly(other: Collection<T>): Boolean {
    return this.size == other.size && this.containsAll(other)
}

fun <T> List<T>.mutate(block: MutableList<T>.() -> Unit): List<T> =
    this.toMutableList().apply(block).toList()