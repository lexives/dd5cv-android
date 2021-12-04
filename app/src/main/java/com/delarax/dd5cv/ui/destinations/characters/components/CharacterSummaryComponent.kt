package com.delarax.dd5cv.ui.destinations.characters.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.delarax.dd5cv.data.characters.CharacterRepoMockData
import com.delarax.dd5cv.extensions.toCharacterSummaryList
import com.delarax.dd5cv.models.characters.CharacterSummary
import com.delarax.dd5cv.ui.components.PreviewSurface
import com.delarax.dd5cv.ui.destinations.characters.screens.CharacterClasses
import com.delarax.dd5cv.ui.theme.Dimens

@Composable
fun CharacterSummaryComponent(characterSummary: CharacterSummary) {
    Column {
        Text(
            text = characterSummary.name ?: "Name is null",
            style = MaterialTheme.typography.h6
        )
        Spacer(Modifier.height(Dimens.Spacing.sm))
        CharacterClasses(classes = characterSummary.classes)
    }
}

/****************************************** Previews **********************************************/

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CharacterSummaryComponentPreview() {
    PreviewSurface {
        CharacterSummaryComponent(
            CharacterRepoMockData.DEFAULT_CHARACTERS.toCharacterSummaryList().first()
        )
    }
}