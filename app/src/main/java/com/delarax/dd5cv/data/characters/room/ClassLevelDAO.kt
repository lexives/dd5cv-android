package com.delarax.dd5cv.data.characters.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ClassLevelDAO {
    @Query("SELECT * FROM classLevelEntity WHERE characterId=:characterId")
    fun getAllForCharacter(characterId: String) : List<ClassLevelEntity>

    @Insert
    fun insertMany(vararg classLevels: ClassLevelEntity)

    @Update
    fun updateMany(vararg classLevels: ClassLevelEntity)

    @Query("DELETE FROM classLevelEntity WHERE characterId=:characterId")
    fun deleteAllForCharacter(characterId: String)
}