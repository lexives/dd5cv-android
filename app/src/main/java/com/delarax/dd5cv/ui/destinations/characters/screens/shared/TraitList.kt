package com.delarax.dd5cv.ui.destinations.characters.screens.shared

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.delarax.dd5cv.models.ui.FormattedResource
import com.delarax.dd5cv.ui.components.PreviewSurface
import com.delarax.dd5cv.ui.components.layout.BrokenBorderBox
import com.delarax.dd5cv.ui.components.resolve
import com.delarax.dd5cv.ui.theme.Dimens
import kotlinx.coroutines.FlowPreview


@Composable
fun TraitList(
    traits: List<String>,
    onTraitsChanged: (List<String>) -> Unit,
    label: FormattedResource,
    inEditMode: Boolean,
    modifier: Modifier = Modifier
) {
    BrokenBorderBox(
        modifier = modifier,
        labelPadding = Dimens.Spacing.sm,
        borderContent = {
            Text(
                text = label.resolve(),
                style = MaterialTheme.typography.subtitle2,
                fontSize = Dimens.FontSize.md
            )
        }
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(Dimens.Spacing.sm),
            modifier = Modifier.padding(top = Dimens.Spacing.xxs)
        ) {
            traits.forEach {
                Text(it)
            }
        }
    }
    // note: edit mode allows them to edit and delete existing list items.
    // Need to think about if they can always add a new one or only add when in edit mode.
}

/****************************************** Previews **********************************************/

@FlowPreview
@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TraitListPreview() {
    PreviewSurface {
        TraitList(
            traits = listOf(
                "Thinking is for other people. I prefer action.",
                "I have a strong sense of fair play and always try to find the most equitable " +
                        "solution to arguments."
            ),
            onTraitsChanged = {},
            label = FormattedResource("Traits"),
            inEditMode = false
        )
    }
}