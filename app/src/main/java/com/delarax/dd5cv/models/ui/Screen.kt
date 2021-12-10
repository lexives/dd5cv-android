package com.delarax.dd5cv.models.ui

import com.delarax.dd5cv.ui.destinations.RouteArg

interface Screen {
    val baseRoute: String
    val routeArgs: List<RouteArg>
}