package com.delarax.dd5cv.ui.characters

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.delarax.dd5cv.data.characters.CharacterRepoMockData.Companion.DEFAULT_CHARACTERS
import com.delarax.dd5cv.models.characters.Character
import com.delarax.dd5cv.models.characters.toSummary
import com.delarax.dd5cv.ui.theme.Dd5cvTheme
import com.delarax.dd5cv.utils.State

@Composable
fun CharacterDetailsScreen(
    characterId: String?,
    onBackPress: () -> Unit
) {
    val characterDetailsVM: CharacterDetailsVM = hiltViewModel()
    characterDetailsVM.fetchCharacterById(characterId)
    CharacterDetailsScreenContent(characterDetailsVM.characterState)
}

@Composable
fun CharacterDetailsScreenContent(
    characterState: State<Character>
) {
    val character = characterState.getOrNull()
    Box(modifier = Modifier.padding(12.dp)) {
        character?.toSummary()?.let { it1 -> CharacterSummary(characterSummary = it1) }
    }
}

@Composable
@Preview
fun CharacterDetailsScreenPreview() {
    Dd5cvTheme {
        CharacterDetailsScreenContent(State.Success(DEFAULT_CHARACTERS[0]))
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun CharacterDetailsScreenDarkPreview() {
    CharacterDetailsScreenPreview()
}