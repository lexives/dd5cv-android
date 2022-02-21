package com.delarax.dd5cv.ui.components

import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.delarax.dd5cv.R
import com.delarax.dd5cv.ui.components.layout.VerticalSpacer
import com.delarax.dd5cv.ui.destinations.Destinations
import com.delarax.dd5cv.ui.theme.Dd5cvTheme
import com.delarax.dd5cv.ui.theme.Dimens

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
            style = MaterialTheme.typography.h5,
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
            verticalArrangement = Arrangement.spacedBy(Dimens.Spacing.md),
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
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable { it.onClick() }
                        .padding(vertical = Dimens.Spacing.md)
                        .fillMaxWidth()
                ) {
                    it.icon?.let { icon ->
                        Icon(
                            imageVector = icon,
                            contentDescription = stringResource(id = it.nameRes)
                        )
                        VerticalSpacer.Medium()
                    }
                    Text(
                        text = stringResource(id = it.nameRes),
                        fontSize = Dimens.FontSize.lg
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
            Dd5cvSideDrawerContent(Destinations.values().map { screen ->
                DrawerMenuItem(
                    nameRes = screen.titleRes,
                    icon = screen.icon,
                    onClick = {}
                )
            })
        }
    }
}