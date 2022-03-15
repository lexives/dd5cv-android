package com.delarax.dd5cv.ui.destinations.characters.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.delarax.dd5cv.R
import com.delarax.dd5cv.models.characters.Ability
import com.delarax.dd5cv.models.characters.Character
import com.delarax.dd5cv.models.characters.Proficiency
import com.delarax.dd5cv.models.data.State
import com.delarax.dd5cv.ui.components.PreviewSurface
import com.delarax.dd5cv.ui.components.divider.DividerWithText
import com.delarax.dd5cv.ui.components.divider.VerticalDivider
import com.delarax.dd5cv.ui.destinations.characters.screens.shared.AbilityScore
import com.delarax.dd5cv.ui.destinations.characters.screens.shared.ProficiencyListItem
import com.delarax.dd5cv.ui.theme.Dimens
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import com.google.accompanist.flowlayout.FlowRow

@Composable
fun CharacterStatsTab(
    characterState: State<Character>,
    inEditMode: Boolean,
    onAbilityScoreChanged: (Ability, Int?) -> Unit,
    onToggleSavingThrowProficiency: (Proficiency) -> Unit
) {
    characterState.getOrNull()?.let { character ->
        /**
         * Adaptive row of main stats
         */
        FlowRow(
            mainAxisAlignment = FlowMainAxisAlignment.Center,
            mainAxisSpacing = Dimens.Spacing.md,
            crossAxisSpacing = Dimens.Spacing.md,
            modifier = Modifier.fillMaxWidth()
        ) {
            character.abilityScores.forEach {
                AbilityScore(
                    score = it.value,
                    onScoreChanged = { newValue ->
                        onAbilityScoreChanged(it.key, newValue)
                    },
                    ability = it.key,
                    inEditMode = inEditMode
                )
            }
        }

        /**
         * Saving Throws divider
         */
        DividerWithText(
            text = stringResource(R.string.saving_throws_label),
            modifier = Modifier.padding(vertical = Dimens.Spacing.md)
        )

        /**
         * Box with list of saving throws and a vertical divider
         */
        Box(
            contentAlignment = Alignment.TopStart,
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .border(
                    color = MaterialTheme.colors.onSurface,
                    width = 2.dp,
                    shape = RoundedCornerShape(10.dp)
                )
        ) {
            Column(modifier = Modifier.padding(Dimens.Spacing.sm)) {
                character.savingThrows.forEachIndexed { i, savingThrow ->
                    ProficiencyListItem(
                        proficiency = savingThrow,
                        bonus = character.savingThrowBonuses[i],
                        inEditMode = inEditMode,
                        showAbilityAbbreviation = false,
                        onToggleProficiency = onToggleSavingThrowProficiency,
                        modifier = Modifier.padding(vertical = Dimens.Spacing.xxs)
                    )
                }
            }
            VerticalDivider(
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier.padding(start = 54.dp)
            )
        }
    }
}

/****************************************** Previews **********************************************/

@Preview
@Composable
private fun CharacterStatsTabPreview() {
    PreviewSurface {
        Column {
            CharacterStatsTab(
                characterState = State.Success(Character()),
                inEditMode = false,
                onAbilityScoreChanged = {_,_ ->},
                onToggleSavingThrowProficiency = {}
            )
        }
    }
}