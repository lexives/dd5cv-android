package com.delarax.dd5cv.ui.common

import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.delarax.dd5cv.R
import com.delarax.dd5cv.ui.theme.Dd5cvTheme

@Composable
fun Dd5cvTopAppBar(
    @StringRes titleRes: Int,
    leftActionItem: ActionItem? = null,
    actionItems: List<ActionItem> = listOf(),
    defaultIconSpace: Int = 3, // includes overflow menu
) {
    TopAppBar(
        title = {
            Text(text = stringResource(id = titleRes))
        },
        backgroundColor = MaterialTheme.colors.primary,
        navigationIcon = leftActionItem?.let {
            {
                IconButton(onClick = leftActionItem.onClick) {
                    Icon(
                        imageVector = leftActionItem.icon,
                        contentDescription = leftActionItem.name
                    )
                }
            }
        },
        actions = {
            ActionMenu(
                items = actionItems,
                defaultIconSpace = defaultIconSpace
            )
        }
    )
}

@Composable
@Preview
fun Dd5cvTopAppBarPreview() {
    Dd5cvTheme {
        Dd5cvTopAppBar(
            titleRes = R.string.app_name,
            leftActionItem = ActionItem(
                name = "Menu",
                icon = Icons.Filled.Menu
            ),
            actionItems = listOf(
                ActionItem(
                    name = "Action Item",
                    icon = Icons.Default.Send,
                    visibility = ActionItemMode.NEVER_SHOW
                )
            )
        )
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun Dd5cvTopAppBarDarkPreview() {
    Dd5cvTheme {
        Dd5cvTopAppBar(
            titleRes = R.string.app_name,
            leftActionItem = ActionItem(
                name = "Menu",
                icon = Icons.Filled.Menu
            ),
            actionItems = listOf(
                ActionItem(
                    name = "Action Item",
                    icon = Icons.Default.Send,
                    visibility = ActionItemMode.NEVER_SHOW
                )
            )
        )
    }
}