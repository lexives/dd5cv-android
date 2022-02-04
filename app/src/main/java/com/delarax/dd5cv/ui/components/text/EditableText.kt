package com.delarax.dd5cv.ui.components.text

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
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
    textStyle: TextStyle = TextStyle.Default.copy(
        fontSize = Dimens.FontSize.md,
        color = MaterialTheme.colors.onSurface
    ),
    backgroundColor: Color? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        imeAction = ImeAction.Done
    ),
    keyboardActions: KeyboardActions? = null,
    singleLine: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    cursorBrush: Brush = SolidColor(MaterialTheme.colors.onSurface)
) {
    val focusManager = LocalFocusManager.current

    if (inEditMode) {
        CondensedTextField(
            value = text,
            onValueChange = onTextChanged,
            modifier = backgroundColor?.let {
                modifier.background(color = it)
            } ?: modifier,
            enabled = enabled,
            readOnly = readOnly,
            textStyle = textStyle,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions ?: KeyboardActions {
                focusManager.clearFocus()
            },
            singleLine = singleLine,
            maxLines = maxLines,
            visualTransformation = visualTransformation,
            onTextLayout = onTextLayout,
            interactionSource = interactionSource,
            cursorBrush = cursorBrush,
        )
    } else {
        Text(
            text = visualTransformation.filter(AnnotatedString(text)).text,
            style = textStyle,
            modifier = (
                backgroundColor?.let {
                    modifier.background(
                        color = it,
                        shape = RoundedCornerShape(10)
                    )
                } ?: modifier
            ).padding(
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