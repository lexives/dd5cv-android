# D&D 5e Character Vault - Android App
An android app for managing characters for the Dungeons and Dragons 5th edition tabletop RPG.

## How to Run Locally
TBD

## Contributors
Developed by Lexi Ives

## License
TBD

## Notes

### Regarding View Models
Source: https://developer.android.com/jetpack/compose/libraries#viewmodel
```
@Composable
fun MyExample() {
// Returns the same instance as long as the activity is alive,
// just as if you grabbed the instance from an Activity or Fragment
val viewModel: ExampleViewModel = viewModel()
}

@Composable
fun MyExample2() {
val viewModel: ExampleViewModel = viewModel() // Same instance as in MyExample
}
```