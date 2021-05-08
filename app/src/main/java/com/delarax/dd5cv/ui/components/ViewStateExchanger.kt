package com.delarax.dd5cv.ui.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.delarax.dd5cv.ui.theme.Dd5cvTheme
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
            .padding(12.dp),
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
            .padding(12.dp),
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
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Nothing to see here.")
    }
}

@Preview
@Composable
fun ViewStateExchangerInteractivePreview() {
    var stateInt by remember { mutableStateOf(1) }

    val state = when (stateInt) {
        1 -> State.Loading(0)
        2 -> State.Error(Throwable("error message"))
        3 -> State.Empty("")
        else -> State.Success("success")
    }

    Dd5cvTheme {
        Surface {
            Column(
                modifier = Modifier.padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ViewStateExchanger(state = state) {
                    Text(text = "success")
                }

                Row(
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        modifier = Modifier.padding(4.dp),
                        onClick = { stateInt = 1 }
                    ) {
                        Text(text = "Loading")
                    }
                    Button(
                        modifier = Modifier.padding(4.dp),
                        onClick = { stateInt = 2 }
                    ) {
                        Text(text = "Error")
                    }
                    Button(
                        modifier = Modifier.padding(4.dp),
                        onClick = { stateInt = 3 }
                    ) {
                        Text(text = "Empty")
                    }
                    Button(
                        modifier = Modifier.padding(4.dp),
                        onClick = { stateInt = 4 }
                    ) {
                        Text(text = "Success")
                    }
                }
            }
        }
    }
}