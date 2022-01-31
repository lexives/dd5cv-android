package com.delarax.dd5cv.ui.destinations.characters.screens.shared

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.delarax.dd5cv.ui.components.PreviewSurface

val HEALTH_BAR_HEIGHT = 20.dp

@Composable
fun HealthBar(
    currentHP: Int,
    maxHP: Int,
    tempHP: Int,
    modifier: Modifier = Modifier,
    borderColor: Color = MaterialTheme.colors.onBackground,
    currentHPColor: Color = MaterialTheme.colors.primary,
    tempHPColor: Color = currentHPColor.copy(
        red = currentHPColor.red +.25f,
        green = currentHPColor.green +.25f,
        blue = currentHPColor.blue +.25f,
    )
) {
    val totalHP = currentHP + tempHP

    val maxHPWidth = if (totalHP != 0 && maxHP != 0) {
        kotlin.math.min(maxHP / totalHP.toFloat(), 1f)
    } else { 1f }

    val totalHPWidth = when {
        totalHP == 0 -> 0f
        maxHP == 0 -> 1f
        else -> kotlin.math.min(totalHP / maxHP.toFloat(), 1f)
    }

    val currentHPWidth = if (totalHP != 0) {
        currentHP / totalHP.toFloat()
    } else { 0f }

    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = modifier
            .fillMaxWidth()
            .height(HEALTH_BAR_HEIGHT)
            .clip(RoundedCornerShape(10))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(fraction = totalHPWidth)
                .fillMaxHeight()
        ) {
            Box(
                modifier = Modifier
                    .background(currentHPColor)
                    .fillMaxWidth(fraction = currentHPWidth)
                    .fillMaxHeight()
            )
            Box(
                modifier = Modifier
                    .background(tempHPColor)
                    .fillMaxWidth()
                    .fillMaxHeight()
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth(fraction = maxHPWidth)
                .fillMaxHeight()
                .border(
                    width = 1.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(10)
                )
        )
    }
}

/****************************************** Previews **********************************************/


@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun HealthBarPreview() {
    PreviewSurface {
        HealthBar(currentHP = 10, maxHP = 20, tempHP = 5, modifier = Modifier.padding(10.dp))
    }
}