package com.delarax.dd5cv.data.characters

import com.delarax.dd5cv.models.State.Loading
import com.delarax.dd5cv.models.State.Success
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import javax.inject.Inject

@HiltAndroidTest
@RunWith(MockitoJUnitRunner::class)
class CharacterRepoTest {

    @Rule
    @JvmField
    var hiltRule = HiltAndroidRule(this)

    @Inject lateinit var characterRepo: CharacterRepo

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun getAllCharacters() = runBlocking {
        val gatAllCharactersFlow = characterRepo.getAllCharacters()

        val firstItem = gatAllCharactersFlow.first()
        assertTrue(firstItem is Loading)

        val secondItem = gatAllCharactersFlow.drop(1).first()
        assertTrue(secondItem is Success)
    }
}