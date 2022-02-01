package com.delarax.dd5cv.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.LeadingIconTab
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.delarax.dd5cv.models.ui.FormattedResource
import com.delarax.dd5cv.ui.theme.Dimens
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

data class TabData(
    val text: FormattedResource,
    val icon: ImageVector? = null,
    val content: @Composable () -> Unit = {}
)

val DEFAULT_INDICATOR_HEIGHT = 4.dp

@ExperimentalPagerApi
@ExperimentalMaterialApi
@Composable
fun TabScreenLayout(
    tabs: List<TabData>,
    modifier: Modifier = Modifier,
    contentPadding: Dp = Dimens.Spacing.none,
    backgroundColor: Color = MaterialTheme.colors.primarySurface,
    contentColor: Color = contentColorFor(backgroundColor),
    indicatorColor: Color = contentColorFor(backgroundColor),
    indicatorHeight: Dp = DEFAULT_INDICATOR_HEIGHT,
    divider: @Composable () -> Unit = @Composable {
        TabRowDefaults.Divider()
    },
) {
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    Column {
        ScrollableTabRow(
            selectedTabIndex = pagerState.currentPage,
            backgroundColor = backgroundColor,
            contentColor = contentColor,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    color = indicatorColor,
                    height = indicatorHeight,
                    modifier = modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
                )
            },
            divider = divider,
            edgePadding = Dimens.Spacing.none
        ) {
            tabs.forEachIndexed { index, tabData ->
                LeadingIconTab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                          coroutineScope.launch {
                              pagerState.animateScrollToPage(index)
                          }
                    },
                    text = {
                        Text(text = tabData.text.resolve())
                    },
                    icon = {
                        tabData.icon?.let { Icon(imageVector = it, contentDescription = null) }
                    }
                )
            }
        }
        HorizontalPager(
            count = tabs.size,
            state = pagerState,
            itemSpacing = contentPadding,
            contentPadding = PaddingValues(contentPadding),
            verticalAlignment = Alignment.Top,
            modifier = Modifier.fillMaxSize()
        ) { index ->
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                tabs[index].content()
            }
        }
    }
}