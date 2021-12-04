package com.delarax.dd5cv.ui.navigation.characters

import com.delarax.dd5cv.ui.navigation.RouteArg
import com.delarax.dd5cv.ui.navigation.Screen

enum class CharactersScreen(
    override val baseRoute: String,
    override val routeArgs: List<RouteArg>
) : Screen {
    CHARACTER_LIST(
        baseRoute = "characterList",
        routeArgs = listOf()
    ),

    CHARACTER_DETAILS (
        baseRoute = "characterDetails",
        routeArgs = listOf(
            RouteArg.CHARACTER_ID
        )
    )
}