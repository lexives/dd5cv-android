package com.delarax.dd5cv.ui.destinations

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.delarax.dd5cv.ui.destinations.characters.navigation.CharactersNavActions
import com.delarax.dd5cv.ui.destinations.characters.navigation.charactersNavGraph
import com.delarax.dd5cv.ui.scaffold.CustomScaffoldState

@Composable
fun MainNavHost(
    navController: NavHostController,
    setScaffold: (CustomScaffoldState) -> Unit
) {
    val charactersNavActions = remember(navController) { CharactersNavActions(navController) }

    NavHost(
        navController = navController,
        startDestination = Destination.CHARACTERS.route
    ) {
        charactersNavGraph(navActions = charactersNavActions, setScaffold = setScaffold)
    }
}