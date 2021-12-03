package com.delarax.dd5cv.ui.components

import android.content.res.Configuration
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.delarax.dd5cv.ui.common.Dimens
import com.delarax.dd5cv.utils.State

@Composable
fun ViewStateExchanger(
    state: State<*> = State.Loading<Any>(0),
    loading: @Composable () -> Unit = { DefaultLoadingView() },
    error: @Composable ((() -> Unit)?, (() -> Unit)?) -> Unit = { _, _ ->
        DefaultErrorView()
    },
    empty: @Composable () -> Unit = { DefaultEmptyView() },
    success: @Composable () -> Unit,
) {
    Crossfade(
        modifier = Modifier.animateContentSize(),
        targetState = state
    ) {
        when (it) {
            is State.Loading -> loading()
            is State.Error -> error(null, null)
            is State.Empty -> empty()
            is State.Success -> success()
        }
    }
}

@Composable
fun DefaultLoadingView() {
    LoadingDots(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimens.Spacing.md),
        color = Color.Gray
    )
}

@Composable
fun DefaultErrorView(
    onErrorDetails: (() -> Unit)? = null,
    onErrorRetry: (() -> Unit)? = null,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimens.Spacing.md),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "There was an error.")
    }
}

@Composable
fun DefaultEmptyView() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimens.Spacing.md),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Nothing to see here.")
    }
}

/****************************************** Previews **********************************************/

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ViewStateExchangerInteractivePreview() {
    var stateInt by remember { mutableStateOf(1) }

    val state = when (stateInt) {
        1 -> State.Loading(0)
        2 -> State.Error(Throwable("error message"))
        3 -> State.Empty("")
        else -> State.Success("success")
    }

    PreviewSurface {
        Column(
            modifier = Modifier.padding(Dimens.Spacing.md),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ViewStateExchanger(state = state) {
                Text(text = "success")
            }

            Row(
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    modifier = Modifier.padding(Dimens.Spacing.xs),
                    onClick = { stateInt = 1 }
                ) {
                    Text(text = "Loading")
                }
                Button(
                    modifier = Modifier.padding(Dimens.Spacing.xs),
                    onClick = { stateInt = 2 }
                ) {
                    Text(text = "Error")
                }
                Button(
                    modifier = Modifier.padding(Dimens.Spacing.xs),
                    onClick = { stateInt = 3 }
                ) {
                    Text(text = "Empty")
                }
                Button(
                    modifier = Modifier.padding(Dimens.Spacing.xs),
                    onClick = { stateInt = 4 }
                ) {
                    Text(text = "Success")
                }
            }
        }
    }
}