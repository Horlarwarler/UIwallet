package com.crezent.finalyearproject.app

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.crezent.finalyearproject.authentication.presentation.forgot_password.ForgotPasswordScreenRoot
import com.crezent.finalyearproject.authentication.presentation.otp.OtpScreenRoot
import com.crezent.finalyearproject.authentication.presentation.recovery_password.ResetPasswordScreenRoot
import com.crezent.finalyearproject.authentication.presentation.signin.SignInScreenRoot
import com.crezent.finalyearproject.authentication.presentation.signup.SignUpScreenRoot
import com.crezent.finalyearproject.onboard.presentation.OnboardScreenRoot
import com.crezent.finalyearproject.splash.presesentation.SplashScreenRoot
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
@Preview
fun App() {
    MaterialTheme {
        val navHostController = rememberNavController()

        val screenNavigation by remember(navHostController) {
            mutableStateOf(ScreenNavigation(navHostController))
        }

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


        }
    }
}