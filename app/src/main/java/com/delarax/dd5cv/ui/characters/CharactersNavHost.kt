package com.delarax.dd5cv.ui.characters

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.delarax.dd5cv.models.FormattedResource
import com.delarax.dd5cv.ui.components.ActionItem
import com.delarax.dd5cv.ui.scaffold.ScaffoldVM

@Composable
fun CharactersNavHost(
    setScaffold: (
        FormattedResource,
        List<ActionItem>,
        ScaffoldVM.FloatingActionButton?
    ) -> Unit
) {
    val navController = rememberNavController()
    val actions = remember(navController) { MainActions(navController) }

    NavHost(
        navController = navController,
        startDestination = Routes.CHARACTER_LIST
    ) {
        composable(Routes.CHARACTER_LIST) {
            CharacterListScreen(
                onSelectCharacter = actions.selectCharacter,
                setScaffold = setScaffold
            )
        }
        composable("${Routes.CHARACTER_DETAILS}/{${RouteArgs.CHARACTER_ID}}") {
            val arguments = requireNotNull(it.arguments)
            val characterId = arguments.getString(RouteArgs.CHARACTER_ID)
            CharacterDetailsScreen(
                characterId = characterId,
                onBackPress = actions.back,
                setScaffold = setScaffold
            )
        }
    }
}

object Routes {
    const val CHARACTER_LIST = "characterList"
    const val CHARACTER_DETAILS = "characterDetails"
}

object RouteArgs {
    const val CHARACTER_ID = "characterId"
}

class MainActions(navController: NavController) {
    val selectCharacter: (String) -> Unit = { characterId ->
        navController.navigate("${Routes.CHARACTER_DETAILS}/$characterId")
    }

    val back: () -> Unit = {
        navController.popBackStack()
    }
}