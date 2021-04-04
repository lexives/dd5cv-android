package com.delarax.dd5cv.models

import java.util.*

data class Character (
    val id: String = UUID.randomUUID().toString(),
    val name: String? = null
)