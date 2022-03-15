package com.delarax.dd5cv.ui.destinations.characters.screens.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.delarax.dd5cv.extensions.isExpert
import com.delarax.dd5cv.extensions.isProficient
import com.delarax.dd5cv.extensions.toBonus
import com.delarax.dd5cv.models.characters.Proficiency
import com.delarax.dd5cv.models.characters.ProficiencyLevel
import com.delarax.dd5cv.ui.components.SizeableCheckbox
import com.delarax.dd5cv.ui.components.layout.VerticalSpacer
import com.delarax.dd5cv.ui.theme.Dimens

const val FIRST_COLUMN_WEIGHT = 1f
const val SECOND_COLUMN_WEIGHT = 0.25f

val FIRST_COLUMN_ARRANGEMENT = Arrangement.Start
val SECOND_COLUMN_ALIGNMENT = Alignment.End

val DEFAULT_CHECKBOX_SIZE = 26.dp
val DEFAULT_COLUMN_SPACING = Dimens.Spacing.sm

@Composable
fun ProficiencyListItem(
    proficiency: Proficiency,
    bonus: Int,
    inEditMode: Boolean,
    onToggleProficiency: (Proficiency) -> Unit,
    modifier: Modifier = Modifier,
    checkboxSize: Dp = DEFAULT_CHECKBOX_SIZE,
    columnSpacing: Dp = DEFAULT_COLUMN_SPACING,
    showAbilityAbbreviation: Boolean = true,
    showExpertise: Boolean = false,
    onToggleExpertise: ((Proficiency) -> Unit) = { }
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(columnSpacing),
        modifier = modifier.fillMaxWidth()
    ) {
        // First Column - Proficiency Checkmark(s) and Name
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = FIRST_COLUMN_ARRANGEMENT,
            modifier = Modifier.weight(FIRST_COLUMN_WEIGHT)
        ) {
            // If in edit mode show toggleable checkbox(es) - one for proficiency
            // and optionally one for expertise
            if (inEditMode) {
                // Calculate start and end padding for the proficiency checkbox
                val startPadding = if (showExpertise) Dimens.Spacing.xs else Dimens.Spacing.sm
                val endPadding = if (showExpertise) Dimens.Spacing.md else Dimens.Spacing.sm

                // Proficiency checkbox
                SizeableCheckbox(
                    checked = proficiency.isProficient(),
                    onCheckedChange = { onToggleProficiency(proficiency) },
                    size = checkboxSize,
                    modifier = Modifier.padding(start = startPadding, end = endPadding)
                )

                VerticalSpacer.XXSmall()

                // Expertise Checkbox
                if (showExpertise) {
                    SizeableCheckbox(
                        checked = proficiency.isExpert(),
                        onCheckedChange = { onToggleExpertise(proficiency) },
                        size = checkboxSize,
                        modifier = Modifier.padding(end = Dimens.Spacing.xs)
                    )
                }
            }
            // Otherwise show a single icon (or no icon) for the proficiency level
            else {
                // More padding will be needed if using the expertise checkbox.
                val horizontalPadding = if (showExpertise) Dimens.Spacing.lg else Dimens.Spacing.sm + 1.dp

                ProficiencyLevelIcon(
                    proficiencyLevel = proficiency.proficiencyLevel,
                    size = checkboxSize,
                    modifier = Modifier.padding(horizontal = horizontalPadding)
                )
            }

            VerticalSpacer.Small()

            // Proficiency name and (optionally) ability score abbreviation
            Text(text = proficiency.name)
            if (showAbilityAbbreviation) {
                Text(
                    text = "(${proficiency.ability.abbreviation})",
                    style = MaterialTheme.typography.caption,
                    softWrap = false,
                    modifier = Modifier.padding(start = Dimens.Spacing.sm)
                )
            }
        }

        // Second Column - Proficiency Bonus
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
private fun ProficiencyLevelIcon(
    proficiencyLevel: ProficiencyLevel,
    size: Dp,
    modifier: Modifier = Modifier
) {
    val icon = when (proficiencyLevel) {
        ProficiencyLevel.NONE -> null
        ProficiencyLevel.PROFICIENT -> Icons.Default.Done
        ProficiencyLevel.EXPERT -> Icons.Default.DoneAll
    }
    val backgroundColor = when (proficiencyLevel) {
        ProficiencyLevel.NONE -> Color.Transparent
        ProficiencyLevel.PROFICIENT, ProficiencyLevel.EXPERT -> MaterialTheme.colors.primary
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(size)
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