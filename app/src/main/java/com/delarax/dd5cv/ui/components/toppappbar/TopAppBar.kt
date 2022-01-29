package com.delarax.dd5cv.ui.components.toppappbar

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.AppBarDefaults
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.delarax.dd5cv.R
import com.delarax.dd5cv.models.FormattedResource
import com.delarax.dd5cv.ui.components.resolve
import com.delarax.dd5cv.ui.theme.Dd5cvTheme

/**
 * Originally copy-pasted from the Compose library's [TopAppBar], with the addition of the
 * subtitle field.
 */
private val AppBarHeight = 56.dp
private val AppBarHorizontalPadding = 4.dp
// Start inset for the title when there is no navigation icon provided
private val TitleInsetWithoutIcon = Modifier.width(16.dp - AppBarHorizontalPadding)
// Start inset for the title when there is a navigation icon provided
private val TitleIconModifier = Modifier
    .fillMaxHeight()
    .width(72.dp - AppBarHorizontalPadding)

@Composable
fun Dd5cvTopAppBar(
    title: FormattedResource,
    subtitle: FormattedResource? = null,
    leftActionItem: ActionItem? = null,
    actionItems: List<ActionItem> = listOf(),
    defaultIconSpace: Int = 3, // includes overflow menu
) {
    Surface(
        color = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.onPrimary,
        elevation = AppBarDefaults.TopAppBarElevation,
        shape = RectangleShape
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppBarDefaults.ContentPadding)
                .height(AppBarHeight),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val navigationIcon: (@Composable () -> Unit)? = leftActionItem?.let {
                {
                    IconButton(onClick = leftActionItem.onClick) {
                        Icon(
                            imageVector = leftActionItem.icon,
                            contentDescription = leftActionItem.name.resolve()
                        )
                    }
                }
            }

            if (navigationIcon == null) {
                Spacer(TitleInsetWithoutIcon)
            } else {
                Row(TitleIconModifier, verticalAlignment = Alignment.CenterVertically) {
                    CompositionLocalProvider(
                        LocalContentAlpha provides ContentAlpha.high,
                        content = navigationIcon
                    )
                }
            }

            Row(
                Modifier
                    .fillMaxHeight()
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ProvideTextStyle(value = MaterialTheme.typography.h6) {
                    CompositionLocalProvider(
                        LocalContentAlpha provides ContentAlpha.high
                    ) {
                        Column(
                            Modifier.height(IntrinsicSize.Min)
                        ) {
                            Text(
                                text = title.resolve(),
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1
                            )
                            subtitle?.let {
                                Text(
                                    text = subtitle.resolve(),
                                    style = MaterialTheme.typography.caption,
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }

            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Row(
                    Modifier.fillMaxHeight(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    ActionMenu(
                        items = actionItems,
                        defaultIconSpace = defaultIconSpace
                    )
                }
            }
        }
    }
}

/****************************************** Previews **********************************************/

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Dd5cvTopAppBarPreview() {
    Dd5cvTheme {
        Dd5cvTopAppBar(
            title = FormattedResource(resId = R.string.app_name),
            subtitle = FormattedResource("This Subtitle is Optional"),
            leftActionItem = ActionItem(
                name = FormattedResource("Menu"),
                icon = Icons.Filled.Menu
            ),
            actionItems = listOf(
                ActionItem(
                    name = FormattedResource("Action Item"),
                    icon = Icons.Default.Send,
                    visibility = ActionItemMode.NEVER_SHOW
                )
            )
        )
    }
}