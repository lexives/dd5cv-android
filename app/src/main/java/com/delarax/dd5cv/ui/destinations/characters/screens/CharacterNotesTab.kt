package com.delarax.dd5cv.ui.destinations.characters.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.delarax.dd5cv.R
import com.delarax.dd5cv.extensions.mutate
import com.delarax.dd5cv.models.characters.Character
import com.delarax.dd5cv.models.data.State
import com.delarax.dd5cv.ui.components.text.EditableText
import com.delarax.dd5cv.ui.theme.Dimens
import com.delarax.dd5cv.ui.theme.Red700

private val ADD_NEW_BUTTON_HEIGHT = 42.dp

@Composable
fun CharacterNotesTab(
    characterState: State<Character>,
    inEditMode: Boolean,
    onNotesChanged: (List<String>) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Dimens.Spacing.md)
    ) {
        characterState.getOrNull()?.notes?.let { notes ->
            // Either an empty message or the list of notes
            if (notes.isEmpty()) {
                Text(
                    text = stringResource(R.string.notes_list_empty_message),
                    style = MaterialTheme.typography.body2.copy(
                        fontStyle = FontStyle.Italic
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                )
            } else {
                notes.forEachIndexed { i, note ->
                    NoteListItem(
                        text = note,
                        inEditMode = inEditMode,
                        onTextChanged = { newNote ->
                            onNotesChanged(notes.mutate {
                                this[i] = newNote
                            })
                        },
                        onDelete = {
                            onNotesChanged(notes.mutate {
                                this.removeAt(i)
                            })
                        }
                    )
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
                            onNotesChanged(notes.mutate { this.add("") })
                        }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(
                            R.string.add_new_note_button_content_description
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun NoteListItem(
    text: String,
    onTextChanged: (String) -> Unit,
    onDelete: () -> Unit,
    inEditMode: Boolean
) {
    val focusRequester = remember { FocusRequester() }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens.Spacing.md)
    ) {
        // Editable note text
        EditableText(
            text = text,
            onTextChanged = onTextChanged,
            inEditMode = inEditMode,
            singleLine = false,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .weight(1f)
        )

        if (inEditMode) {
            // Delete button
            IconButton(
                onClick = onDelete,
                modifier = Modifier
                    .clip(RoundedCornerShape(5.dp))
                    .background(Red700)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(
                        R.string.delete_note_button_content_description
                    ),
                    tint = Color.White
                )
            }

            // Requests focus to the text field because it has modifier.focusRequester
            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }
        }
    }
}