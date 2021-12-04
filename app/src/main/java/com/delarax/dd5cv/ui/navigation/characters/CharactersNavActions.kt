package com.delarax.dd5cv.ui.navigation.characters

import androidx.navigation.NavHostController
import com.delarax.dd5cv.ui.navigation.RouteArg
import com.delarax.dd5cv.ui.navigation.scaffold.MainNavActions

class CharactersNavActions(
    navController: NavHostController
) : MainNavActions(navController) {

    fun goToCharacterDetails(characterId: String) = navToScreenWithArgs(
        CharactersScreen.CHARACTER_DETAILS,
        mapOf(RouteArg.CHARACTER_ID to characterId)
    )
}