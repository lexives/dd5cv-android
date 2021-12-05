package com.delarax.dd5cv.ui.destinations.characters.screens

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.delarax.dd5cv.R
import com.delarax.dd5cv.data.characters.repo.CharacterRepoMockData.Companion.DEFAULT_CHARACTERS
import com.delarax.dd5cv.extensions.toCharacterSummaryList
import com.delarax.dd5cv.models.FormattedResource
import com.delarax.dd5cv.models.State
import com.delarax.dd5cv.models.State.Success
import com.delarax.dd5cv.models.characters.CharacterClassLevel
import com.delarax.dd5cv.models.characters.CharacterSummary
import com.delarax.dd5cv.models.navigation.CustomScaffoldState
import com.delarax.dd5cv.models.navigation.FloatingActionButtonState
import com.delarax.dd5cv.ui.components.ViewStateExchanger
import com.delarax.dd5cv.ui.destinations.characters.screens.shared.CharacterSummaryComponent
import com.delarax.dd5cv.ui.destinations.characters.viewmodels.CharacterListVM
import com.delarax.dd5cv.ui.theme.Dd5cvTheme
import com.delarax.dd5cv.ui.theme.Dimens

@Composable
fun CharacterListScreen(
    onSelectCharacter: (String) -> Unit,
    setScaffold: (CustomScaffoldState) -> Unit
) {
    val characterListVM: CharacterListVM = hiltViewModel()

    setScaffold(
        CustomScaffoldState(
            title = FormattedResource(R.string.destination_characters_title),
            floatingActionButtonState = FloatingActionButtonState(
                icon = Icons.Default.Edit,
                contentDescription = FormattedResource(R.string.add_character_content_desc),
                onClick = {
                    characterListVM.createNewCharacter(goToCharacterDetails = onSelectCharacter)
                }
            )
        )
    )

    CharacterListScreenContent(
        characterListState = characterListVM.characterListState,
        onSelectCharacter = onSelectCharacter
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
                        shape = RoundedCornerShape(size = 8.dp),
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
                                vertical = Dimens.Spacing.xxs,
                                horizontal = Dimens.Spacing.sm
                            )
                        )
                    }
                    Spacer(modifier = Modifier.width(Dimens.Spacing.md))
                }
            }
        }
    }
}

/****************************************** Previews **********************************************/

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CharacterListScreenPreview() {
    Dd5cvTheme {
        CharacterListScreenContent(Success(DEFAULT_CHARACTERS.toCharacterSummaryList()), {})
    }
}