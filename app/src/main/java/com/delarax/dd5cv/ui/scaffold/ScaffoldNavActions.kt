package com.delarax.dd5cv.ui.scaffold

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.delarax.dd5cv.ui.common.Destination

class ScaffoldNavActions(
    navController: NavHostController
) {
    val popUpTo: (Destination) -> Unit = { destination ->
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

    val back: () -> Unit = { navController.popBackStack() }
}