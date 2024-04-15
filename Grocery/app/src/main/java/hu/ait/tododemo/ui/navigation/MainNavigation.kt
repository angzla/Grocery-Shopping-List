package hu.ait.grocery.ui.navigation

sealed class MainNavigation(val route: String) {
    object SplashScreen : MainNavigation("splashscreen")
    object MainScreen : MainNavigation("mainscreen") {
    }
}