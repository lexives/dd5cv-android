package com.delarax.dd5cv.ui.components.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.delarax.dd5cv.models.ui.ButtonData
import com.delarax.dd5cv.models.ui.DialogData
import com.delarax.dd5cv.models.ui.DialogData.CustomDialog
import com.delarax.dd5cv.models.ui.DialogData.MessageDialog
import com.delarax.dd5cv.models.ui.FormattedResource
import com.delarax.dd5cv.ui.components.PreviewSurface
import com.delarax.dd5cv.ui.components.layout.VerticalSpacer
import com.delarax.dd5cv.ui.components.resolve
import com.delarax.dd5cv.ui.theme.Dimens

@Composable
fun Dialog(dialogData: DialogData) {
    when (dialogData) {
        is MessageDialog -> {
            AlertDialog(
                onDismissRequest = dialogData.onDismissRequest,
                title = { DialogTitle(dialogData.title) },
                text = { Text(text = dialogData.message.resolve()) },
                buttons = {
                    Row(
                        horizontalArrangement = dialogData.buttonAlignment,
                        modifier = Modifier
                            .padding(
                                start = Dimens.Spacing.lg,
                                end = Dimens.Spacing.lg,
                                bottom = Dimens.Spacing.lg
                            )
                            .fillMaxWidth()
                    ) {
                        dialogData.secondaryAction?.let {
                            Button(onClick = it.onClick) {
                                Text(text = it.text.resolve(), textAlign = TextAlign.Center)
                            }
                            VerticalSpacer.Medium()
                        }
                        dialogData.mainAction?.let {
                            Button(onClick = it.onClick, modifier = it.modifier) {
                                Text(text = it.text.resolve())
                            }
                        }
                    }
                }
            )
        }
        is CustomDialog -> {
            androidx.compose.ui.window.Dialog(
                onDismissRequest = dialogData.onDismissRequest
            ) {
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    color = MaterialTheme.colors.surface,
                    contentColor = MaterialTheme.colors.onSurface
                ) {
                    Column {
                        dialogData.title?.let {
                            DialogTitle(title = it, modifier = Modifier.padding(Dimens.Spacing.md))
                        }
                        dialogData.content()
                    }
                }
            }
        }
    }
}

@Composable
private fun DialogTitle(
    title: FormattedResource,
    modifier: Modifier = Modifier
) = Text(text = title.resolve(), fontSize = Dimens.FontSize.lg, modifier = modifier)

/****************************************** Previews **********************************************/

@Preview
@Composable
private fun DialogPreview() {
    PreviewSurface {
        Dialog(
            dialogData = MessageDialog(
                title = FormattedResource("Title"),
                message = FormattedResource("Message"),
                mainAction = ButtonData(
                    text = FormattedResource("Main")
                ),
                secondaryAction = ButtonData(
                    text = FormattedResource("Secondary")
                )
            )
        )
    }
}