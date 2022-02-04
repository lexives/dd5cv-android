package com.delarax.dd5cv.ui.components.layout

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.delarax.dd5cv.ui.theme.Dimens

@Composable
fun BorderedColumn(
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    borderWidth: Dp = 2.dp,
    borderColor: Color = MaterialTheme.colors.onSurface,
    borderShape: Shape = RectangleShape,
    contentPadding: Dp = Dimens.Spacing.md,
    content: @Composable ColumnScope.() -> Unit = {}
) = Column(
    verticalArrangement = verticalArrangement,
    horizontalAlignment = horizontalAlignment,
    modifier = modifier
        .border(
            width = borderWidth,
            color = borderColor,
            shape = borderShape
        )
        .padding(contentPadding),
    content = content
)
