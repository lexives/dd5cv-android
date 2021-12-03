package com.delarax.dd5cv.ui.characters

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.delarax.dd5cv.data.characters.CharacterRepoMockData.Companion.DEFAULT_CHARACTERS
import com.delarax.dd5cv.models.characters.Character
import com.delarax.dd5cv.models.characters.toSummary
import com.delarax.dd5cv.ui.components.ActionItem
import com.delarax.dd5cv.ui.components.Dd5cvTopAppBar
import com.delarax.dd5cv.ui.theme.Dd5cvTheme
import com.delarax.dd5cv.utils.State

@Composable
fun CharacterDetailsScreen(
    characterId: String?,
    onBackPress: () -> Unit
) {
    val characterDetailsVM: CharacterDetailsVM = hiltViewModel()
    characterDetailsVM.fetchCharacterById(characterId)
    CharacterDetailsScreenContent(characterDetailsVM.characterState, onBackPress)
}

@Composable
fun CharacterDetailsScreenContent(
    characterState: State<Character>,
    onBackPress: () -> Unit
) {
    val character = characterState.getOrNull()
    Scaffold(
        topBar = {
            Dd5cvTopAppBar(
                title = character?.name ?: "Unspecified Character",
                leftActionItem = ActionItem(
                    name = "Back",
                    icon = Icons.Filled.ArrowBack,
                    onClick = onBackPress
                )
            )
        },
    ) {
        Box(modifier = Modifier.padding(12.dp)) {
            character?.toSummary()?.let { it1 -> CharacterSummary(characterSummary = it1) }
        }
    }
}

@Composable
@Preview
fun CharacterDetailsScreenPreview() {
    Dd5cvTheme {
        CharacterDetailsScreenContent(State.Success(DEFAULT_CHARACTERS[0]), {})
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun CharacterDetailsScreenDarkPreview() {
    CharacterDetailsScreenPreview()
}