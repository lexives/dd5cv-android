package com.delarax.dd5cv.data.database

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.delarax.dd5cv.data.characters.local.CharacterDAO
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
internal abstract class AppDatabaseTest {

    lateinit var db: AppDatabase
    lateinit var characterDAO: CharacterDAO

    @Before
    fun createDb() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(appContext, AppDatabase::class.java).build()
        characterDAO = db.characterDAO()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }
}