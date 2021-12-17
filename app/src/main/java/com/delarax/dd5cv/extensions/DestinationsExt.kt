package com.delarax.dd5cv.extensions

import com.delarax.dd5cv.ui.destinations.Destinations

fun Destinations.matchesLandingScreenRoute(route: String): Boolean =
    route.startsWith(landingScreenRoute)