package com.delarax.dd5cv.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.delarax.dd5cv.ui.navigation.characters.CharactersNavActions
import com.delarax.dd5cv.ui.navigation.characters.charactersNavGraph
import com.delarax.dd5cv.ui.scaffold.ScaffoldVM

@Composable
fun MainNavHost(
    navController: NavHostController,
    setScaffold: (ScaffoldVM.ViewState) -> Unit
) {
    val charactersNavActions = remember(navController) { CharactersNavActions(navController) }

    NavHost(
        navController = navController,
        startDestination = Destination.CHARACTERS.route
    ) {
        charactersNavGraph(actions = charactersNavActions, setScaffold = setScaffold)
    }
}