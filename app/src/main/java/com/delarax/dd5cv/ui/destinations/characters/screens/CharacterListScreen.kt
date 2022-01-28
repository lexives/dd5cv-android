package com.delarax.dd5cv.ui.destinations.characters.screens

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.delarax.dd5cv.data.characters.remote.RemoteCharacterDataSourceMocked.Companion.DEFAULT_CHARACTERS
import com.delarax.dd5cv.extensions.toCharacterSummaryList
import com.delarax.dd5cv.models.State
import com.delarax.dd5cv.models.State.Success
import com.delarax.dd5cv.models.characters.CharacterSummary
import com.delarax.dd5cv.ui.components.PreviewSurface
import com.delarax.dd5cv.ui.components.state.ViewStateExchanger
import com.delarax.dd5cv.ui.destinations.characters.screens.shared.CharacterSummaryComponent
import com.delarax.dd5cv.ui.destinations.characters.viewmodels.CharacterListVM
import com.delarax.dd5cv.ui.theme.Dimens

@Composable
fun CharacterListScreen(
    navToCharacterDetails: (String) -> Unit
) {
    val characterListVM: CharacterListVM = hiltViewModel()

    val hasRunAsyncInit = remember { mutableStateOf(false) }
    if (!hasRunAsyncInit.value) {
        characterListVM.updateScaffoldState(navToCharacterDetails)
        characterListVM.asyncInit(navToCharacterDetails)
        hasRunAsyncInit.value = true
    }

    CharacterListScreenContent(
        characterListState = characterListVM.characterListState,
        onSelectCharacter = navToCharacterDetails
    )
}

@Composable
fun CharacterListScreenContent(
    characterListState: State<List<CharacterSummary>>,
    onSelectCharacter: (String) -> Unit
) {
    CharacterList(
        characterListState = characterListState,
        onSelectCharacter = onSelectCharacter
    )
}

@Composable
fun CharacterList(
    characterListState: State<List<CharacterSummary>>,
    onSelectCharacter: (String) -> Unit
) {
    ViewStateExchanger(
        state = characterListState
    ) {
        val characters = characterListState.getOrDefault(listOf())
        LazyColumn {
            items(
                items = characters,
                key = { characterSummary -> characterSummary.id }
            ) { characterSummary ->
                CharacterListItem(
                    characterSummary = characterSummary,
                    onClick = { onSelectCharacter(characterSummary.id) }
                )
                Divider(color = MaterialTheme.colors.onSurface.copy(alpha = 0.4f))
            }
        }
    }
}

@Composable
fun CharacterListItem(
    characterSummary: CharacterSummary,
    onClick: () -> Unit
) {
    Surface(modifier = Modifier
        .fillMaxWidth()
        .clickable(onClick = onClick)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(Dimens.Spacing.md)
        ) {
            //  TODO: image
            CharacterSummaryComponent(characterSummary)
            // TODO: context buttons
        }
    }
}

/****************************************** Previews **********************************************/

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CharacterListScreenPreview() {
    PreviewSurface {
        CharacterListScreenContent(Success(DEFAULT_CHARACTERS.toCharacterSummaryList()), {})
    }
}