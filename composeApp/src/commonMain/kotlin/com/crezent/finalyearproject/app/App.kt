package com.crezent.finalyearproject.app

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.crezent.finalyearproject.authentication.presentation.forgot_password.ForgotPasswordScreenRoot
import com.crezent.finalyearproject.authentication.presentation.otp.OtpScreenRoot
import com.crezent.finalyearproject.authentication.presentation.recovery_password.ResetPasswordScreenRoot
import com.crezent.finalyearproject.authentication.presentation.signin.SignInScreenRoot
import com.crezent.finalyearproject.authentication.presentation.signup.SignUpScreenRoot
import com.crezent.finalyearproject.core.presentation.util.SnackBarController
import com.crezent.finalyearproject.core.presentation.util.SnackBarEvent
import com.crezent.finalyearproject.core.presentation.util.observeFlowAsEvent
import com.crezent.finalyearproject.home.presentation.HomeScreenRoot
import com.crezent.finalyearproject.onboard.presentation.OnboardScreenRoot
import com.crezent.finalyearproject.splash.presesentation.SplashScreenRoot
import com.crezent.finalyearproject.transaction.presentation.deposit.DepositScreenRoot
import com.crezent.finalyearproject.transaction.presentation.new_credit_card.NewCreditCardScreenRoot
import com.crezent.finalyearproject.transaction.presentation.payment_method.PaymentMethodRoute
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
@Preview
fun App() {
    MaterialTheme {
        val navHostController = rememberNavController()

        val screenNavigation by remember(navHostController) {
            mutableStateOf(ScreenNavigation(navHostController))
        }
        val snackbarHostState = remember {
            SnackbarHostState()
        }
        val scope = rememberCoroutineScope()

        observeFlowAsEvent(
            flow = SnackBarController.snackBarEvent,
            key1 = snackbarHostState,
            onEvent = { event ->
                snackbarHostState.currentSnackbarData?.dismiss()
                if (event is SnackBarEvent.ShowSnackBar) {
                    scope.launch {
                        val result = snackbarHostState.showSnackbar(
                            message = event.message,
                            duration = event.duration,
                            actionLabel = event.snackBarAction?.name,
                            withDismissAction = event.dismissAction
                        )
                        if (result == SnackbarResult.ActionPerformed) {
                            event.snackBarAction?.action?.let { it() }
                        }
                    }

                }

            }
        )
        Scaffold(
            snackbarHost = {
                SnackbarHost(
                    hostState = snackbarHostState
                )
            }
        ) {
            NavHost(
                navController = navHostController,
                startDestination = Route.SplashRoute
            ) {

                composable<Route.SplashRoute> {
                    SplashScreenRoot(
                        screenNavigation = screenNavigation
                    )
                }

                composable<Route.OnboardRoute> {
                    OnboardScreenRoot(
                        screenNavigation = screenNavigation
                    )
                }

                navigation<Route.AuthenticationGraph>(
                    startDestination = Route.SignInRoute
                ) {
                    composable<Route.SignUpRoute> {
                        SignUpScreenRoot(
                            screenNavigation = screenNavigation
                        )
                    }

                    composable<Route.SignInRoute> {
                        SignInScreenRoot(
                            screenNavigation = screenNavigation
                        )


                    }

                    composable<Route.ForgotPassword> {
                        ForgotPasswordScreenRoot(
                            screenNavigation = screenNavigation
                        )


                    }

                    composable<Route.OtpRoute> {
                        OtpScreenRoot(
                            screenNavigation = screenNavigation
                        )

                    }

                    composable<Route.ResetPassword> {
                        ResetPasswordScreenRoot(
                            screenNavigation = screenNavigation
                        )

                    }


                }
                navigation<Route.TransactionGraph>(
                    startDestination = Route.DepositRoute
                ) {
                    composable<Route.DepositRoute> {
                        DepositScreenRoot(
                            screenNavigation = screenNavigation
                        )
                    }
                    composable<Route.PaymentMethodRoute> {
                        PaymentMethodRoute(
                            screenNavigation = screenNavigation
                        )
                    }
                    composable<Route.CreditCardRoute> {
                        NewCreditCardScreenRoot(
                            screenNavigation = screenNavigation
                        )
                    }
                }

                composable<Route.HomeRoute>() {
                    HomeScreenRoot(navigation = screenNavigation)
                }


            }
        }


    }
}