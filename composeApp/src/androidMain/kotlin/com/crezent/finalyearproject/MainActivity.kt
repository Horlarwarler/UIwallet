package com.crezent.finalyearproject

import android.os.Bundle
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.crezent.finalyearproject.app.App
import com.crezent.finalyearproject.core.data.util.CurrentActivityHolder
import com.crezent.finalyearproject.core.domain.model.FundingSource
import com.crezent.finalyearproject.core.domain.model.Transaction
import com.crezent.finalyearproject.core.presentation.component.NumberInputDialog
import com.crezent.finalyearproject.transaction.TransactionStatus
import com.crezent.finalyearproject.transaction.TransactionType
import com.crezent.finalyearproject.transaction.presentation.transaction_details.TransactionDetailScreen
import com.crezent.finalyearproject.transaction.presentation.transaction_details.TransactionDetailsScreenState
import com.crezent.finalyearproject.transaction.presentation.transaction_details.component.AnimatingStatusMark
import com.crezent.finalyearproject.ui.theme.colorWhite
import com.crezent.finalyearproject.ui.theme.errorColor
import com.crezent.finalyearproject.ui.theme.green
import com.crezent.finalyearproject.ui.theme.limeColor
import com.crezent.finalyearproject.ui.theme.limePurple
import com.paystack.android.core.Paystack
import com.paystack.android.ui.paymentsheet.PaymentSheet
import finalyearproject.composeapp.generated.resources.Res
import finalyearproject.composeapp.generated.resources.baseline_question_mark_24
import finalyearproject.composeapp.generated.resources.checkmark


class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CurrentActivityHolder.set(this)
        setContent {
            App()
        }

    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    val transaction = Transaction(
        transactionId = "RM-0D0AI1234",
        transactionTitle = "Test title",
        transactionDescription = "Test description",
        transactionAmount = 10000.0,
        transactionStatus = TransactionStatus.Success,
        transactionType = TransactionType.Credit,
        paidAt = "2024-08-12",
        createdDate = "2024-08-12",
        fundingSource = FundingSource.UssdPayment("Kuda Bank")
    )
    TransactionDetailScreen(
        state = TransactionDetailsScreenState(
            transaction = transaction,
            email = "mikailramadan999@gmail.com",
            name = "Mikail Ramadan"
        ),
        action = {

        },
        navigateBack = {

        }
    ) { }
//    Box(
//        modifier = Modifier
//            .background(colorWhite)
//            .fillMaxSize(),
//        contentAlignment = Alignment.Center
//    ) {
//        AnimatingStatusMark(
//            backgroundColor = limeColor,
//            icon =  Res.drawable.baseline_question_mark_24
//        )
//    }
//    NewCreditCardScreen(
//        navigateBack = {},
//        state = NewCreditCardScreenState()
//    ) { }

}