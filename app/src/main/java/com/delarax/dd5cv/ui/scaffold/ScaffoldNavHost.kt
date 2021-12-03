package com.delarax.dd5cv.ui.scaffold

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.delarax.dd5cv.ui.characters.DestinationCharacters
import com.delarax.dd5cv.ui.common.Destination

@Composable
fun ScaffoldNavHost(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Destination.CHARACTERS.route
    ) {
        composable(Destination.CHARACTERS.route) {
            DestinationCharacters()
        }
    }
}