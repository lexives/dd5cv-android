package com.delarax.dd5cv.ui.components

import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.staticCompositionLocalOf

@Composable
fun BackPressHandler(onBackPressed: () -> Unit) {
    // Safely update the current `onBack` lambda when a new one is provided
    val currentOnBackPressed by rememberUpdatedState(onBackPressed)

    // Remember a back pressed callback that calls the current `onBackPressed` lambda
    val backCallback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                currentOnBackPressed()
            }
        }
    }

    // Obtain the back pressed dispatcher from CompositionLocal
    val backDispatcher = LocalBackPressedDispatcher.current

    // Whenever there's a new dispatcher set up the callback. A DisposableEffect is:
    /**
     * "A side effect of composition that must run for any new unique value of key1 and must be
     * reversed or cleaned up if key1 changes or if the DisposableEffect leaves the composition."
     */
    DisposableEffect(key1 = backDispatcher) {
        backDispatcher.addCallback(backCallback)
        // When the effect leaves the Composition or there's a new dispatcher, remove the callback
        onDispose {
            backCallback.remove()
        }
    }
}

val LocalBackPressedDispatcher = staticCompositionLocalOf<OnBackPressedDispatcher> {
    error("No back dispatcher provided.")
}