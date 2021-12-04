package com.delarax.dd5cv.ui.navigation.scaffold

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.delarax.dd5cv.ui.navigation.Destination
import com.delarax.dd5cv.ui.navigation.RouteArg
import com.delarax.dd5cv.ui.navigation.Screen
import com.delarax.dd5cv.ui.navigation.getRoute
import com.delarax.dd5cv.ui.navigation.getRouteWithArgs

open class MainNavActions(
    private val navController: NavHostController
) {
    fun back() { navController.popBackStack() }

    fun popUpTo(destination: Destination) {
        navController.navigate(destination.route) {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            // Avoid multiple copies of the same destination when
            // re-selecting the same item
            launchSingleTop = true
            // Restore state when re-selecting a previously selected item
            restoreState = true
        }
    }

    protected fun navToScreen(screen: Screen,): Unit = navController.navigate(screen.getRoute())

    protected fun navToScreenWithArgs(screen: Screen, args: Map<RouteArg, String>): Unit =
        navController.navigate(screen.getRouteWithArgs(args))
}