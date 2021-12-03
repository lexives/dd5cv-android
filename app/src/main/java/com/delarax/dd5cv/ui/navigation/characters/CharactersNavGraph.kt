package com.delarax.dd5cv.ui.navigation.characters

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.delarax.dd5cv.ui.destinations.characters.screens.CharacterDetailsScreen
import com.delarax.dd5cv.ui.destinations.characters.screens.CharacterListScreen
import com.delarax.dd5cv.ui.navigation.Destination
import com.delarax.dd5cv.ui.scaffold.ScaffoldVM

fun NavGraphBuilder.charactersNavGraph(
    actions: CharactersNavActions,
    setScaffold: (ScaffoldVM.ViewState) -> Unit
) {
    navigation(
        startDestination = CharactersRoutes.CHARACTER_LIST,
        route = Destination.CHARACTERS.route
    ) {
        composable(CharactersRoutes.CHARACTER_LIST) {
            CharacterListScreen(
                onSelectCharacter = actions::toCharacterDetails,
                setScaffold = setScaffold
            )
        }
        composable("${CharactersRoutes.CHARACTER_DETAILS}/{${CharactersRouteArgs.CHARACTER_ID}}") {
            val arguments = requireNotNull(it.arguments)
            val characterId = arguments.getString(CharactersRouteArgs.CHARACTER_ID)
            CharacterDetailsScreen(
                characterId = characterId,
                onBackPress = actions::back,
                setScaffold = setScaffold
            )
        }
    }
}

object CharactersRoutes {
    const val CHARACTER_LIST = "characterList"
    const val CHARACTER_DETAILS = "characterDetails"
}

object CharactersRouteArgs {
    const val CHARACTER_ID = "characterId"
}

class CharactersNavActions(private val navController: NavController) {
    fun toCharacterDetails(characterId: String) {
        navController.navigate("${CharactersRoutes.CHARACTER_DETAILS}/$characterId")
    }

    fun back() { navController.popBackStack() }
}