package com.delarax.dd5cv.data.characters.api

import com.delarax.dd5cv.models.characters.Character
import retrofit2.Response
import retrofit2.http.GET

interface CharacterService {

    @GET("/characters")
    suspend fun getAllCharacters(): Response<List<Character>>
}