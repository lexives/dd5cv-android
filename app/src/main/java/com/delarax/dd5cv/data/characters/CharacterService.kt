package com.delarax.dd5cv.data.characters

import com.delarax.dd5cv.models.ErrorModel
import com.delarax.dd5cv.models.characters.Character
import com.delarax.dd5cv.utils.retrofit.ServiceResponse
import retrofit2.http.GET

interface CharacterService {

    @GET("/characters")
    suspend fun getAllCharacters(): ServiceResponse<List<Character>, ErrorModel>
}