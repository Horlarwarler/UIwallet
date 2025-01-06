package com.crezent.finalyearproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.crezent.finalyearproject.app.App
import com.crezent.finalyearproject.authentication.presentation.forgot_password.ForgotPasswordScreen
import com.crezent.finalyearproject.authentication.presentation.otp.OtpScreen
import com.crezent.finalyearproject.authentication.presentation.recovery_password.ResetPasswordScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    ResetPasswordScreen()

}