package com.delarax.dd5cv.ui.destinations.characters.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.delarax.dd5cv.R
import com.delarax.dd5cv.extensions.mutate
import com.delarax.dd5cv.models.characters.Character
import com.delarax.dd5cv.models.characters.CharacterDefaults
import com.delarax.dd5cv.models.characters.Proficiency
import com.delarax.dd5cv.models.characters.ProficiencyLevel.EXPERT
import com.delarax.dd5cv.models.characters.ProficiencyLevel.PROFICIENT
import com.delarax.dd5cv.models.data.State
import com.delarax.dd5cv.ui.components.PreviewSurface
import com.delarax.dd5cv.ui.destinations.characters.screens.shared.DEFAULT_COLUMN_SPACING
import com.delarax.dd5cv.ui.destinations.characters.screens.shared.FIRST_COLUMN_ARRANGEMENT
import com.delarax.dd5cv.ui.destinations.characters.screens.shared.FIRST_COLUMN_WEIGHT
import com.delarax.dd5cv.ui.destinations.characters.screens.shared.ProficiencyListItem
import com.delarax.dd5cv.ui.destinations.characters.screens.shared.SECOND_COLUMN_ALIGNMENT
import com.delarax.dd5cv.ui.destinations.characters.screens.shared.SECOND_COLUMN_WEIGHT
import com.delarax.dd5cv.ui.theme.Dimens

@Composable
fun CharacterSkillsTab(
    characterState: State<Character>,
    inEditMode: Boolean,
    onToggleProficiency: (Proficiency) -> Unit,
    onToggleExpertise: (Proficiency) -> Unit
) {
    /**
     * Header & Divider
     */
    Row(
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(DEFAULT_COLUMN_SPACING),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = Dimens.Spacing.sm)
    ) {
        // First Column - Proficiency Checkmark(s) and Skill Name
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = FIRST_COLUMN_ARRANGEMENT,
            modifier = Modifier.weight(FIRST_COLUMN_WEIGHT)
        ) {
            Text(
                text = stringResource(R.string.skill_proficiency_label),
                style = MaterialTheme.typography.subtitle2,
                textAlign = TextAlign.Center,
                modifier = Modifier.width(IntrinsicSize.Min)
            )
            Spacer(modifier = Modifier.width(DEFAULT_COLUMN_SPACING))
            Text(
                text = stringResource(R.string.skill_name_label),
                style = MaterialTheme.typography.subtitle2
            )
        }

        // Second Column - Skill Bonus
        Text(
            text = stringResource(R.string.skill_bonus_label),
            style = MaterialTheme.typography.subtitle2,
            modifier = Modifier
                .weight(SECOND_COLUMN_WEIGHT)
                .wrapContentWidth(SECOND_COLUMN_ALIGNMENT)
        )
    }
    Divider(
        color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f),
        modifier = Modifier.fillMaxWidth()
    )

    /**
     * Row of each skill
     */
    characterState.getOrNull()?.let { character ->
        character.skills.forEachIndexed { i, skill ->
            val backgroundColor = if (i % 2 == 0) {
                MaterialTheme.colors.onSurface.copy(alpha = 0.1f)
            } else {
                MaterialTheme.colors.surface
            }
            ProficiencyListItem(
                proficiency = skill,
                bonus = character.skillBonuses[i],
                inEditMode = inEditMode,
                showExpertise = true,
                onToggleProficiency = onToggleProficiency,
                onToggleExpertise = onToggleExpertise,
                modifier = Modifier
                    .background(color = backgroundColor)
                    .padding(vertical = Dimens.Spacing.xs)
            )
        }
    }
}

/****************************************** Previews **********************************************/

private val demoCharacter = Character(
    proficiencyBonusOverride = 2,
    abilityScores = CharacterDefaults.abilities.associateWith {
        when (it.abbreviation) {
            "STR" -> 1
            "DEX" -> 2
            "CON" -> 3
            "INT" -> 1
            "WIS" -> 2
            else -> 0
        }
    },
    skills = CharacterDefaults.skills.mutate {
        this[1] = this[1].copy(proficiencyLevel = PROFICIENT)
        this[4] = this[4].copy(proficiencyLevel = PROFICIENT)
        this[5] = this[5].copy(proficiencyLevel = EXPERT)
        this[6] = this[6].copy(proficiencyLevel = PROFICIENT)
        this[10] = this[10].copy(proficiencyLevel = PROFICIENT)
        this[this.lastIndex] = this[this.lastIndex].copy(proficiencyLevel = PROFICIENT)
    }
)

@Preview
//@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CharacterSkillsTabPreview() {
    PreviewSurface {
        Column {
            CharacterSkillsTab(
                characterState = State.Success(demoCharacter),
                inEditMode = false,
                onToggleProficiency = {},
                onToggleExpertise = {}
            )
        }
    }
}

@Preview
//@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CharacterSkillsTabEditModePreview() {
    PreviewSurface {
        Column {
            CharacterSkillsTab(
                characterState = State.Success(demoCharacter),
                inEditMode = true,
                onToggleProficiency = {},
                onToggleExpertise = {}
            )
        }
    }
}
