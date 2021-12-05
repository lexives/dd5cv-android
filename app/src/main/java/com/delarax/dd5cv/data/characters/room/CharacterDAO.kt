package com.delarax.dd5cv.data.characters.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
internal interface CharacterDAO {
    @Query("SELECT * FROM characterEntity WHERE id=:id")
    fun getById(id: String): CharacterEntity

    @Insert
    fun insert(character: CharacterEntity)

    @Update
    fun update(character: CharacterEntity)

    @Query("DELETE FROM characterEntity WHERE id=:id")
    fun deleteCharacterById(id: String)
}