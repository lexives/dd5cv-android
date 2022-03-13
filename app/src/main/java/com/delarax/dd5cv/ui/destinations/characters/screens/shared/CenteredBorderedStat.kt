package com.delarax.dd5cv.ui.destinations.characters.screens.shared

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.delarax.dd5cv.models.ui.FormattedResource
import com.delarax.dd5cv.ui.components.PreviewSurface
import com.delarax.dd5cv.ui.components.layout.BorderedColumn
import com.delarax.dd5cv.ui.components.layout.VerticalSpacer
import com.delarax.dd5cv.ui.components.resolve
import com.delarax.dd5cv.ui.components.text.EditableIntText
import com.delarax.dd5cv.ui.components.text.IntVisualTransformation
import com.delarax.dd5cv.ui.theme.Dimens

@Composable
fun CenteredBorderedStat(
    height: Dp,
    width: Dp,
    text: String,
    onTextChanged: (String) -> Unit,
    visualTransformation: VisualTransformation,
    maxDigits: Int,
    borderShape: Shape,
    inEditMode: Boolean,
    label: String,
    modifier: Modifier = Modifier,
    statModifier: Modifier = Modifier,
    labelModifier: Modifier = Modifier,
    borderWidth: Dp = 2.dp,
    fontSize: TextUnit = Dimens.FontSize.xxl,
    suffix: FormattedResource? = null,
    includeNegatives: Boolean = false
) {
    BorderedColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        borderShape = borderShape,
        borderWidth = borderWidth,
        modifier = modifier
            .height(height)
            .width(width)
    ) {
        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = statModifier,
        ) {
            EditableIntText(
                text = text,
                onTextChanged = onTextChanged,
                maxDigits = maxDigits,
                includeNegatives = includeNegatives,
                visualTransformation = visualTransformation,
                inEditMode = inEditMode,
                textStyle = TextStyle(
                    textAlign = TextAlign.Center,
                    fontSize = fontSize,
                    color = MaterialTheme.colors.onSurface
                )
            )
            if (suffix != null && !inEditMode) {
                VerticalSpacer.XSmall()
                Text(
                    text = suffix.resolve(),
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.padding(bottom = Dimens.Spacing.sm)
                )
            }
        }
        Text(
            text = label,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.body2,
            modifier = labelModifier
        )
    }
}

/****************************************** Previews **********************************************/

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CenteredBorderedStatPreview() {
    PreviewSurface {
        CenteredBorderedStat(
            height = 100.dp,
            width = 90.dp,
            text = "20",
            onTextChanged = {},
            visualTransformation = IntVisualTransformation(),
            maxDigits = 2,
            borderShape = RoundedCornerShape(10.dp),
            inEditMode = false,
            label = "Label"
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CenteredBorderedStatEditModePreview() {
    PreviewSurface {
        CenteredBorderedStat(
            height = 100.dp,
            width = 90.dp,
            text = "20",
            onTextChanged = {},
            visualTransformation = IntVisualTransformation(),
            maxDigits = 2,
            borderShape = RoundedCornerShape(10.dp),
            inEditMode = true,
            label = "Label"
        )
    }
}