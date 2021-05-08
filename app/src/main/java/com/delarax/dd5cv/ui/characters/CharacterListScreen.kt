package com.delarax.dd5cv.ui.characters

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import com.delarax.dd5cv.R
import com.delarax.dd5cv.data.characters.CharacterRepoMockData.Companion.DEFAULT_CHARACTERS
import com.delarax.dd5cv.models.characters.CharacterClassLevel
import com.delarax.dd5cv.models.characters.CharacterSummary
import com.delarax.dd5cv.models.characters.toCharacterSummaryList
import com.delarax.dd5cv.ui.components.ActionItem
import com.delarax.dd5cv.ui.components.Dd5cvTopAppBar
import com.delarax.dd5cv.ui.components.ViewStateExchanger
import com.delarax.dd5cv.ui.theme.Dd5cvTheme
import com.delarax.dd5cv.utils.State
import com.delarax.dd5cv.utils.State.*

@Composable
fun CharacterListScreen(
    onSelectCharacter: (String) -> Unit
) {
    val characterListVM: CharacterListVM = hiltNavGraphViewModel()
    CharacterListScreenContent(
        characterListState = characterListVM.characterListState,
        onCreateNewCharacter = {
            characterListVM.createNewCharacter(onSelectCharacter)
        },
        onSelectCharacter = onSelectCharacter
    )
}

@Composable
fun CharacterListScreenContent(
    characterListState: State<List<CharacterSummary>>,
    onCreateNewCharacter: () -> Unit,
    onSelectCharacter: (String) -> Unit
) {
    Scaffold(
        topBar = {
            Dd5cvTopAppBar(
                title = stringResource(R.string.app_name),
                leftActionItem = ActionItem(
                    name = "Side Menu",
                    icon = Icons.Filled.Menu
                )
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
            modifier = Modifier.padding(12.dp)
        ) {
            // TODO: image
            CharacterSummary(characterSummary)
            // TODO: context buttons
        }
    }
}

@Composable
fun CharacterSummary(characterSummary: CharacterSummary) {
    Column {
        Text(
            text = characterSummary.name ?: "Name is null",
            style = MaterialTheme.typography.h6
        )
        Spacer(Modifier.height(4.dp))
        CharacterClasses(classes = characterSummary.classes)
    }
}

@Composable
fun CharacterClasses(classes: List<CharacterClassLevel>) {
    Row {
        val sortedClasses = classes.sortedByDescending {
            it.level
        }
        if (classes.isEmpty()) {
            Text(
                text = "This character has not selected a class",
                style = MaterialTheme.typography.body2,
                fontStyle = FontStyle.Italic
            )
        } else {
            for (characterClass in sortedClasses) {
                Row {
                    Card(
                        shape = RoundedCornerShape(8.dp),
                        elevation = 0.dp,
                        backgroundColor = MaterialTheme.colors.onSurface.copy(
                            alpha = 0.2f
                        ),
                    ) {
                        val className: String = characterClass.name
                            ?: "Unspecified Class"
                        val level: String = characterClass.level?.toString()
                            ?: "-"

                        Text(
                            text = "$className lv. $level",
                            style = MaterialTheme.typography.body2,
                            modifier = Modifier.padding(
                                vertical = 1.dp,
                                horizontal = 6.dp
                            )
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                }
            }
        }
    }
}

/**
 * Previews
 */

@Composable
@Preview
fun CharacterListScreenPreview() {
    Dd5cvTheme {
        CharacterListScreenContent(Success(DEFAULT_CHARACTERS.toCharacterSummaryList()), {}, {})
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun CharacterListScreenDarkPreview() {
    CharacterListScreenPreview()
}
