package com.delarax.dd5cv.ui.characters

import androidx.compose.runtime.Composable
import com.delarax.dd5cv.models.FormattedResource
import com.delarax.dd5cv.ui.components.ActionItem
import com.delarax.dd5cv.ui.scaffold.ScaffoldVM

@Composable
fun DestinationCharacters(
    setScaffold: (
        FormattedResource,
        List<ActionItem>,
        ScaffoldVM.FloatingActionButton?
    ) -> Unit
) {
    // TODO: list view vs list details view
    CharactersNavHost(setScaffold)
}