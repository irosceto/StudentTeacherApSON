package com.example.student_teacherapplication.navigation



sealed class MainDirection(
    protected val appScreen: AppScreen
) {

    object Splash : MainDirection(
        appScreen = AppScreen.SPLASH
    ) {
        val route = appScreen.path
    }
    object SignIn : MainDirection(
        appScreen = AppScreen.SIGN_IN
    ) {
        val route = appScreen.path
    }

    object ForgotPassword : MainDirection(
        appScreen = AppScreen.FORGOT_PASSWORD
    ) {
        val route = appScreen.path
    }


    object SignUp : MainDirection(
        appScreen = AppScreen.SIGN_UP
    ) {
        val route = appScreen.path
    }

    object MainScreen : MainDirection(
        appScreen = AppScreen.MAIN
    ) {
        val route = appScreen.path
    }

    object Appointment : MainDirection(
        appScreen = AppScreen.APPOINTMENT
    ) {
        val route = appScreen.path
    }

    object Comments : MainDirection(
        appScreen = AppScreen.COMMENTS
    ) {
        val route = appScreen.path
    }
}


enum class AppScreen(
    val path: String,
) {

    SPLASH("splash"),
    SIGN_IN("sign_in"),
    FORGOT_PASSWORD("forgot_password"),
    SIGN_UP("sign_up"),
    MAIN("main_screen"),
    APPOINTMENT("appointment"),
    COMMENTS("comments"),
}