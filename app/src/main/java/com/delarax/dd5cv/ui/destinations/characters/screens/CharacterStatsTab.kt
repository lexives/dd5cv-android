package com.delarax.dd5cv.ui.destinations.characters.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.delarax.dd5cv.models.characters.Ability
import com.delarax.dd5cv.models.characters.Character
import com.delarax.dd5cv.models.data.State
import com.delarax.dd5cv.ui.components.PreviewSurface
import com.delarax.dd5cv.ui.destinations.characters.screens.shared.AbilityScore
import com.delarax.dd5cv.ui.theme.Dimens
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import com.google.accompanist.flowlayout.FlowRow

@Composable
fun CharacterStatsTab(
    characterState: State<Character>,
    inEditMode: Boolean,
    onAbilityScoreChanged: (Ability, Int?) -> Unit
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
                    inEditMode = inEditMode,
                )
            }
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
                onAbilityScoreChanged = {_,_ ->}
            )
        }
    }
}