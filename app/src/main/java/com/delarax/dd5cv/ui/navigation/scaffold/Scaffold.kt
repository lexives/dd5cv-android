package com.delarax.dd5cv.ui.navigation.scaffold

import android.widget.Toast
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.delarax.dd5cv.R
import com.delarax.dd5cv.ui.components.ActionItem
import com.delarax.dd5cv.ui.components.BackPressHandler
import com.delarax.dd5cv.ui.components.Dd5cvSideDrawerContent
import com.delarax.dd5cv.ui.components.Dd5cvTopAppBar
import com.delarax.dd5cv.ui.components.DrawerMenuItem
import com.delarax.dd5cv.ui.navigation.Destination
import com.delarax.dd5cv.ui.resolve
import kotlinx.coroutines.launch

@Composable
fun Dd5cvScaffold() {
    val scaffoldVM: ScaffoldVM = hiltViewModel()

    val navController = rememberNavController()
    val navActions = remember(navController) { MainNavActions(navController) }

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    val unknownScreenToast = Toast.makeText(
        LocalContext.current,
        R.string.destination_does_not_exist,
        Toast.LENGTH_SHORT
    )

    val defaultLeftActionItem = ActionItem(
        name = if (scaffoldState.drawerState.isOpen) {
            stringResource(id = R.string.action_item_close_left_drawer)
        } else {
            stringResource(id = R.string.action_item_open_left_drawer)
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

    // If the side drawer is open then sSet up a back press handler to close it
    if (scaffoldState.drawerState.isOpen) {
        BackPressHandler {
            scope.launch {
                scaffoldState.drawerState.close()
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            Dd5cvTopAppBar(
                title = scaffoldVM.viewState.title.resolve(),
                actionItems = scaffoldVM.viewState.actionMenu,
                leftActionItem = scaffoldVM.viewState.leftActionItem ?: defaultLeftActionItem
            )
        },
        floatingActionButton = {
            scaffoldVM.viewState.floatingActionButton?.let {
                FloatingActionButton(onClick = it.onClick) {
                    Icon(
                        imageVector = it.icon,
                        contentDescription = it.contentDescription.resolve()
                    )
                }
            }
        },
        drawerContent = {
            Dd5cvSideDrawerContent(Destination.values().map { destination ->
                DrawerMenuItem(
                    nameRes = destination.titleRes,
                    icon = destination.icon,
                    onClick = {
                        try {
                            navActions.popUpTo(destination)
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
        MainNavHost(
            navController = navController,
            setScaffold = { scaffoldVM.setScaffold(it) }
        )
    }
}