package com.delarax.dd5cv.ui.destinations

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.delarax.dd5cv.ui.destinations.characters.navigation.CharactersNavActions
import com.delarax.dd5cv.ui.destinations.characters.navigation.charactersNavGraph
import com.delarax.dd5cv.ui.destinations.settings.SettingsScreen
import com.delarax.dd5cv.ui.scaffold.CustomScaffoldState

@Composable
fun MainNavHost(
    navController: NavHostController,
    mainNavActions: MainNavActions,
    setScaffold: (CustomScaffoldState) -> Unit
) {
    val charactersNavActions = remember(navController) { CharactersNavActions(navController) }

    NavHost(
        navController = navController,
        startDestination = Destination.CHARACTERS.route
    ) {
        charactersNavGraph(navActions = charactersNavActions, setScaffold = setScaffold)
        composable(Destination.SETTINGS.route) {
            SettingsScreen(setScaffold = setScaffold)
        }
    }
}