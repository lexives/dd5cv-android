package com.delarax.dd5cv.ui.components.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.delarax.dd5cv.models.ui.FormattedResource
import com.delarax.dd5cv.models.ui.ButtonData
import com.delarax.dd5cv.models.ui.DialogState
import com.delarax.dd5cv.ui.components.PreviewSurface
import com.delarax.dd5cv.ui.components.VerticalSpacer
import com.delarax.dd5cv.ui.components.resolve
import com.delarax.dd5cv.ui.theme.Dimens

@Composable
fun Dialog(dialogState: DialogState) {
    AlertDialog(
        onDismissRequest = dialogState.onDismissRequest,
        title = { Text(text = dialogState.title.resolve()) },
        text = { Text(text = dialogState.message.resolve()) },
        buttons = {
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.padding(Dimens.Spacing.lg).fillMaxWidth()
            ) {
                dialogState.secondaryAction?.let {
                    Button(onClick = it.onClick) {
                        Text(text = it.text.resolve(), textAlign = TextAlign.Center)
                    }
                }
                VerticalSpacer.Medium()
                dialogState.mainAction?.let {
                    Button(onClick = it.onClick) {
                        Text(text = it.text.resolve())
                    }
                }
            }
        }
    )
}

/****************************************** Previews **********************************************/

@Preview
@Composable
private fun DialogPreview() {
    PreviewSurface {
        Dialog(
            dialogState = DialogState(
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