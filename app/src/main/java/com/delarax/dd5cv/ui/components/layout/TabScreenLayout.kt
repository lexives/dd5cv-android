package com.delarax.dd5cv.ui.components.layout

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.LeadingIconTab
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.delarax.dd5cv.models.ui.FormattedResource
import com.delarax.dd5cv.ui.components.resolve
import com.delarax.dd5cv.ui.theme.Dimens
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState

data class TabData(
    val text: FormattedResource,
    val icon: ImageVector? = null,
    val content: @Composable () -> Unit = {}
)

@ExperimentalPagerApi
@ExperimentalMaterialApi
@Composable
fun TabScreenLayout(
    tabData: List<TabData>,
    scrollable: Boolean,
    modifier: Modifier = Modifier,
    contentPadding: Dp = Dimens.Spacing.none,
    backgroundColor: Color = MaterialTheme.colors.primarySurface,
    contentColor: Color = contentColorFor(backgroundColor),
    indicatorColor: Color = contentColorFor(backgroundColor),
    indicatorHeight: Dp =  4.dp,
    divider: @Composable () -> Unit = @Composable {
        TabRowDefaults.Divider()
    },
) {
    val orientationState = remember { mutableStateOf(Configuration.ORIENTATION_PORTRAIT) }
    var collapsingToolbarScaffoldState = rememberCollapsingToolbarScaffoldState()
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    val tabs = tabData.mapIndexed { index, it ->
        it.toLeadingIconTab(
            index = index,
            pagerState = pagerState,
            coroutineScope = coroutineScope
        )
    }

    // Listen for changes in orientation. If it changes, reset the height of the collapsable
    // toolbar to its full height.
    val configuration = LocalConfiguration.current
    if (configuration.orientation != orientationState.value) {
        collapsingToolbarScaffoldState = rememberCollapsingToolbarScaffoldState()
        orientationState.value = configuration.orientation
    }

    CollapsingToolbarScaffold(
        state = collapsingToolbarScaffoldState,
        scrollStrategy = ScrollStrategy.EnterAlways,
        modifier = Modifier,
        toolbar = {
            if (scrollable) {
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
                    tabs.forEach { it() }
                }
            } else {
                TabRow(
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
                    divider = divider
                ) {
                    tabs.forEach { it() }
                }
            }
        }
    ) {
        HorizontalPager(
            count = tabData.size,
            state = pagerState,
            itemSpacing = contentPadding,
            contentPadding = PaddingValues(contentPadding),
            verticalAlignment = Alignment.Top,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) { index ->
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                tabData[index].content()
            }
        }
    }
}

@ExperimentalMaterialApi
@ExperimentalPagerApi
@Composable
private fun TabData.toLeadingIconTab(
    index: Int,
    pagerState: PagerState,
    coroutineScope: CoroutineScope
) : @Composable () -> Unit = {
    LeadingIconTab(
        selected = pagerState.currentPage == index,
        onClick = {
            coroutineScope.launch {
                pagerState.animateScrollToPage(index)
            }
        },
        text = {
            Text(text = text.resolve())
        },
        icon = {
            icon?.let { Icon(imageVector = it, contentDescription = null) }
        }
    )
}