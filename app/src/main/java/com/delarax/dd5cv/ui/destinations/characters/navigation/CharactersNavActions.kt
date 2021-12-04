package com.delarax.dd5cv.ui.destinations.characters.navigation

import androidx.navigation.NavHostController
import com.delarax.dd5cv.ui.destinations.MainNavActions
import com.delarax.dd5cv.ui.destinations.RouteArg

class CharactersNavActions(
    navController: NavHostController
) : MainNavActions(navController) {

    fun goToCharacterDetails(characterId: String) = navToScreenWithArgs(
        CharactersScreen.CHARACTER_DETAILS,
        mapOf(RouteArg.CHARACTER_ID to characterId)
    )
}