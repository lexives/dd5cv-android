package com.delarax.dd5cv.ui.components.text

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import com.delarax.dd5cv.extensions.formatAsBonus

class BonusVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val originalText = text.text
        val formattedText = originalText.formatAsBonus()

        return TransformedText(
            text = AnnotatedString(formattedText),
            offsetMapping = object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int {
                    return if (formattedText.length > originalText.length) {
                        offset + 1
                    } else {
                        offset
                    }
                }

                override fun transformedToOriginal(offset: Int): Int {
                    return if (formattedText.length > originalText.length) {
                        offset - 1
                    } else {
                        offset
                    }
                }

            }
        )
    }
}