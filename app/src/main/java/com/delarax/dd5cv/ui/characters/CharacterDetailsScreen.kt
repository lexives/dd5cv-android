package com.delarax.dd5cv.ui.characters

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import com.delarax.dd5cv.data.CharacterRepoMockData.Companion.DEFAULT_CHARACTERS
import com.delarax.dd5cv.models.Character
import com.delarax.dd5cv.ui.theme.Dd5cvTheme

@Composable
fun CharacterDetailsScreen(
    characterId: String?,
) {
    val characterDetailsVM: CharacterDetailsVM = hiltNavGraphViewModel()
    characterDetailsVM.fetchCharacterById(characterId)
    CharacterDetailsScreenContent(characterDetailsVM.character)
}

@Composable
fun CharacterDetailsScreenContent(character: Character) {
    Text(character.name ?: "")
}

@Composable
@Preview
fun CharacterDetailsScreenPreview() {
    Dd5cvTheme {
        Surface(Modifier.fillMaxSize()) {
            CharacterDetailsScreenContent(DEFAULT_CHARACTERS[0])
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun CharacterDetailsScreenDarkPreview() {
    Dd5cvTheme {
        Surface(Modifier.fillMaxSize()) {
            CharacterDetailsScreenContent(DEFAULT_CHARACTERS[0])
        }
    }
}