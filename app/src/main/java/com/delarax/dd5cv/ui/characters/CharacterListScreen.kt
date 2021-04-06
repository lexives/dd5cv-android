package com.delarax.dd5cv.ui.characters

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import com.delarax.dd5cv.R
import com.delarax.dd5cv.data.CharacterRepoMockData.Companion.DEFAULT_CHARACTERS
import com.delarax.dd5cv.models.CharacterSummary
import com.delarax.dd5cv.models.toCharacterSummaryList
import com.delarax.dd5cv.ui.theme.Dd5cvTheme

@Composable
fun CharacterListScreen(
    onSelectCharacter: (String) -> Unit
) {
    val characterListVM: CharacterListVM = hiltNavGraphViewModel()
    CharacterListScreenContent(
        characters = characterListVM.characterSummaries,
        onCreateNewCharacter = {
            characterListVM.createNewCharacter(onSelectCharacter)
        },
        onSelectCharacter = onSelectCharacter
    )
}

@Composable
fun CharacterListScreenContent(
    characters: List<CharacterSummary>,
    onCreateNewCharacter: () -> Unit,
    onSelectCharacter: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                },
                backgroundColor = MaterialTheme.colors.primary
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onCreateNewCharacter) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null
                )
            }
        }
    ) {
        CharacterList(
            characters = characters,
            onSelectCharacter = onSelectCharacter
        )
    }
}

@Composable
fun CharacterList(
    characters: List<CharacterSummary>,
    onSelectCharacter: (String) -> Unit
) {
    LazyColumn {
        items(
            items = characters,
            key = { characterSummary -> characterSummary.id }
        ) { characterSummary ->
            CharacterListItem(
                characterSummary = characterSummary,
                onClick = { onSelectCharacter(characterSummary.id) }
            )
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
            modifier = Modifier.padding(16.dp)
        ) {
            // TODO: image
            CharacterSummary(characterSummary)
            // TODO: context buttons
        }
    }
}

@Composable
fun CharacterSummary(characterSummary: CharacterSummary) {
    Text(
        text = characterSummary.name ?: "Name is null",
        style = MaterialTheme.typography.h6
    )
}

/**
 * Previews
 */

//@Composable
//@Preview
//fun CharacterListItemPreview() {
//    Dd5cvTheme {
//        CharacterListItem(
//            characterSummary = DEFAULT_CHARACTERS.toCharacterSummaryList()[0],
//            onClick = {}
//        )
//    }
//}
//
//@Composable
//@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
//fun CharacterListItemDarkPreview() {
//    Dd5cvTheme {
//        CharacterListItem(
//            characterSummary = DEFAULT_CHARACTERS.toCharacterSummaryList()[0],
//            onClick = {}
//        )
//    }
//}

@Composable
@Preview
fun CharacterListScreenPreview() {
    Dd5cvTheme {
        CharacterListScreenContent(DEFAULT_CHARACTERS.toCharacterSummaryList(), {}, {})
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun CharacterListScreenDarkPreview() {
    Dd5cvTheme {
        CharacterListScreenContent(DEFAULT_CHARACTERS.toCharacterSummaryList(), {}, {})
    }
}
