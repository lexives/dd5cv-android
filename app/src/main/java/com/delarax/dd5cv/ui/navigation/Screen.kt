package com.delarax.dd5cv.ui.navigation

interface Screen {
    val baseRoute: String
    val routeArgs: List<RouteArg>
}

fun Screen.getRoute() : String = routeArgs
    .map{ it.name }
    .fold(baseRoute) { route, argName -> "$route/{${argName}}" }

fun Screen.getRouteWithArgs(
    argValues: Map<RouteArg, String>
): String = routeArgs
    .map{ argValues[it] ?: it.name }
    .fold(baseRoute) { route, argValue -> "$route/${argValue}" }