package com.delarax.dd5cv.ui.destinations.characters.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.delarax.dd5cv.ui.destinations.Destination
import com.delarax.dd5cv.ui.destinations.RouteArg
import com.delarax.dd5cv.ui.destinations.characters.screens.CharacterDetailsScreen
import com.delarax.dd5cv.ui.destinations.characters.screens.CharacterListScreen
import com.delarax.dd5cv.ui.destinations.getRoute
import com.delarax.dd5cv.ui.scaffold.CustomScaffoldState

fun NavGraphBuilder.charactersNavGraph(
    navActions: CharactersNavActions,
    setScaffold: (CustomScaffoldState) -> Unit
) {
    navigation(
        startDestination = CharactersScreen.CHARACTER_LIST.getRoute(),
        route = Destination.CHARACTERS.route
    ) {
        composable(CharactersScreen.CHARACTER_LIST.getRoute()) {
            CharacterListScreen(
                onSelectCharacter = navActions::goToCharacterDetails,
                setScaffold = setScaffold
            )
        }
        composable(CharactersScreen.CHARACTER_DETAILS.getRoute()) {
            val arguments = requireNotNull(it.arguments)
            val characterId = arguments.getString(RouteArg.CHARACTER_ID.name)
            CharacterDetailsScreen(
                characterId = characterId,
                onBackPress = navActions::back,
                setScaffold = setScaffold
            )
        }
    }
}