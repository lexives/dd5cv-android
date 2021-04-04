package com.delarax.dd5cv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.delarax.dd5cv.ui.characters.CharacterListPage
import com.delarax.dd5cv.ui.theme.Dd5cvTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Dd5cvContent {
                CharacterListPage()
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

@Preview
@Composable
fun DefaultPreview() {
    Dd5cvContent {
        CharacterListPage()
    }
}