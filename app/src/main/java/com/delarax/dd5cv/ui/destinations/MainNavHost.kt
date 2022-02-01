package com.delarax.dd5cv.ui.destinations

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.delarax.dd5cv.ui.destinations.characters.navigation.CharactersNavActions
import com.delarax.dd5cv.ui.destinations.characters.navigation.charactersNavGraph
import com.delarax.dd5cv.ui.destinations.settings.SettingsScreen
import com.google.accompanist.pager.ExperimentalPagerApi

@ExperimentalPagerApi
@ExperimentalMaterialApi
@Composable
fun MainNavHost(
    navController: NavHostController
) {
    val charactersNavActions = remember(navController) { CharactersNavActions(navController) }

    NavHost(
        navController = navController,
        startDestination = Destinations.CHARACTERS.route
    ) {
        charactersNavGraph(navActions = charactersNavActions)
        composable(Destinations.SETTINGS.route) {
            SettingsScreen()
        }
    }
}