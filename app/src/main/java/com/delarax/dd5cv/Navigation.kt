package com.delarax.dd5cv

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.delarax.dd5cv.ui.characters.CharacterListScreen
import com.delarax.dd5cv.ui.characters.CharacterListVM

@Composable
fun Dd5cvNavigation(characterListVM: CharacterListVM) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Routes.CHARACTER_LIST
    ) {
        composable(Routes.CHARACTER_LIST) {
            CharacterListScreen(characterListVM = characterListVM)
        }
    }
}

object Routes {
    const val CHARACTER_LIST = "characterList"
}