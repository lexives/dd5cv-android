package com.delarax.dd5cv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import com.delarax.dd5cv.ui.characters.CharacterListVM
import com.delarax.dd5cv.ui.theme.Dd5cvTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    // View Models
    private val characterListVM by viewModels<CharacterListVM>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Dd5cvContent {
                Dd5cvNavigation(characterListVM)
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