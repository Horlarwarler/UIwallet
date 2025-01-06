package com.crezent.finalyearproject.app

import androidx.navigation.NavController

class ScreenNavigation(private val navController: NavController) {


    val navigateToOnboard: () -> Unit = {
        navController.navigate(Route.OnboardRoute) {
            popUpTo(Route.SplashRoute) {
                inclusive = true
            }
        }
    }

    val navigateToSignIn: () -> Unit = {
        navController.navigate(Route.SignInRoute) {
            popUpTo(Route.SplashRoute) {
                inclusive = true
            }
        }
    }

    val navigateToPin: () -> Unit = {
        navController.navigate(Route.PinRoute) {
            popUpTo(Route.SplashRoute) {
                inclusive = true
            }
        }
    }

    val navigateToSignUp: () -> Unit = {
        val previousScreen = navController.currentBackStackEntry?.destination?.route
        navController.navigate(Route.SignUpRoute) {
            previousScreen?.let {
                popUpTo(it) {
                    inclusive = true
                }
            }
        }
    }
    val navigateBack: () -> Unit = {
        navController.navigateUp()
    }

    val navigateToForgotPassword: () -> Unit = {
        navController.navigate(Route.ForgotPassword)
    }

    val navigateToOtpScreen: (
        String
    ) -> Unit = { email ->
        navController.navigate(Route.OtpRoute(emailAddress = email))
    }

    val navigateToResetPassword: () -> Unit = {
        val previousScreen = navController.currentBackStackEntry?.destination?.route
        navController.navigate(Route.ResetPassword) {
            previousScreen?.let {
                popUpTo(previousScreen) {
                    inclusive = true
                }
            }
        }

    }

}