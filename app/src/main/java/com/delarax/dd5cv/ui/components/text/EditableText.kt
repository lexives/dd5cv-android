package com.delarax.dd5cv.ui.components.text

import android.content.res.Configuration
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.delarax.dd5cv.ui.components.PreviewSurface
import com.delarax.dd5cv.ui.theme.Dimens

@Composable
fun EditableText(
    text: String,
    onTextChanged: (String) -> Unit,
    inEditMode: Boolean,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = TextStyle.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    cursorBrush: Brush = SolidColor(Color.Black)
) {
    if (inEditMode) {
        CondensedTextField(
            value = text,
            onValueChange = onTextChanged,
            modifier = modifier,
            enabled = enabled,
            readOnly = readOnly,
            textStyle = textStyle,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = singleLine,
            maxLines = maxLines,
            visualTransformation = visualTransformation,
            onTextLayout = onTextLayout,
            interactionSource = interactionSource,
            cursorBrush = cursorBrush
        )
    } else {
        Text(
            text = text,
            style = textStyle,
            modifier = modifier.padding(
                horizontal = Dimens.Spacing.sm,
                vertical = Dimens.Spacing.xs
            )
        )
    }
}

/****************************************** Previews **********************************************/

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun EditableTextPreview() {
    val inEditMode = remember { mutableStateOf(false)}
    PreviewSurface {
        Column(
            verticalArrangement = Arrangement.spacedBy(Dimens.Spacing.sm),
            modifier = Modifier.padding(Dimens.Spacing.md)
        ) {
            EditableText(
                text = "Holdrum",
                onTextChanged = {},
                inEditMode = inEditMode.value,
                textStyle = MaterialTheme.typography.h6
            )
            Text(text = if (inEditMode.value) "Edit Mode: ON" else "Edit Mode: OFF")
            Button(onClick = { inEditMode.value = !inEditMode.value }) {
                Text(text = "Toggle Edit Mode")
            }
        }
    }
}