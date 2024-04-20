BeyondCalendar is an Android calendar library

![calendar](https://github.com/climbeyond/beyondcalendar/assets/12541406/8685c170-a0e1-434a-8908-23a3b87a9ea1)

## Features

Features include:
* Header showing current year and month
* Show one month in view
* Swipe to change month between defined month interval
* Add markers to any given day

## Download
Android, iOS and macOS is available through github packages

## Example

### Code
```
    val calendar = BeyondCalendar(Settings, object : BeyondCalendar.Listener {
        override fun logMessage(message: String) {
            // Exists only for debugging - no need to implement
        }
    
        override fun onDateSelected(date: LocalDate) {
            // Date node clicked in the view
        }
    
        override fun onHeaderTodayClicked() {
            // Moved back to today
        }
    
        override fun onInitialized(date: LocalDate) {
            // Triggered when first time composed to view
        }
    
        override fun onMonthSelected(date: LocalDate) {
            // Swipe change month
        }
    })

    // Add accents to dates
    calendar.clearAccents()
    calendar.setAccents(mutableMapOf(
        2 to mutableListOf(Accent("id1", Accent.AccentType.DOT, Color.Blue)),
        15 to mutableListOf(
            Accent("id1", Accent.AccentType.DOT, Color.Blue),
            Accent("id2", Accent.AccentType.DOT, Color.Cyan)),
    ))

    // In compose call to show the calendar
    calendar.View()
```

### Setup & Gradle

Library requires resources to be added to composeResources directory.
* Drawable: beyond_calendar_today.xml
* Fonts: titillium_web_light.ttf and titillium_web_regular.ttf

```
dependencyResolutionManagement {
    repositories {
        maven {
            name = "Github Packages - Climbeyond"
            url = uri("https://maven.pkg.github.com/climbeyond/beyondcalendar")
            credentials {
                // Github packages require logged in user
                username = "User"
                password = "Password"
            }
        }
    }
}
```

## License

    Copyright 2024 Misa Munde

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
