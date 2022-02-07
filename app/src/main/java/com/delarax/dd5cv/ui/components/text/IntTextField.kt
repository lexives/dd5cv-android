package com.delarax.dd5cv.ui.components.text

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import com.delarax.dd5cv.extensions.filterToInt
import com.delarax.dd5cv.ui.theme.Dimens

@Composable
fun IntTextField(
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
    singleLine: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    val focusManager = LocalFocusManager.current
    val hasFocus = remember { mutableStateOf(false) }

    TextField(
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
        modifier = modifier.onFocusChanged { focusState ->
            hasFocus.value = focusState.isFocused || focusState.hasFocus
        },
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        keyboardOptions = keyboardOptions.copy(keyboardType = KeyboardType.Number),
        keyboardActions = keyboardActions ?: KeyboardActions {
            focusManager.clearFocus()
        },
        singleLine = singleLine,
        maxLines = maxLines,
        visualTransformation = visualTransformation,
        interactionSource = interactionSource
    )
}