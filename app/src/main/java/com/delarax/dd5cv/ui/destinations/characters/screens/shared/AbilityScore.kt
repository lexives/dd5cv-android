package com.delarax.dd5cv.ui.destinations.characters.screens.shared

import android.content.res.Configuration
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.delarax.dd5cv.extensions.calculateBonus
import com.delarax.dd5cv.extensions.toBonusOrEmpty
import com.delarax.dd5cv.extensions.toStringOrEmpty
import com.delarax.dd5cv.models.characters.Ability
import com.delarax.dd5cv.ui.components.PreviewSurface
import com.delarax.dd5cv.ui.components.layout.BorderedColumn
import com.delarax.dd5cv.ui.components.text.EditableIntText
import com.delarax.dd5cv.ui.components.text.IntVisualTransformation
import com.delarax.dd5cv.ui.theme.Dimens
import com.delarax.dd5cv.ui.theme.shapes.ArcShape
import com.delarax.dd5cv.ui.theme.shapes.ArcSide

@Composable
fun AbilityScore(
    score: Int?,
    onScoreChanged: (Int?) -> Unit,
    ability: Ability,
    inEditMode: Boolean,
    modifier: Modifier = Modifier
) {
    BorderedColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        borderShape = RoundedCornerShape(10.dp),
        borderWidth = 2.dp,
        contentPadding = PaddingValues(all = Dimens.Spacing.none),
        modifier = modifier
            .width(IntrinsicSize.Min)
            .defaultMinSize(minWidth = 100.dp)
    ) {
        /**
         * Box of just the modifier so that it can have an arc border
         */
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 0.5.dp,
                    color = MaterialTheme.colors.onSurface,
                    shape = ArcShape(ArcSide.BOTTOM)
                )
        ) {
            Text(
                text = score?.calculateBonus().toBonusOrEmpty(),
                fontSize = Dimens.FontSize.lg
            )
        }

        /**
         * Column of editable score, ability name, and abbreviation
         */
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(
                top = Dimens.Spacing.xs,
                bottom = Dimens.Spacing.sm,
                start = Dimens.Spacing.md,
                end = Dimens.Spacing.md
            )
        ) {
            EditableIntText(
                text = score.toStringOrEmpty(),
                onTextChanged = { onScoreChanged(it.toIntOrNull()) },
                maxDigits = 2,
                includeNegatives = false,
                visualTransformation = IntVisualTransformation(),
                inEditMode = inEditMode,
                textStyle = TextStyle(
                    textAlign = TextAlign.Center,
                    fontSize = Dimens.FontSize.xxl,
                    color = MaterialTheme.colors.onSurface
                )
            )
            Text(
                text = ability.name,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.body2
            )
            Text(
                text = "(${ability.abbreviation})",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.caption
            )
        }
    }
}

/****************************************** Previews **********************************************/

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CenteredBorderedStatPreview() {
    PreviewSurface {
        AbilityScore(
            score = 20,
            onScoreChanged = {},
            ability = Ability(name = "Constitution", abbreviation = "CON"),
            inEditMode = false
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CenteredBorderedStatEditModePreview() {
    PreviewSurface {
        AbilityScore(
            score = 20,
            onScoreChanged = {},
            ability = Ability(name = "Constitution", abbreviation = "CON"),
            inEditMode = true
        )
    }
}