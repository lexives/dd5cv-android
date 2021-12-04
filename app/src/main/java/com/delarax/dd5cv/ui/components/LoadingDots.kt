package com.delarax.dd5cv.ui.components

import android.content.res.Configuration
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.delarax.dd5cv.ui.theme.Dd5cvTheme
import com.delarax.dd5cv.ui.theme.Dimens

@Composable
fun LoadingDots(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.primary,
    minAlpha: Float = 0.4f,
    dotSize: Dp = 24.dp,
    delayUnit: Int = 350 // you can change delay to change animation speed
) {
    val minSize = 0.3f

    @Composable
    fun Dot(
        scale: Float, // for pulsing effect (size)
        alpha: Float, // for flashing effect (transparency)
    ) = Spacer(
        Modifier
            .size(dotSize)
            .scale(scale)
            .alpha(alpha)
            .background(
                color = color,
                shape = CircleShape
            )
    )

    val infiniteTransition = rememberInfiniteTransition()

    @Composable
    fun animateScaleWithDelay(delay: Int) = infiniteTransition.animateFloat(
        initialValue = minSize,
        targetValue = minSize,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = delayUnit * 4
                minSize at delay with LinearEasing
                1f at delay + delayUnit with LinearEasing
                minSize at delay + delayUnit * 2
            }
        )
    )

    val scale1 by animateScaleWithDelay(0)
    val scale2 by animateScaleWithDelay(delayUnit)
    val scale3 by animateScaleWithDelay(delayUnit * 2)

    @Composable
    fun animateAlphaWithDelay(delay: Int) = infiniteTransition.animateFloat(
        initialValue = minAlpha,
        targetValue = minAlpha,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = delayUnit * 4
                minAlpha at delay with LinearEasing
                1f at delay + delayUnit with LinearEasing
                minAlpha at delay + delayUnit * 2
            }
        )
    )

    val alpha1 by animateAlphaWithDelay(0)
    val alpha2 by animateAlphaWithDelay(delayUnit)
    val alpha3 by animateAlphaWithDelay(delayUnit * 2)

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        val spaceSize = Dimens.Spacing.xxs

        Dot(scale1, alpha1)
        Spacer(Modifier.width(spaceSize))
        Dot(scale2, alpha2)
        Spacer(Modifier.width(spaceSize))
        Dot(scale3, alpha3)
    }
}

/****************************************** Previews **********************************************/

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun LoadingDotsPreview() = Dd5cvTheme {
    PreviewSurface {
        Column(
            modifier = Modifier
                .padding(Dimens.Spacing.sm)
                .fillMaxWidth()
        ) {
            LoadingDots(
                modifier = Modifier.fillMaxWidth(),
                color = Color.Gray
            )
        }
    }
}