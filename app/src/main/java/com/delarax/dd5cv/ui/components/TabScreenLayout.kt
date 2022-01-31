package com.delarax.dd5cv.ui.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.delarax.dd5cv.models.ui.FormattedResource
import com.delarax.dd5cv.ui.theme.Dimens

data class TabData(
    val text: FormattedResource,
    val icon: ImageVector? = null,
    val content: @Composable () -> Unit = {}
)

val INDICATOR_HEIGHT = 4.dp

@Composable
fun TabScreenLayout(
    tabs: List<TabData>,
    modifier: Modifier = Modifier,
    contentPadding: Dp = Dimens.Spacing.none,
    backgroundColor: Color = MaterialTheme.colors.primarySurface,
    contentColor: Color = contentColorFor(backgroundColor),
    indicatorColor: Color = contentColorFor(backgroundColor),
    indicatorHeight: Dp = INDICATOR_HEIGHT,
    divider: @Composable () -> Unit = @Composable {
        TabRowDefaults.Divider()
    },
) {
    val showIcons: Boolean = tabs.find { it.icon != null } != null
    var selectedTabIndex by remember { mutableStateOf(0) }

    Column(modifier = modifier.fillMaxWidth()) {
        ScrollableTabRow(
            selectedTabIndex = selectedTabIndex,
            backgroundColor = backgroundColor,
            contentColor = contentColor,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    color = indicatorColor,
                    height = indicatorHeight,
                    modifier = modifier.tabIndicatorOffset(tabPositions[selectedTabIndex])
                )
            },
            divider = divider,

        ) {
            tabs.forEachIndexed { i, tabData ->
                if (showIcons) {
                    Tab(
                        selected = i == selectedTabIndex,
                        onClick = { selectedTabIndex = i },
                        text = {
                            Text(text = tabData.text.resolve())
                        },
                        icon = {
                            tabData.icon?.let { Icon(imageVector = it, contentDescription = null) }
                        }
                    )
                } else {
                    Tab(
                        selected = i == selectedTabIndex,
                        onClick = { selectedTabIndex = i },
                        text = {
                            Text(text = tabData.text.resolve())
                        },
                    )
                }
            }
        }
        Crossfade(
            modifier = Modifier.animateContentSize(),
            targetState = selectedTabIndex
        ) {
            Column(modifier = Modifier.padding(contentPadding)) {
                tabs[it].content()
            }
        }
    }
}