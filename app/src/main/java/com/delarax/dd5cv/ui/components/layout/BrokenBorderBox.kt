package com.delarax.dd5cv.ui.components.layout

import android.content.res.Configuration
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.delarax.dd5cv.ui.components.PreviewSurface
import com.delarax.dd5cv.ui.components.drawWithoutRect
import com.delarax.dd5cv.ui.theme.Dimens
import kotlinx.coroutines.FlowPreview

@Composable
fun BrokenBorderBox(
    modifier: Modifier = Modifier,
    labelPosition: PaddingValues = PaddingValues(
        start = Dimens.Spacing.md,
        top = Dimens.Spacing.xxs
    ),
    labelPadding: Dp = Dimens.Spacing.none,
    contentPadding: PaddingValues = PaddingValues(Dimens.Spacing.md),
    borderWidth: Dp = 1.dp,
    borderColor: Color = MaterialTheme.colors.onSurface,
    borderShape: Shape = RoundedCornerShape(10.dp),
    borderContent: @Composable () -> Unit = {},
    content: @Composable () -> Unit = {}
) {
    Box {
        var textCoordinates by remember { mutableStateOf<Rect?>(null) }

        // Label Text
        Row(
            modifier = Modifier
                .padding(labelPosition)
                .onGloballyPositioned {
                    textCoordinates = it.boundsInParent()
                        .inflate(labelPadding.value)
                }
        ) {
            borderContent()
        }

        // Bordered box with list of traits
        Box(
            modifier = modifier
                .drawWithoutRect(textCoordinates)
                .padding(top = Dimens.Spacing.md)
                .border(
                    width = borderWidth,
                    color = borderColor,
                    shape = borderShape
                )
                .padding(contentPadding),
        ) {
            content()
        }
    }
}

/****************************************** Previews **********************************************/

@FlowPreview
@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun BrokenBorderBoxPreview() {
    PreviewSurface {
        BrokenBorderBox(
            borderContent = {
                Text("Label")
            }
        ) {
            Text(text = "Some content")
        }
    }
}