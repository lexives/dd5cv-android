package com.delarax.dd5cv.ui.characters

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.delarax.dd5cv.R
import com.delarax.dd5cv.ui.theme.Dd5cvTheme

@Composable
fun CharacterListScreen(characterListVM: CharacterListVM) {
    CharacterListScreenContent(characterList = characterListVM.characterList)
}

@Composable
fun CharacterListScreenContent(characterList: List<String>) {
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
            FloatingActionButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null
                )
            }
        }
    ) {
        Column {
            for (character in characterList) {
                Text(text = character)
            }
        }
    }
}

@Composable
fun CharacterListItem() {

}

@Composable
@Preview
fun CharacterListPagePreview() {
    Dd5cvTheme {
        CharacterListScreenContent(listOf("Holdrum", "Delarax", "Elissa"))
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun CharacterListPageDarkPreview() {
    Dd5cvTheme {
        CharacterListScreenContent(listOf("Holdrum", "Delarax", "Elissa"))
    }
}