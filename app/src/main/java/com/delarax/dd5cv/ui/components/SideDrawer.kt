package com.delarax.dd5cv.ui.components

import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.delarax.dd5cv.R
import com.delarax.dd5cv.ui.common.Dimens
import com.delarax.dd5cv.ui.common.Destination
import com.delarax.dd5cv.ui.theme.Dd5cvTheme

data class DrawerMenuItem(
    @StringRes val nameRes: Int,
    val icon: ImageVector? = null,
    val onClick: () -> Unit = {},
)

@Composable
fun Dd5cvSideDrawerContent(
    menuItems: List<DrawerMenuItem>
) {
    Column {
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(
                top = Dimens.Spacing.lg,
                bottom = Dimens.Spacing.none,
                start = Dimens.Spacing.lg,
                end = Dimens.Spacing.lg
            )
        )
        Divider(
            modifier = Modifier.padding(vertical = Dimens.Spacing.md)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = Dimens.Spacing.none,
                    bottom = Dimens.Spacing.lg,
                    start = Dimens.Spacing.lg,
                    end = Dimens.Spacing.lg
                )
        ) {
            menuItems.forEach {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { it.onClick() }
                        .padding(vertical = Dimens.Spacing.sm),
                ) {
                    it.icon?.let { icon ->
                        Icon(
                            imageVector = icon,
                            contentDescription = stringResource(id = it.nameRes)
                        )
                        Spacer(modifier = Modifier.padding(end = Dimens.Spacing.md))
                    }
                    Text(
                        text = stringResource(id = it.nameRes)
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
private fun Dd5cvSideDrawerPreview() {
    Dd5cvTheme {
        Surface {
            Dd5cvSideDrawerContent(Destination.values().map { screen ->
                DrawerMenuItem(
                    nameRes = screen.titleRes,
                    icon = screen.icon,
                    onClick = {}
                )
            })
        }
    }
}