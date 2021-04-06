package com.delarax.dd5cv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import com.delarax.dd5cv.ui.theme.Dd5cvTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Dd5cvContent {
                Dd5cvNavigation()
            }
        }
    }
}

@Composable
fun Dd5cvContent(content: @Composable () -> Unit) {
    Dd5cvTheme {
        content()
    }
}