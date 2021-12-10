package com.delarax.dd5cv.ui.destinations.characters.navigation

import com.delarax.dd5cv.models.ui.Screen
import com.delarax.dd5cv.ui.destinations.RouteArg

enum class CharactersDestScreen(
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