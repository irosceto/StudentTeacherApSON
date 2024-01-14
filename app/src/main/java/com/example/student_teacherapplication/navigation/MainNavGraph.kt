package com.example.student_teacherapplication.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import com.example.student_teacherapplication.screens.MainViewModel
import com.example.student_teacherapplication.screens.main.MainScreen
import com.example.student_teacherapplication.screens.authentication.ForgotPasswordScreen
import com.example.student_teacherapplication.screens.authentication.SignInScreen
import com.example.student_teacherapplication.screens.authentication.SignUpScreen
import com.example.student_teacherapplication.screens.splash.SplashScreen



@Composable
fun MainNavGraph(
    navController: NavHostController,
    viewModel: MainViewModel,
    startDestination: String = MainDirection.Splash.route,
) {

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        splashScreenNav(navController)
        signInScreenNav(viewModel, navController)
        signUpScreenNav(viewModel, navController)
        forgotPasswordScreenNav(viewModel, navController)
        mainScreenNav(viewModel, navController)
    }
}

private fun NavGraphBuilder.splashScreenNav(
    navController: NavHostController,
) {
    composable(
        route = MainDirection.Splash.route
    ) {
        SplashScreen(
            navigateToHome = {
                navController.navigate(MainDirection.MainScreen.route, navOptions {
                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                })
            },
            navigateToSignIn = {
                navController.navigate(MainDirection.SignIn.route, navOptions {
                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                })
            }
        )
    }
}


private fun NavGraphBuilder.forgotPasswordScreenNav(
    viewModel: MainViewModel,
    navController: NavHostController,
) {
    composable(
        route = MainDirection.ForgotPassword.route
    ) {
        ForgotPasswordScreen(
            viewModel = viewModel,
            navigateBack = {
                navController.popBackStack()
            }
        )
    }
}


private fun NavGraphBuilder.signInScreenNav(
    viewModel: MainViewModel,
    navController: NavHostController,
) {
    composable(
        route = MainDirection.SignIn.route
    ) {
        SignInScreen(
            viewModel = viewModel,
            onLoginSuccess = { navController.navigate(MainDirection.MainScreen.route) },
            onRegisterClick = { navController.navigate(MainDirection.SignUp.route) },
            onForgotPasswordClick = { navController.navigate(MainDirection.ForgotPassword.route) }
        )
    }
}


private fun NavGraphBuilder.signUpScreenNav(
    viewModel: MainViewModel,
    navController: NavHostController,
) {
    composable(
        route = MainDirection.SignUp.route
    ) {
        SignUpScreen(
            viewModel = viewModel,
            navigateToHomeScreen = {
                navController.navigate(MainDirection.MainScreen.route, navOptions {
                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                })
            }
        )
    }
}

private fun NavGraphBuilder.mainScreenNav(
    viewModel: MainViewModel,
    navController: NavHostController,
) {
    composable(
        route = MainDirection.MainScreen.route
    ) {
        MainScreen(viewModel = viewModel, onSignOut = {
            navController.navigate(MainDirection.SignIn.route, navOptions {
                popUpTo(navController.graph.id) {
                    inclusive = true
                }
            })
        })
    }
}