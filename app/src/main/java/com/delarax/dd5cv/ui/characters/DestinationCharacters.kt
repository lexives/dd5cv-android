package com.delarax.dd5cv.ui.characters

import androidx.compose.runtime.Composable
import com.delarax.dd5cv.ui.scaffold.ScaffoldVM

@Composable
fun DestinationCharacters(
    setScaffold: (ScaffoldVM.ViewState) -> Unit
) {
    // TODO: list view vs list details view
    CharactersNavHost(setScaffold)
}