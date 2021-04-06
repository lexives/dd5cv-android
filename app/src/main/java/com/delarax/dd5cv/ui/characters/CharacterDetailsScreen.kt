package com.delarax.dd5cv.ui.characters

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import com.delarax.dd5cv.data.CharacterRepoMockData.Companion.DEFAULT_CHARACTERS
import com.delarax.dd5cv.models.Character
import com.delarax.dd5cv.models.toSummary
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
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = character.name ?: "Unspecified Character")
                },
                backgroundColor = MaterialTheme.colors.primary
            )
        },
    ) {
        Box(modifier = Modifier.padding(12.dp)) {
            CharacterSummary(characterSummary = character.toSummary())
        }
    }
}

@Composable
@Preview
fun CharacterDetailsScreenPreview() {
    Dd5cvTheme {
        CharacterDetailsScreenContent(DEFAULT_CHARACTERS[0])
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun CharacterDetailsScreenDarkPreview() {
    Dd5cvTheme {
        CharacterDetailsScreenContent(DEFAULT_CHARACTERS[0])
    }
}