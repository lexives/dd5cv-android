package com.delarax.dd5cv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import com.delarax.dd5cv.ui.characters.CharacterListScreen
import com.delarax.dd5cv.ui.characters.CharacterListVM
import com.delarax.dd5cv.ui.theme.Dd5cvTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // TODO: move this to a fragment (or however compose does single activity navigation)
    private val characterListVM by viewModels<CharacterListVM>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Dd5cvContent {
                CharacterListFragment(characterListVM)
            }
        }
    }
}

// TODO: This should be in the fragment too
@Composable
fun CharacterListFragment(characterListVM: CharacterListVM) {
    CharacterListScreen(characterList = characterListVM.characterList)
}

@Composable
fun Dd5cvContent(content: @Composable () -> Unit) {
    Dd5cvTheme {
        content()
    }
}