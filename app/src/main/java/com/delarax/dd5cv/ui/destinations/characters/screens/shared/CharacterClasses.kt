package com.delarax.dd5cv.ui.destinations.characters.screens.shared

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.delarax.dd5cv.R
import com.delarax.dd5cv.models.characters.CharacterClassLevel
import com.delarax.dd5cv.ui.components.VerticalSpacer
import com.delarax.dd5cv.ui.theme.Dimens

@Composable
fun CharacterClasses(classes: List<CharacterClassLevel>) {
    Row {
        val sortedClasses = classes.sortedByDescending {
            it.level
        }
        if (classes.isEmpty()) {
            Text(
                text = "This character has not selected a class",
                style = MaterialTheme.typography.body2,
                fontStyle = FontStyle.Italic
            )
        } else {
            for (characterClass in sortedClasses) {
                Row {
                    Card(
                        shape = RoundedCornerShape(size = 8.dp),
                        elevation = 0.dp,
                        backgroundColor = MaterialTheme.colors.onSurface.copy(
                            alpha = 0.2f
                        ),
                    ) {
                        val className: String = characterClass.name.ifEmpty {
                            stringResource(R.string.default_class_name)
                        }
                        val level: String = characterClass.level.toString()
                        Text(
                            text = "$className lv. $level",
                            style = MaterialTheme.typography.body2,
                            modifier = Modifier.padding(
                                vertical = Dimens.Spacing.xxs,
                                horizontal = Dimens.Spacing.sm
                            )
                        )
                    }
                    VerticalSpacer.Medium()
                }
            }
        }
    }
}