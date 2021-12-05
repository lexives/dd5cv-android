package com.delarax.dd5cv.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.delarax.dd5cv.data.characters.room.CharacterDAO
import com.delarax.dd5cv.data.characters.room.CharacterEntity
import com.delarax.dd5cv.data.characters.room.ClassLevelDAO
import com.delarax.dd5cv.data.characters.room.ClassLevelEntity

@Database(entities = [CharacterEntity::class, ClassLevelEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    internal abstract fun characterDAO(): CharacterDAO
    internal abstract fun classLevelDAO(): ClassLevelDAO
}