package com.delarax.dd5cv.ui.destinations.characters.screens.shared

import android.content.res.Configuration
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.delarax.dd5cv.ui.components.PreviewSurface
import com.delarax.dd5cv.ui.theme.Dimens

val DEFAULT_HEALTH_BAR_HEIGHT = 20.dp

@Composable
fun HealthBar(
    currentHP: Int, // Negatives will be treated as 0
    maxHP: Int,  // Negatives will be treated as 0
    tempHP: Int,  // Negatives will be treated as 0
    modifier: Modifier = Modifier,
    barHeight: Dp = DEFAULT_HEALTH_BAR_HEIGHT,
    borderColor: Color = MaterialTheme.colors.onBackground,
    currentHPColor: Color = MaterialTheme.colors.primary,
    tempHPColor: Color = currentHPColor.copy(
        red = currentHPColor.red +.25f,
        green = currentHPColor.green +.25f,
        blue = currentHPColor.blue +.25f,
    )
) {
    val currentHPNonNegative: Int = currentHP.takeIf { it >= 0 } ?: 0
    val maxHPNonNegative: Int = maxHP.takeIf { it >= 0 } ?: 0
    val tempHPNonNegative: Int = tempHP.takeIf { it >= 0 } ?: 0

    val totalHP = currentHPNonNegative + tempHPNonNegative

    val maxHPWidth = if (totalHP != 0 && maxHPNonNegative != 0) {
        kotlin.math.min(maxHPNonNegative / totalHP.toFloat(), 1f)
    } else { 1f }

    val totalHPWidth = when {
        totalHP == 0 -> 0f
        maxHPNonNegative == 0 -> 1f
        else -> kotlin.math.min(totalHP / maxHPNonNegative.toFloat(), 1f)
    }

    val currentHPWidth = if (totalHP != 0) {
        currentHPNonNegative / totalHP.toFloat()
    } else { 0f }

    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = modifier
            .fillMaxWidth()
            .height(barHeight)
            .clip(RoundedCornerShape(10))
    ) {
        Row(
            modifier = Modifier
                .animateContentSize()
                .fillMaxWidth(fraction = totalHPWidth)
                .fillMaxHeight()
        ) {
            Box(
                modifier = Modifier
                    .background(currentHPColor)
                    .animateContentSize()
                    .fillMaxWidth(fraction = currentHPWidth)
                    .fillMaxHeight()
            )
            Box(
                modifier = Modifier
                    .background(tempHPColor)
                    .animateContentSize()
                    .fillMaxWidth()
                    .fillMaxHeight()
            )
        }
        Box(
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(10)
                )
                .animateContentSize()
                .fillMaxWidth(fraction = maxHPWidth)
                .fillMaxHeight()
        )
    }
}

/****************************************** Previews **********************************************/


@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun HealthBarPreview() {
    PreviewSurface {
        var currentHP by remember { mutableStateOf(20) }
        var maxHP by remember { mutableStateOf(20) }
        var tempHP by remember { mutableStateOf(0) }

        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimens.Spacing.xs),
                modifier = Modifier.padding(Dimens.Spacing.md)
            ) {
                ButtonsForPreview(
                    label = "Current",
                    value = currentHP,
                    setValue = { currentHP = it }
                )
                ButtonsForPreview(
                    label = "Temp",
                    value = tempHP,
                    setValue = { tempHP = it }
                )
                ButtonsForPreview(
                    label = "Max",
                    value = maxHP,
                    setValue = { maxHP = it }
                )
            }
            HealthBar(
                currentHP = currentHP,
                maxHP = maxHP,
                tempHP = tempHP,
                modifier = Modifier.padding(
                    top = Dimens.Spacing.none,
                    bottom = Dimens.Spacing.md,
                    start = Dimens.Spacing.md,
                    end = Dimens.Spacing.md
                )
            )
        }
    }
}

@Composable
private fun ButtonsForPreview(
    label: String,
    value: Int,
    setValue: (Int) -> Unit
) {
    Row(
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(Dimens.Spacing.xs)
    ) {
        Button(
            onClick = { setValue(value - 1) },
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier.size(25.dp)
        ) {
            Icon(imageVector = Icons.Default.Remove, contentDescription = null)
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = Dimens.Spacing.sm)
        ) {
            Text(text = value.toString())
            Text(text = label)
        }
        Button(
            onClick = { setValue(value + 1) },
            contentPadding = PaddingValues(Dimens.Spacing.none),
            modifier = Modifier.size(25.dp)
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = null)
        }
    }
}