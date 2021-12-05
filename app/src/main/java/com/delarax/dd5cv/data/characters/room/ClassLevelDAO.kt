package com.delarax.dd5cv.data.characters.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ClassLevelDAO {
    @Query("SELECT * FROM classLevelEntity WHERE characterId=:characterId")
    suspend fun getAllForCharacter(characterId: String) : List<ClassLevelEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMany(vararg classLevels: ClassLevelEntity)

    @Update
    suspend fun updateMany(vararg classLevels: ClassLevelEntity)

    @Query("DELETE FROM classLevelEntity WHERE characterId=:characterId")
    suspend fun deleteAllForCharacter(characterId: String)
}