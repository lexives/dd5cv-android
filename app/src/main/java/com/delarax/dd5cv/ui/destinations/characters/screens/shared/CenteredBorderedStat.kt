package com.delarax.dd5cv.ui.destinations.characters.screens.shared

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.delarax.dd5cv.models.ui.FormattedResource
import com.delarax.dd5cv.ui.components.layout.BorderedColumn
import com.delarax.dd5cv.ui.components.resolve
import com.delarax.dd5cv.ui.components.text.EditableIntText
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
        modifier = Modifier
            .height(height)
            .width(width)
    ) {
        Row(
            verticalAlignment = Alignment.Bottom
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
                ),
                modifier = statModifier,
            )
            suffix?.let {
                Text(
                    text = it.resolve(),
                    fontSize = Dimens.FontSize.sm,
                    modifier = Modifier.padding(bottom = Dimens.Spacing.sm)
                )
            }
        }
        Text(
            text = label,
            textAlign = TextAlign.Center,
            fontSize = Dimens.FontSize.sm,
            modifier = labelModifier
        )
    }
}