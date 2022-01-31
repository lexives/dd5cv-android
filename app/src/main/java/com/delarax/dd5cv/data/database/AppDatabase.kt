package com.delarax.dd5cv.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.delarax.dd5cv.data.characters.local.CharacterDAO
import com.delarax.dd5cv.data.characters.local.CharacterEntity
import javax.inject.Singleton

@Database(entities = [CharacterEntity::class], version = 1)
@TypeConverters(Converters::class)
@Singleton
abstract class AppDatabase : RoomDatabase() {
    internal abstract fun characterDAO(): CharacterDAO
}