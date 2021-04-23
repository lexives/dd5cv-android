package com.delarax.dd5cv.data.characters

import com.delarax.dd5cv.models.ErrorModel
import com.delarax.dd5cv.models.characters.Character
import com.delarax.dd5cv.utils.retrofit.ServiceResponse
import retrofit2.http.*

interface CharacterService {

    @GET("/characters")
    suspend fun getAllCharacters(): ServiceResponse<List<Character>, ErrorModel>

    @GET("/characters/{id}")
    suspend fun getCharacter(
        @Path("id") id: String
    ): ServiceResponse<Character, ErrorModel>

    @POST("/characters")
    suspend fun postCharacter(
        @Body character: Character
    ): ServiceResponse<Character, ErrorModel>

    // TODO: currently, sending `null` for a value will update it to null. We want to not update it
    @PATCH("/characters/{id}")
    suspend fun patchCharacter(
        @Body character: Character
    ): ServiceResponse<Character, ErrorModel>

    @DELETE("/characters/{id}")
    suspend fun deleteCharacter(
        @Path("id") id: String
    ): ServiceResponse<Unit, ErrorModel>
}