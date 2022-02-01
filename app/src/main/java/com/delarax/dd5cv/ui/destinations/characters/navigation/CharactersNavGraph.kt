package com.delarax.dd5cv.ui.destinations.characters.navigation

import androidx.compose.material.ExperimentalMaterialApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.delarax.dd5cv.extensions.getRoute
import com.delarax.dd5cv.ui.destinations.Destinations
import com.delarax.dd5cv.ui.destinations.RouteArg
import com.delarax.dd5cv.ui.destinations.characters.screens.CharacterDetailsScreen
import com.delarax.dd5cv.ui.destinations.characters.screens.CharacterListScreen
import com.google.accompanist.pager.ExperimentalPagerApi

@ExperimentalMaterialApi
@ExperimentalPagerApi
fun NavGraphBuilder.charactersNavGraph(
    navActions: CharactersNavActions
) {
    navigation(
        startDestination = CharactersDestScreen.CHARACTER_LIST.getRoute(),
        route = Destinations.CHARACTERS.route
    ) {
        composable(CharactersDestScreen.CHARACTER_LIST.getRoute()) {
            CharacterListScreen(
                navToCharacterDetails = navActions::goToCharacterDetails,
            )
        }
        composable(CharactersDestScreen.CHARACTER_DETAILS.getRoute()) {
            val arguments = requireNotNull(it.arguments)
            val characterId = arguments.getString(RouteArg.CHARACTER_ID.name)
            CharacterDetailsScreen(
                characterId = characterId,
                navBack = navActions::back
            )
        }
    }
}