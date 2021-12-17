package com.delarax.dd5cv.ui

import android.widget.Toast
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.delarax.dd5cv.R
import com.delarax.dd5cv.models.FormattedResource
import com.delarax.dd5cv.models.ui.AppState
import com.delarax.dd5cv.ui.components.ActionItem
import com.delarax.dd5cv.ui.components.BackPressHandler
import com.delarax.dd5cv.ui.components.Dd5cvSideDrawerContent
import com.delarax.dd5cv.ui.components.Dd5cvTopAppBar
import com.delarax.dd5cv.ui.components.DrawerMenuItem
import com.delarax.dd5cv.ui.components.resolve
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

    val unknownScreenToast = Toast.makeText(
        LocalContext.current,
        R.string.destination_does_not_exist,
        Toast.LENGTH_SHORT
    )

    val defaultLeftActionItem = ActionItem(
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

    // If the side drawer is open then set up a back press handler to close it
    if (scaffoldState.drawerState.isOpen) {
        BackPressHandler {
            scope.launch {
                scaffoldState.drawerState.close()
            }
        }
    }

    Dialog(dialogState = appState.dialogState)

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            Dd5cvTopAppBar(
                title = appState.scaffoldState.title,
                actionItems = appState.scaffoldState.actionMenu,
                leftActionItem = appState.scaffoldState.leftActionItem ?: defaultLeftActionItem
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