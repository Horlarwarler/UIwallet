package com.crezent.finalyearproject.transaction.presentation.payment

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.crezent.finalyearproject.app.ScreenNavigation
import com.crezent.finalyearproject.core.presentation.SharedData
import com.crezent.finalyearproject.core.presentation.component.TopBarLoadingIndicator
import com.crezent.finalyearproject.core.presentation.util.SnackBarController
import com.crezent.finalyearproject.core.presentation.util.SnackBarEvent.ShowSnackBar
import com.crezent.finalyearproject.core.presentation.util.observeFlowAsEvent
import com.crezent.finalyearproject.domain.util.toErrorMessage
import com.crezent.finalyearproject.ui.theme.black
import com.crezent.finalyearproject.ui.theme.colorWhite
import com.crezent.finalyearproject.ui.theme.green
import finalyearproject.composeapp.generated.resources.Res
import finalyearproject.composeapp.generated.resources.cancel
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PayStackPaymentScreen(
    screenNavigation: ScreenNavigation,
    authorizationUrl: String
) {


    val viewModel: PayStackPaymentViewModel = koinViewModel()

    val state = viewModel.state.collectAsStateWithLifecycle()
    var showClosePaymentDialog by remember {
        mutableStateOf(false)
    }

    val scope = rememberCoroutineScope()
    observeFlowAsEvent(
        flow = viewModel.channel
    ) { event ->

        when (event) {
            is PaymentScreenEvent.PaymentError -> {
                scope.launch {
                    SnackBarController.sendEvent(
                        snackBarEvent = ShowSnackBar(
                            message = event.error.toErrorMessage(),
                            duration = SnackbarDuration.Long

                        )
                    )
                }
            }

            is PaymentScreenEvent.PaymentSuccessful -> {
                SharedData.editCurrentTransaction(event.transaction)
                screenNavigation.navigateToDetailTransactionRoute()
                println("Payment Successful ${event.transaction}")
            }
        }
    }
    if (showClosePaymentDialog) {
        ClosePaymentDialog(
            onDismiss = {
                screenNavigation.navigateBack()
            },
            continuePayment = {
                showClosePaymentDialog = false
            }
        )
    }

    Box(
        modifier = Modifier
            .background(colorWhite)
            .fillMaxSize()
    ) {


        PayStackWebView(
            authorizationUrl = authorizationUrl,
            verifyPayment = {
                viewModel.handleScreenEvent(PayStackScreenAction.VerifyPayment)

            },
            closeDialog = {
                // showClosePaymentDialog = true
                screenNavigation.navigateBack()
                // screenNavigation.navigateBack()
            }
        )
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .clip(CircleShape)
                .clickable {
                    showClosePaymentDialog = true
                }
                .background(black)
                .size(40.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(Res.drawable.cancel),
                contentDescription = "close",
                tint = colorWhite
            )
        }

        TopBarLoadingIndicator(
            isLoading = state.value.isLoading,
            modifier = Modifier.align(Alignment.TopCenter)
        )

    }


}

@Composable

private fun ClosePaymentDialog(
    onDismiss: () -> Unit,
    continuePayment: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {
            continuePayment()
        },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true

        ),
        confirmButton = {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .height(50.dp)
                    .fillMaxWidth()
                    .background(green)
                    .clickable {
                        continuePayment()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Continue Payment",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center,
                    color = colorWhite


                )
            }
        },
        title = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Do you want to cancel payment",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = black


            )
        },
        text = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "You are in the middle of a transaction,please ensure you have not made any transaction before closing this dialog",
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Left,
                color = black


            )
        },
        dismissButton = {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .height(50.dp)
                    .fillMaxWidth()
                    .background(green.copy(0.5f))
                    .clickable {
                        onDismiss()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Dismiss",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center,
                    color = black

                )
            }
        }
    )

}