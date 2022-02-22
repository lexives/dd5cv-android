package com.delarax.dd5cv.ui.destinations.characters.screens.shared

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.delarax.dd5cv.R
import com.delarax.dd5cv.models.ui.DialogData
import com.delarax.dd5cv.models.ui.FormattedResource
import com.delarax.dd5cv.ui.components.PreviewSurface
import com.delarax.dd5cv.ui.components.layout.BrokenBorderBox
import com.delarax.dd5cv.ui.components.resolve
import com.delarax.dd5cv.ui.components.resolveOrEmpty
import com.delarax.dd5cv.ui.components.text.ButtonText
import com.delarax.dd5cv.ui.theme.Dimens
import com.delarax.dd5cv.ui.theme.Green600
import com.delarax.dd5cv.ui.theme.Red700
import kotlinx.coroutines.FlowPreview

private val ADD_NEW_BUTTON_HEIGHT = 42.dp

@Composable
fun TraitList(
    traits: List<String>,
    onTraitsChanged: (List<String>) -> Unit,
    label: FormattedResource,
    emptyMessage: FormattedResource,
    contentDescription: String,
    showCustomDialog: (DialogData.CustomDialog) -> Unit,
    hideDialog: () -> Unit,
    inEditMode: Boolean,
    modifier: Modifier = Modifier
) {
    BrokenBorderBox(
        modifier = modifier.fillMaxWidth(),
        labelPadding = Dimens.Spacing.sm,
        borderContent = {
            Text(
                text = label.resolveOrEmpty(),
                style = MaterialTheme.typography.subtitle2,
                fontSize = Dimens.FontSize.md
            )
        }
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(Dimens.Spacing.sm),
            modifier = Modifier
                .padding(top = Dimens.Spacing.xxs)
        ) {
            // Trait list or empty message
            if (traits.isEmpty()) {
                Text(
                    text = emptyMessage.resolveOrEmpty(),
                    style = MaterialTheme.typography.body2.copy (
                        fontStyle = FontStyle.Italic
                    ),
                    modifier = Modifier.width(IntrinsicSize.Max)
                )
            } else {
                traits.forEachIndexed { i, trait ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(Dimens.Spacing.sm)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Start,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(trait)
                        }
                        if (inEditMode) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(
                                    Dimens.Spacing.sm,
                                    Alignment.End
                                )
                            ) {
                                // todo: content descriptions
                                IconButton(
                                    onClick = {
                                        val newList = traits.toMutableList()
                                        newList.removeAt(i)
                                        onTraitsChanged(newList.toList())
                                    },
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(5.dp))
                                        .background(Red700)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = FormattedResource(
                                            R.string.delete_something,
                                            contentDescription
                                        ).resolveOrEmpty(),
                                        tint = Color.White
                                    )
                                }
                                IconButton(
                                    onClick = {
                                        showCustomDialog(
                                            getEditDialog(
                                                startingText = trait,
                                                buttonText = FormattedResource(
                                                    R.string.edit_something,
                                                    contentDescription
                                                ),
                                                onSubmit = { editedTrait ->
                                                    val newList = traits.toMutableList()
                                                    newList[i] = editedTrait
                                                    onTraitsChanged(newList.toList())
                                                },
                                                hideDialog = hideDialog
                                            )
                                        )
                                    },
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(5.dp))
                                        .background(Green600)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = FormattedResource(
                                            R.string.edit_something,
                                            contentDescription
                                        ).resolveOrEmpty(),
                                        tint = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // "Add New" button if in edit mode
            if (inEditMode) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(ADD_NEW_BUTTON_HEIGHT)
                        .clip(RoundedCornerShape(5.dp))
                        .background(MaterialTheme.colors.onSurface.copy(alpha = 0.15f))
                        .clickable {
                            showCustomDialog(
                                getEditDialog(
                                    buttonText = FormattedResource(
                                        R.string.add_new_something,
                                        contentDescription
                                    ),
                                    onSubmit = { trait ->
                                        val newList = traits.toMutableList()
                                        newList.add(trait)
                                        onTraitsChanged(newList.toList())
                                    },
                                    hideDialog = hideDialog
                                )
                            )
                        }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = FormattedResource(
                            R.string.add_new_something,
                            contentDescription
                        ).resolveOrEmpty()
                    )
                }
            }
        }
    }
}


private fun getEditDialog(
    startingText: String? = null,
    buttonText: FormattedResource,
    onSubmit: (String) -> Unit,
    hideDialog: () -> Unit
): DialogData.CustomDialog = DialogData.CustomDialog(
    onDismissRequest = hideDialog,
    content = {
        val (text, setText) = remember { mutableStateOf(startingText ?: "") }
        val focusRequester = remember { FocusRequester() }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens.Spacing.lg),
            modifier = Modifier.padding(Dimens.Spacing.lg)
        ) {
            TextField(
                value = text,
                onValueChange = setText,
                maxLines = 7,
                colors = TextFieldDefaults.textFieldColors(
                    cursorColor = MaterialTheme.colors.onSurface
                ),
                keyboardOptions = KeyboardOptions.Default.copy(
                    capitalization = KeyboardCapitalization.Sentences
                ),
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .fillMaxWidth(0.8f)
            )
            Button(
                onClick = {
                    onSubmit(text)
                    hideDialog()
                },
                enabled = text.isNotEmpty(),
                modifier = Modifier.width(IntrinsicSize.Max)
            ) {
                ButtonText(buttonText.resolve())
            }
            // Requests focus to the text field because it has modifier.focusRequester
            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }
        }
    }
)
/****************************************** Previews **********************************************/

@FlowPreview
@Preview
//@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TraitListPreview() {
    PreviewSurface {
        TraitList(
            traits = listOf(
                "Some trait",
                "I have a strong sense of fair play and always try to find the most equitable " +
                        "solution to arguments."
            ),
            onTraitsChanged = {},
            label = FormattedResource("Traits"),
            emptyMessage = FormattedResource(""),
            contentDescription = "",
            showCustomDialog = {},
            hideDialog = {},
            inEditMode = false
        )
    }
}

@FlowPreview
@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TraitListEditModePreview() {
    PreviewSurface {
        TraitList(
            traits = listOf(
                "Some trait",
                "I have a strong sense of fair play and always try to find the most equitable " +
                        "solution to arguments."
            ),
            onTraitsChanged = {},
            label = FormattedResource("Traits"),
            emptyMessage = FormattedResource(""),
            contentDescription = "",
            showCustomDialog = {},
            hideDialog = {},
            inEditMode = true
        )
    }
}