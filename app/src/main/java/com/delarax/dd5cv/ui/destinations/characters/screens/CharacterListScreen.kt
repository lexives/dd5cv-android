package com.delarax.dd5cv.ui.destinations.characters.screens

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import com.delarax.dd5cv.models.characters.CharacterSummary
import com.delarax.dd5cv.models.data.State
import com.delarax.dd5cv.models.data.State.Success
import com.delarax.dd5cv.ui.components.PreviewSurface
import com.delarax.dd5cv.ui.components.state.ViewStateExchanger
import com.delarax.dd5cv.ui.destinations.characters.screens.shared.CharacterSummary
import com.delarax.dd5cv.ui.destinations.characters.viewmodels.CharacterListVM
import com.delarax.dd5cv.ui.theme.Dimens
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

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
        onSelectCharacter = navToCharacterDetails,
        isRefreshing = characterListVM.viewState.isRefreshing,
        onRefresh = characterListVM::refreshCharacters
    )
}

@Composable
fun CharacterListScreenContent(
    characterListState: State<List<CharacterSummary>>,
    onSelectCharacter: (String) -> Unit,
    isRefreshing: Boolean,
    onRefresh: () -> Unit
) {
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
        onRefresh = onRefresh
    ) {
        CharacterList(
            characterListState = characterListState,
            onSelectCharacter = onSelectCharacter
        )
    }
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
        LazyColumn(modifier = Modifier.fillMaxSize()) {
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
            CharacterSummary(characterSummary)
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
        CharacterListScreenContent(
            characterListState = Success(DEFAULT_CHARACTERS.toCharacterSummaryList()),
            onSelectCharacter = {},
            isRefreshing = false,
            onRefresh = {}
        )
    }
}