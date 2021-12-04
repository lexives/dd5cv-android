package com.delarax.dd5cv.ui.components

import android.content.res.Configuration
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.delarax.dd5cv.R
import com.delarax.dd5cv.models.FormattedResource
import com.delarax.dd5cv.ui.resolve
import com.delarax.dd5cv.ui.theme.Dd5cvTheme

@Composable
fun Dd5cvTopAppBar(
    title: FormattedResource,
    leftActionItem: ActionItem? = null,
    actionItems: List<ActionItem> = listOf(),
    defaultIconSpace: Int = 3, // includes overflow menu
) {
    TopAppBar(
        title = {
            Text(text = title.resolve())
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

/****************************************** Previews **********************************************/

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Dd5cvTopAppBarPreview() {
    Dd5cvTheme {
        Dd5cvTopAppBar(
            title = FormattedResource(resId = R.string.app_name),
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