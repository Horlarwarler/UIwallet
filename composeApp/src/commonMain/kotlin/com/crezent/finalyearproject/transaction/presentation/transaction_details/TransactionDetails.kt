package com.crezent.finalyearproject.transaction.presentation.transaction_details

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposableTargetMarker
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.crezent.finalyearproject.app.ScreenNavigation
import com.crezent.finalyearproject.core.presentation.component.ActionButton
import com.crezent.finalyearproject.core.presentation.component.CustomAppBar
import com.crezent.finalyearproject.core.presentation.component.NumberInputDialog
import com.crezent.finalyearproject.core.presentation.component.TopBarLoadingIndicator
import com.crezent.finalyearproject.transaction.TransactionStatus
import com.crezent.finalyearproject.transaction.presentation.deposit.DepositScreenAction
import com.crezent.finalyearproject.transaction.presentation.deposit.DepositScreenState
import com.crezent.finalyearproject.transaction.presentation.deposit.component.AmountInputField
import com.crezent.finalyearproject.transaction.presentation.payment_method.PaymentScreenAction
import com.crezent.finalyearproject.transaction.presentation.transaction_details.component.AnimatingStatusMark
import com.crezent.finalyearproject.transaction.presentation.transaction_details.component.TransactionDetailsBox
import com.crezent.finalyearproject.ui.theme.NunitoFontFamily
import com.crezent.finalyearproject.ui.theme.background
import com.crezent.finalyearproject.ui.theme.black
import com.crezent.finalyearproject.ui.theme.errorColor
import com.crezent.finalyearproject.ui.theme.failedColor
import com.crezent.finalyearproject.ui.theme.green
import com.crezent.finalyearproject.ui.theme.largePadding
import com.crezent.finalyearproject.ui.theme.limeColor
import com.crezent.finalyearproject.ui.theme.limePurple
import com.crezent.finalyearproject.ui.theme.mediumPadding
import com.crezent.finalyearproject.ui.theme.smallPadding
import finalyearproject.composeapp.generated.resources.Res
import finalyearproject.composeapp.generated.resources.baseline_question_mark_24
import finalyearproject.composeapp.generated.resources.cancel
import finalyearproject.composeapp.generated.resources.checkmark
import finalyearproject.composeapp.generated.resources.`continue`
import finalyearproject.composeapp.generated.resources.deposit
import finalyearproject.composeapp.generated.resources.deposit_transaction_pending
import finalyearproject.composeapp.generated.resources.deposit_transaction_successful
import finalyearproject.composeapp.generated.resources.deposit_transaction_unsuccessful
import finalyearproject.composeapp.generated.resources.new_payment
import finalyearproject.composeapp.generated.resources.next
import finalyearproject.composeapp.generated.resources.printer
import finalyearproject.composeapp.generated.resources.retry_payment
import finalyearproject.composeapp.generated.resources.transaction_pending
import finalyearproject.composeapp.generated.resources.transaction_successful
import finalyearproject.composeapp.generated.resources.transaction_unsuccessful
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TransactionDetailsRoot(
    navigation: ScreenNavigation,
) {
    val viewmodel: TransactionDetailsViewmodel = koinViewModel()

    TransactionDetailScreen(
        state = viewmodel.state.value,
        action = {},
        navigateBack = {
            navigation.navigateBack()
        },
        openNewTransaction = {
            navigation.navigateToDepositRoute()
        }
    )
}

@Composable
fun TransactionDetailScreen(
    state: TransactionDetailsScreenState,
    action: (TransactionDetailsAction) -> Unit,
    navigateBack: () -> Unit,
    openNewTransaction: () -> Unit
) {

    var icon by remember {
        mutableStateOf<DrawableResource?>(null)
    }
    var backgroundColor by remember {
        mutableStateOf<Color?>(null)
    }
    var animateColor by remember {
        mutableStateOf<Color?>(null)
    }
    var title by remember {
        mutableStateOf<StringResource?>(null)
    }
    var description by remember {
        mutableStateOf<StringResource?>(null)
    }
    when (state.transaction.transactionStatus) {
        TransactionStatus.Success -> {
            icon = Res.drawable.checkmark
            backgroundColor = green
            title = Res.string.transaction_successful
            description = Res.string.deposit_transaction_successful
        }

        TransactionStatus.Failed -> {
            icon = Res.drawable.cancel
            backgroundColor = errorColor
            title = Res.string.transaction_unsuccessful
            animateColor = failedColor
            description = Res.string.deposit_transaction_unsuccessful

        }

        else -> {
            icon = Res.drawable.baseline_question_mark_24
            backgroundColor = limeColor
            title = Res.string.transaction_pending
            description = Res.string.deposit_transaction_pending
        }
    }

    Column(
        modifier = Modifier
            .background(background)
            .fillMaxSize()
            .navigationBarsPadding()
            .systemBarsPadding()

    ) {
        ///Scrollable Part


        //   val scrollState = rememberScrollState()
        CustomAppBar(
            modifier = Modifier
                .padding(top = mediumPadding, start = mediumPadding, end = mediumPadding)
                .fillMaxWidth(),
            onNavigationIconClick = navigateBack,
            topText = stringResource(Res.string.deposit),
            rightAction = {
                Icon(
                    painter = painterResource(Res.drawable.printer),
                    contentDescription = "Print",
                    modifier = Modifier
                        .clickable {
                            action(TransactionDetailsAction.PrintReceipt)
                        }
                        .align(Alignment.CenterEnd)
                        .size(25.dp)
                )
            }

        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = mediumPadding)
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            //Sign In Button

            Spacer(
                modifier = Modifier.height(25.dp)
            )
            Box(
                modifier = Modifier.size(100.dp),
                contentAlignment = Alignment.Center
            ) {
                AnimatingStatusMark(
                    backgroundColor = backgroundColor!!,
                    icon = icon!!,
                    animateColor = animateColor ?: backgroundColor!!.copy(0.6f)
                )
            }

            Text(
                text = stringResource(title!!),
                fontFamily = NunitoFontFamily(),
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp,
                color = black
            )
            Spacer(
                modifier = Modifier.height(smallPadding)
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = stringResource(description!!),
                fontFamily = NunitoFontFamily(),
                fontWeight = FontWeight.Light,
                fontSize = 16.sp,
                color = black,
                textAlign = TextAlign.Center,
                lineHeight = 30.sp
            )

            Spacer(
                modifier = Modifier.height(smallPadding)
            )
            TransactionDetailsBox(
                transaction = state.transaction,
                color = animateColor ?: backgroundColor!!.copy(0.6f),
                email = state.email,
                name = state.name
            )


        }
        val buttonText by remember {
            mutableStateOf(
                if (state.transaction.transactionStatus == TransactionStatus.Success) {
                    Res.string.new_payment

                } else {
                    Res.string.retry_payment
                }
            )
        }
        Spacer(
            modifier = Modifier.height(20.dp)
        )
        ActionButton(
            modifier = Modifier
                .navigationBarsPadding()
                .padding(bottom = largePadding, start = mediumPadding, end = mediumPadding)
                .fillMaxWidth(),
            text = stringResource(buttonText),
            height = 50.dp,
            isEnable = true,
            shouldEnableClick = true,
            onClick = openNewTransaction
        )


    }


}
