package com.gyeolse.drinkingcalendar.ui.navigation

sealed class ScreenInfo(val route: String) {
    object Home: ScreenInfo("home_screen")
    object Statistics: ScreenInfo("statistics_screen")
    object Profile: ScreenInfo("profile_screen")

    object Settings: ScreenInfo("setting_screen")

}
