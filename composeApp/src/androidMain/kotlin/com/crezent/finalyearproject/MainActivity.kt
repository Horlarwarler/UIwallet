package com.crezent.finalyearproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.crezent.finalyearproject.app.App
import com.crezent.finalyearproject.authentication.presentation.forgot_password.ForgotPasswordScreen
import com.crezent.finalyearproject.authentication.presentation.otp.OtpScreen
import com.crezent.finalyearproject.authentication.presentation.recovery_password.ResetPasswordScreen
import com.crezent.finalyearproject.core.presentation.component.NumberInputDialog
import com.crezent.finalyearproject.home.presentation.HomeScreen
import com.crezent.finalyearproject.home.presentation.component.AccountHeader
import com.crezent.finalyearproject.transaction.presentation.deposit.DepositScreen
import com.crezent.finalyearproject.transaction.presentation.new_credit_card.NewCreditCardScreen
import com.crezent.finalyearproject.transaction.presentation.new_credit_card.NewCreditCardScreenState
import com.crezent.finalyearproject.transaction.presentation.payment_method.PaymentMethodScreen
import com.crezent.finalyearproject.transaction.presentation.payment_method.PaymentMethodState
import com.crezent.finalyearproject.ui.theme.colorWhite

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
    Box(
        modifier = Modifier
            .background(colorWhite)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        NumberInputDialog(
            modifier = Modifier
        ) { }
    }
//    NewCreditCardScreen(
//        navigateBack = {},
//        state = NewCreditCardScreenState()
//    ) { }

}