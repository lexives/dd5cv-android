package com.delarax.dd5cv.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun SizeableCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    size: Dp,
    modifier: Modifier = Modifier
) {
    // Compose's implementation of the checkbox always has size 20.dp with 2.dp of padding,
    // so divide checkbox size by 20.dp to get scale percentage.
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(size)
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier
                .fillMaxSize()
                .scale(size / 20.dp)
        )
    }
}