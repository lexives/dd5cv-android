package com.delarax.dd5cv.ui

import android.widget.Toast
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.delarax.dd5cv.R
import com.delarax.dd5cv.extensions.matchesLandingScreenRoute
import com.delarax.dd5cv.models.FormattedResource
import com.delarax.dd5cv.models.ui.AppState
import com.delarax.dd5cv.ui.components.BackPressHandler
import com.delarax.dd5cv.ui.components.Dd5cvSideDrawerContent
import com.delarax.dd5cv.ui.components.DrawerMenuItem
import com.delarax.dd5cv.ui.components.dialog.Dialog
import com.delarax.dd5cv.ui.components.dialog.LoadingDialog
import com.delarax.dd5cv.ui.components.resolve
import com.delarax.dd5cv.ui.components.toppappbar.ActionItem
import com.delarax.dd5cv.ui.components.toppappbar.Dd5cvTopAppBar
import com.delarax.dd5cv.ui.destinations.Destinations
import com.delarax.dd5cv.ui.destinations.MainNavActions
import com.delarax.dd5cv.ui.destinations.MainNavHost
import kotlinx.coroutines.launch

@Composable
fun Dd5cvScaffold(
    appState: AppState
) {
    val navController = rememberNavController()
    val mainNavActions = remember(navController) { MainNavActions(navController) }

    val scaffoldState: ScaffoldState = rememberScaffoldState()

    // Used to launch UI events like closing the side drawer
    val scope = rememberCoroutineScope()

    // Used here for clearing the focus of something if you click outside of it
    val localFocusManager = LocalFocusManager.current

    val unknownScreenToast = Toast.makeText(
        LocalContext.current,
        R.string.destination_does_not_exist,
        Toast.LENGTH_SHORT
    )

    // Determine if the current route is a "landing screen" or not. A landing screen is the first
    // screen you see when navigating to a destination
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val onLandingScreen: Boolean = currentRoute?.let {
        Destinations.values()
            .map { it.matchesLandingScreenRoute(currentRoute) }
            .fold(false) { acc, current ->
                acc || current
            }
    } ?: false

    // If we're on a landing screen then the left action icon should be the open/close side drawer
    // button. Otherwise it should be the back button.
    val leftActionItem = if (onLandingScreen) {
        ActionItem(
            name = if (scaffoldState.drawerState.isOpen) {
                FormattedResource(R.string.action_item_close_left_drawer)
            } else {
                FormattedResource(R.string.action_item_open_left_drawer)
            },
            icon = Icons.Default.Menu,
            onClick = {
                scope.launch {
                    if (scaffoldState.drawerState.isOpen) {
                        scaffoldState.drawerState.close()
                    } else {
                        scaffoldState.drawerState.open()
                    }
                }
            }
        )
    } else {
        ActionItem(
            name = FormattedResource(R.string.action_item_back),
            icon = Icons.Default.ArrowBack,
            onClick = {
                // If there's a back press handler then invoke it when the top app bar back button
                // is pressed, otherwise navigate back
                appState.scaffoldState.onBackPressed?.invoke() ?: navController.popBackStack()
            }
        )
    }

    // Set up a back press handler with the given callback if it exists, otherwise if the
    // side drawer is open then set up a back press handler to close it
    appState.scaffoldState.onBackPressed?.let {
        BackPressHandler(it)
    } ?: if (scaffoldState.drawerState.isOpen) {
        BackPressHandler {
            scope.launch {
                scaffoldState.drawerState.close()
            }
        }
    }

    // Show the loading indicator if it should be shown, otherwise show a dialog if one is provided
    appState.loadingIndicatorState?.let {
        LoadingDialog(loadingIndicatorState = it)
    } ?: appState.dialogState?.let {
        Dialog(dialogState = it)
    }

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.pointerInput(Unit) {
            detectTapGestures(onTap = {
                localFocusManager.clearFocus()
            })
        },
        topBar = {
            Dd5cvTopAppBar(
                title = appState.scaffoldState.title,
                actionItems = appState.scaffoldState.actionMenu,
                leftActionItem = leftActionItem
            )
        },
        floatingActionButton = {
            appState.scaffoldState.floatingActionButtonState?.let {
                FloatingActionButton(onClick = it.onClick) {
                    Icon(
                        imageVector = it.icon,
                        contentDescription = it.contentDescription.resolve()
                    )
                }
            }
        },
        drawerContent = {
            Dd5cvSideDrawerContent(Destinations.values().map { destination ->
                DrawerMenuItem(
                    nameRes = destination.titleRes,
                    icon = destination.icon,
                    onClick = {
                        try {
                            mainNavActions.popUpTo(destination)
                            scope.launch {
                                scaffoldState.drawerState.close()
                            }
                        } catch (e: IllegalArgumentException) {
                            // This exception occurs if the destination does not exist
                            unknownScreenToast.show()
                        }
                    }
                )
            })
        },
        drawerBackgroundColor = MaterialTheme.colors.surface
    ) {
        MainNavHost(navController = navController)
    }
}