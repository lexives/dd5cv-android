package com.delarax.dd5cv.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.delarax.dd5cv.ui.theme.Dimens

private const val DividerAlpha = 0.25f

@Composable
fun DividerWithText(
    text: String?,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.body1,
    dividerColor: Color = MaterialTheme.colors.onSurface.copy(alpha = DividerAlpha),
    dividerThickness: Dp = 1.dp,
    textHorizontalPadding: Dp = Dimens.Spacing.md
) {
    if (!text.isNullOrEmpty()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = modifier.fillMaxWidth()
        ) {
            Divider(
                color = dividerColor,
                thickness = dividerThickness,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = text,
                style = textStyle,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(horizontal = textHorizontalPadding)
            )
            Divider(
                color = dividerColor,
                thickness = dividerThickness,
                modifier = Modifier.weight(1f)
            )
        }
    } else {
        Divider(modifier = modifier, color = dividerColor, thickness = dividerThickness)
    }
}

/****************************************** Previews **********************************************/

@Preview
@Composable
private fun DividerWithTextPreview() {
    PreviewSurface {
        DividerWithText(text = "Text")
    }
}