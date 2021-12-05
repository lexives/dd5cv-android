package com.delarax.dd5cv.data.characters.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
internal interface CharacterDAO {
    @Query("SELECT * FROM characterEntity WHERE id=:id")
    suspend fun getById(id: String): CharacterEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(character: CharacterEntity)

    @Update
    suspend fun update(character: CharacterEntity)

    @Query("DELETE FROM characterEntity WHERE id=:id")
    suspend fun deleteCharacterById(id: String)

    @Query("DELETE FROM characterEntity")
    suspend fun deleteAll()
}