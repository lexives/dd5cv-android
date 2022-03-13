package com.delarax.dd5cv.ui.destinations.characters.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Checkbox
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.delarax.dd5cv.R
import com.delarax.dd5cv.extensions.mutate
import com.delarax.dd5cv.extensions.toBonus
import com.delarax.dd5cv.models.characters.Character
import com.delarax.dd5cv.models.characters.CharacterDefaults
import com.delarax.dd5cv.models.characters.Proficiency
import com.delarax.dd5cv.models.characters.ProficiencyLevel
import com.delarax.dd5cv.models.characters.ProficiencyLevel.EXPERT
import com.delarax.dd5cv.models.characters.ProficiencyLevel.NONE
import com.delarax.dd5cv.models.characters.ProficiencyLevel.PROFICIENT
import com.delarax.dd5cv.models.data.State
import com.delarax.dd5cv.ui.components.PreviewSurface
import com.delarax.dd5cv.ui.theme.Dimens

const val FIRST_COLUMN_WEIGHT = 1f
const val SECOND_COLUMN_WEIGHT = 0.25f

val FIRST_COLUMN_ARRANGEMENT = Arrangement.Start
val SECOND_COLUMN_ALIGNMENT = Alignment.End

val COLUMN_SPACING = Dimens.Spacing.sm
val ROW_SPACING = Dimens.Spacing.sm

val CHECKBOX_SIZE = 26.dp

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
        horizontalArrangement = Arrangement.spacedBy(COLUMN_SPACING),
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
            Spacer(modifier = Modifier.width(COLUMN_SPACING))
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
            SkillListItem(
                skill = skill,
                bonus = character.skillBonuses[i],
                inEditMode = inEditMode,
                onToggleProficiency = onToggleProficiency,
                onToggleExpertise = onToggleExpertise,
                modifier = Modifier
                    .background(color = backgroundColor)
                    .padding(vertical = ROW_SPACING / 2)
            )
        }
    }
}

@Composable
private fun SkillListItem(
    skill: Proficiency,
    bonus: Int,
    inEditMode: Boolean,
    onToggleProficiency: (Proficiency) -> Unit,
    onToggleExpertise: (Proficiency) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(COLUMN_SPACING),
        modifier = modifier.fillMaxWidth()
    ) {
        // First Column - Proficiency Checkmark(s) and Skill Name
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = FIRST_COLUMN_ARRANGEMENT,
            modifier = Modifier.weight(FIRST_COLUMN_WEIGHT)
        ) {
            if (inEditMode) {
                // If in edit mode show two checkboxes, one for proficiency and one for expertise.
                SkillCheckbox(
                    checked = skill.isProficient,
                    onCheckedChange = { onToggleProficiency(skill) },
                    modifier = Modifier.padding(start = Dimens.Spacing.xs)
                )
                Spacer(modifier = Modifier.width(Dimens.Spacing.md + Dimens.Spacing.xxs))
                SkillCheckbox(
                    checked = skill.isExpert,
                    onCheckedChange = { onToggleExpertise(skill) },
                    modifier = Modifier.padding(end = Dimens.Spacing.xs)
                )
            } else {
                // Otherwise show a single icon (or no icon) for the skill's proficiency level.
                SkillProficiencyIcon(
                    proficiencyLevel = skill.proficiencyLevel,
                    modifier = Modifier.padding(horizontal = Dimens.Spacing.lg)
                )
            }

            Spacer(modifier = Modifier.width(COLUMN_SPACING))

            // Skill name and ability score abbreviation
            Text(text = skill.name)
            Text(
                text = "(${skill.ability.abbreviation})",
                style = MaterialTheme.typography.caption,
                softWrap = false,
                modifier = Modifier.padding(start = Dimens.Spacing.sm)
            )
        }

        // Second Column - Skill Bonus
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .weight(SECOND_COLUMN_WEIGHT)
                .wrapContentWidth(SECOND_COLUMN_ALIGNMENT)
        ) {
            Text(
                text = bonus.toBonus(),
                modifier = Modifier.padding(horizontal = Dimens.Spacing.sm)
            )
        }
    }
}

@Composable
private fun SkillCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    // Compose's implementation of the checkbox always has size 20.dp with 2.dp of padding,
    // so divide checkbox size by 20.dp to get scale percentage.
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(CHECKBOX_SIZE)
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier
                .fillMaxSize()
                .scale(CHECKBOX_SIZE / 20.dp)
        )
    }
}

@Composable
private fun SkillProficiencyIcon(
    proficiencyLevel: ProficiencyLevel,
    modifier: Modifier = Modifier
) {
    val icon = when (proficiencyLevel) {
        NONE -> null
        PROFICIENT -> Icons.Default.Done
        EXPERT -> Icons.Default.DoneAll
    }
    val backgroundColor = when (proficiencyLevel) {
        NONE -> Color.Transparent
        PROFICIENT, EXPERT -> MaterialTheme.colors.primary
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(CHECKBOX_SIZE)
            .clip(CircleShape)
            .background(color = backgroundColor)
    ) {
        icon?.let {
            Icon(
                imageVector = it,
                contentDescription = null,
                modifier = Modifier.padding(Dimens.Spacing.xs),
                tint = MaterialTheme.colors.onPrimary
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
