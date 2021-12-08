# D&D 5e Character Vault - Android App
A native Android app for managing characters for the Dungeons and Dragons 5th edition tabletop RPG.

## Architecture
TBD

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

### Cache plan
#### Flow
- enter edit mode
- save backup to cache
- while editing, update the character data in the **VM**
  
- **IF THE APP CLOSES UNEXPECTEDLY:**
  - save edits to cache
  - save some indicator that the user has in-progress character edits
  - **WHEN OPENING THE APP AGAIN:**
    - check if the user has in-progress edits (and what type)
    - **IF YES:**
      - show a popup asking if they want to resume
        - **IF YES:**
          - navigate to appropriate screen
          - (show loading indicator)
          - enter edit mode
          - load edits from cache and populate VM with it
          - (remove loading indicator)
        - **IF NO:**
          - clear cache (might be hard because there will be multiple caches)
          - remain on home screen
    - **IF NO:**
      - remain on home screen
  
- **IF THE USER CLOSES THE APP:**
  - (same as **IF THE APP CLOSES UNEXPECTEDLY**)
  
- **IF THE USER CONFIRMS THE EDITS:**
  - (show loading indicator)
  - submit VM edits to remote service
  - **ON SUCCESS:**
    - (remove loading indicator)
    - exit edit mode
    - clear cache
  - **ON ERROR:**
    - (remove loading indicator)
    - remain in edit mode
    - show popup saying there was an error
    
- **IF THE USER CANCELS THE EDITS:**
  - show a popup asking if they're sure
  - **IF YES:**
    - (show loading indicator)
    - load backup from cache and populate VM with it
    - exit edit mode
    - (remove loading indicator)
    - clear cache
  - **IF NO:**
    - close popup and remain on current screen
    
- **IF THE USER PRESSES THE BACK BUTTON:**
  - show a popup saying that their edits will be lost
    - **IF THEY CONFIRM:**
      - clear cache
      - navigate back to character list screen
    - **IF THEY CANCEL:**
      - close popup and remain on current screen

#### Breakdown
When to save backup:
- when entering edit mode

When to read backup:
- when cancelling edits

When to save edits:
- in edit mode, when the app quits unexpectedly
- in edit mode, when the app is shut down by the user

When to read edits:
- When resuming in-progress edits

When to save flag for in-progress edits:
- in edit mode, when the app quits unexpectedly
- in edit mode, when the app is shut down by the user

When to read flag for in-progress edits:
- Upon app start

When to clear flag for in-progress edits:
- Upon app start, if the user decides not to resume edits

When to clear cache:
- Upon app start, if the user decides not to resume edits
- After confirming edits (and successful service update)
- After canceling edits
- After pressing the back button