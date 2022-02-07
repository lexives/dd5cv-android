package com.delarax.dd5cv.ui.components.text

import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.delarax.dd5cv.extensions.filterToInt
import com.delarax.dd5cv.ui.theme.Dimens

@Composable
fun CondensedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = TextStyle.Default.copy(
        fontSize = Dimens.FontSize.md,
        color = MaterialTheme.colors.onSurface
    ),
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        imeAction = ImeAction.Done
    ),
    keyboardActions: KeyboardActions? = null,
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    cursorBrush: Brush = SolidColor(MaterialTheme.colors.onSurface)
) {
    val hasFocus = remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.onFocusChanged { focusState ->
            hasFocus.value = focusState.isFocused || focusState.hasFocus
        },
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
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = if (hasFocus.value) {
                            MaterialTheme.colors.primary.copy(
                                alpha = 0.5f
                            )
                        } else {
                            MaterialTheme.colors.onSurface.copy(
                                alpha = 0.25f
                            )
                        },
                        shape = RoundedCornerShape(10)
                    )
                    .padding(horizontal = Dimens.Spacing.sm, vertical = Dimens.Spacing.xs)
            ) {
                innerTextField()
            }
        }
    )
}

@Composable
fun CondensedIntTextField(
    value: String,
    onValueChange: (String) -> Unit,
    maxDigits: Int,
    modifier: Modifier = Modifier,
    includeNegatives: Boolean = false,
    includeLeadingZeros: Boolean = false,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = TextStyle.Default.copy(
        fontSize = Dimens.FontSize.md,
        color = MaterialTheme.colors.onSurface
    ),
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        imeAction = ImeAction.Done
    ),
    keyboardActions: KeyboardActions? = null,
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    cursorBrush: Brush = SolidColor(MaterialTheme.colors.onSurface)
) = CondensedTextField(
    value = value,
    onValueChange = {
        if (
            it.length <= maxDigits ||
            (includeNegatives && it.startsWith("-") && it.length == maxDigits + 1)
        ) {
            onValueChange(
                it.filterToInt(
                    maxDigits = maxDigits,
                    includeNegatives = includeNegatives,
                    includeLeadingZeros = includeLeadingZeros
                )
            )
        }
    },
    modifier = modifier,
    enabled = enabled,
    readOnly = readOnly,
    textStyle = textStyle,
    keyboardOptions = keyboardOptions.copy(keyboardType = KeyboardType.Number),
    keyboardActions = keyboardActions,
    singleLine = singleLine,
    maxLines = maxLines,
    visualTransformation = visualTransformation,
    onTextLayout = onTextLayout,
    interactionSource = interactionSource,
    cursorBrush = cursorBrush
)