package com.delarax.dd5cv.ui.characters

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.delarax.dd5cv.R
import com.delarax.dd5cv.data.characters.CharacterRepoMockData.Companion.DEFAULT_CHARACTERS
import com.delarax.dd5cv.models.FormattedResource
import com.delarax.dd5cv.models.characters.Character
import com.delarax.dd5cv.models.characters.toSummary
import com.delarax.dd5cv.ui.components.ActionItem
import com.delarax.dd5cv.ui.scaffold.ScaffoldVM
import com.delarax.dd5cv.ui.theme.Dd5cvTheme
import com.delarax.dd5cv.utils.State

@Composable
fun CharacterDetailsScreen(
    characterId: String?,
    onBackPress: () -> Unit,
    setScaffold: (ScaffoldVM.ViewState) -> Unit
) {
    val characterDetailsVM: CharacterDetailsVM = hiltViewModel()

    setScaffold(
        ScaffoldVM.ViewState(
            title = characterDetailsVM.characterState.getOrNull()?.let {
                it.name?.let { name ->
                    FormattedResource(
                        resId = R.string.single_arg,
                        values = listOf(name)
                    )
                } ?: FormattedResource(R.string.default_character_name)

            } ?: FormattedResource(R.string.destination_characters_title),
            actionMenu = listOf(),
            leftActionItem = ActionItem(
                name = stringResource(id = R.string.action_item_back),
                icon = Icons.Default.ArrowBack,
                onClick = onBackPress
            ),
            floatingActionButton = null
        )
    )

    characterDetailsVM.fetchCharacterById(characterId)
    CharacterDetailsScreenContent(characterDetailsVM.characterState)
}

@Composable
fun CharacterDetailsScreenContent(
    characterState: State<Character>
) {
    val character = characterState.getOrNull()
    Box(modifier = Modifier.padding(12.dp)) {
        character?.toSummary()?.let { CharacterSummary(characterSummary = it) }
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