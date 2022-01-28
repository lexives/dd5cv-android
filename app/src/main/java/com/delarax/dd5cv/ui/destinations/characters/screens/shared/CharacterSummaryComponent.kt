package com.delarax.dd5cv.ui.destinations.characters.screens.shared

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.delarax.dd5cv.R
import com.delarax.dd5cv.data.characters.remote.RemoteCharacterDataSourceMocked
import com.delarax.dd5cv.extensions.toCharacterSummaryList
import com.delarax.dd5cv.models.characters.CharacterSummary
import com.delarax.dd5cv.ui.components.PreviewSurface
import com.delarax.dd5cv.ui.theme.Dimens

@Composable
fun CharacterSummaryComponent(
    characterSummary: CharacterSummary
) {
    Column {
        Text(
            text = characterSummary.name ?: stringResource(R.string.default_character_name),
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
            RemoteCharacterDataSourceMocked.DEFAULT_CHARACTERS.toCharacterSummaryList().first()
        )
    }
}