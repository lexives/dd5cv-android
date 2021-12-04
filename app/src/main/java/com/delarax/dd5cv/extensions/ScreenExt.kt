package com.delarax.dd5cv.extensions

import com.delarax.dd5cv.models.navigation.Screen
import com.delarax.dd5cv.ui.destinations.RouteArg

fun Screen.getRoute() : String = routeArgs
    .map{ it.name }
    .fold(baseRoute) { route, argName -> "$route/{${argName}}" }

fun Screen.getRouteWithArgs(
    argValues: Map<RouteArg, String>
): String = routeArgs
    .map{ argValues[it] ?: it.name }
    .fold(baseRoute) { route, argValue -> "$route/${argValue}" }