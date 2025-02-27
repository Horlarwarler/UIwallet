package com.crezent.finalyearproject.app

import androidx.navigation.NavController

class ScreenNavigation(val navController: NavController) {


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
        navController.popBackStack()
    }

    val navigateToForgotPassword: () -> Unit = {
        navController.navigate(Route.ForgotPassword)
    }

    val navigateToOtpScreen: (
        String, String
    ) -> Unit = { email, purpose ->
        println("Email address $email and  purpose is $purpose ")
        navController.navigate(Route.OtpRoute(emailAddress = email, purpose = purpose))
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

    val navigateToHome: () -> Unit = {
        val entryList = navController.currentBackStack.value.mapNotNull {
            it.destination.route
        }
        println("Previous screen is $entryList")
        val lastScreen = entryList.first()
        //val previousScreen = navController.currentBackStackEntry?.destination?.route
        navController.navigate(Route.HomeRoute) {
            lastScreen.let {
                popUpTo(lastScreen) {
                    inclusive = true
                }
            }
        }
    }
    val navigateToSetting: () -> Unit = {
        navController.navigate(Route.SettingRoute)
    }
    val navigateToTransactionRoute: () -> Unit = {
        navController.navigate(Route.TransactionGraph)
    }
    val navigateToCreditCardRoute: () -> Unit = {
        navController.navigate(Route.CreditCardRoute)
    }

    val navigateToDepositRoute: () -> Unit = {
        navController.navigate(Route.DepositRoute)
    }

    val navigateToDetailTransactionRoute: () -> Unit = {
        navController.navigate(Route.TransactionDetailRoute) {
            popUpTo(Route.HomeRoute)
        }
    }

    val navigateToPaymentMethod: (String, String) -> Unit = { authorizationUrl, referenceCode ->
        navController.navigate(
            Route.PaymentMethodRoute(
                authorizationUrl = authorizationUrl,
                reference = referenceCode
            )
        )
    }

}