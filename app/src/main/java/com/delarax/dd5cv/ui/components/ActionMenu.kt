package com.delarax.dd5cv.ui.components

import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.delarax.dd5cv.models.FormattedResource
import com.delarax.dd5cv.ui.theme.Dd5cvTheme

// Kind of equivalent to a menu XML entry, except for the onClick lambda
data class ActionItem(
    val name: FormattedResource,
    val icon: ImageVector,
    val visibility: ActionItemMode = ActionItemMode.IF_ROOM,
    val onClick: () -> Unit = {},
)

// Whether to show the action item as an icon or in the overflow menu
enum class ActionItemMode {
    ALWAYS_SHOW, IF_ROOM, NEVER_SHOW
}

@Composable
fun ActionMenu(
    items: List<ActionItem>,
    defaultIconSpace: Int = 3, // includes overflow menu
    menuExpanded: MutableState<Boolean> = remember { mutableStateOf(false) }
) {
    // decide how many ifRoom icons to show as actions
    val (actionItems, overflowItems) = remember(items, defaultIconSpace) {
        separateIntoActionAndOverflow(items, defaultIconSpace)
    }

    for (item in actionItems) {
        IconButton(onClick = item.onClick) {
            Icon(item.icon, item.name.resolve())
        }
    }
    if (overflowItems.isNotEmpty()) {
        IconButton(onClick = { menuExpanded.value = true }) {
            Icon(Icons.Default.MoreVert, "More actions")
        }
        DropdownMenu(
            expanded = menuExpanded.value,
            onDismissRequest = { menuExpanded.value = false }
        ) {
            for (item in overflowItems) {
                DropdownMenuItem(onClick = item.onClick) {
                    //Icon(item.icon, item.name) just have text in the overflow menu
                    Text(item.name.resolve())
                }
            }
        }
    }
}

private fun separateIntoActionAndOverflow(
    items: List<ActionItem>,
    defaultIconSpace: Int
): Pair<List<ActionItem>, List<ActionItem>> {
    var (alwaysCount, neverCount, ifRoomCount) = Triple(0, 0, 0)
    for (item in items) {
        when (item.visibility) {
            ActionItemMode.ALWAYS_SHOW -> alwaysCount++
            ActionItemMode.NEVER_SHOW -> neverCount++
            ActionItemMode.IF_ROOM -> ifRoomCount++
        }
    }

    val needsOverflow = alwaysCount + ifRoomCount > defaultIconSpace || neverCount > 0
    val actionIconSpace = defaultIconSpace - (if (needsOverflow) 1 else 0)

    val actionItems = ArrayList<ActionItem>()
    val overflowItems = ArrayList<ActionItem>()

    var ifRoomsToDisplay = actionIconSpace - alwaysCount
    for (item in items) {
        when (item.visibility) {
            ActionItemMode.ALWAYS_SHOW -> {
                actionItems.add(item)
            }
            ActionItemMode.NEVER_SHOW -> {
                overflowItems.add(item)
            }
            ActionItemMode.IF_ROOM -> {
                if (ifRoomsToDisplay > 0) {
                    actionItems.add(item)
                    ifRoomsToDisplay--
                } else {
                    overflowItems.add(item)
                }

            }
        }
    }
    return Pair(actionItems, overflowItems)
}

/****************************************** Previews **********************************************/

@Preview
@Composable
private fun PreviewActionMenu() {
    Dd5cvTheme {
        val items = listOf(
            ActionItem(
                FormattedResource("Call"),
                Icons.Default.Call,
                ActionItemMode.ALWAYS_SHOW
            ),
            ActionItem(
                FormattedResource("Send"),
                Icons.Default.Send,
                ActionItemMode.IF_ROOM
            ),
            ActionItem(
                FormattedResource("Email"),
                Icons.Default.Email,
                ActionItemMode.IF_ROOM
            ),
            ActionItem(
                FormattedResource("Delete"),
                Icons.Default.Delete,
                ActionItemMode.IF_ROOM
            ),
        )
        TopAppBar(
            title = { Text("App bar") },
            navigationIcon = {
                IconButton(onClick = {}) {
                    Icon(Icons.Default.Menu, "Menu")
                }
            },
            actions = {
                ActionMenu(items, defaultIconSpace = 3)
            }
        )
    }
}