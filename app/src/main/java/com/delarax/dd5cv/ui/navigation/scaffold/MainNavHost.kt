package com.delarax.dd5cv.ui.navigation.scaffold

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.delarax.dd5cv.ui.navigation.Destination
import com.delarax.dd5cv.ui.navigation.characters.CharactersNavActions
import com.delarax.dd5cv.ui.navigation.characters.charactersNavGraph

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