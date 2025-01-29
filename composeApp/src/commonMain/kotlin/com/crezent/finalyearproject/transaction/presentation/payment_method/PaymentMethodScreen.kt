package com.crezent.finalyearproject.transaction.presentation.payment_method

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.crezent.finalyearproject.VERIFY_EMAIL_PURPOSE
import com.crezent.finalyearproject.app.ScreenNavigation
import com.crezent.finalyearproject.authentication.presentation.signin.SignInEvent
import com.crezent.finalyearproject.core.domain.model.Card
import com.crezent.finalyearproject.core.domain.util.Animations
import com.crezent.finalyearproject.core.presentation.component.ActionButton
import com.crezent.finalyearproject.core.presentation.component.CustomAppBar
import com.crezent.finalyearproject.core.presentation.component.NumberInputDialog
import com.crezent.finalyearproject.core.presentation.component.SectionHeader
import com.crezent.finalyearproject.core.presentation.util.SnackBarController
import com.crezent.finalyearproject.core.presentation.util.SnackBarEvent
import com.crezent.finalyearproject.core.presentation.util.SnackBarEvent.*
import com.crezent.finalyearproject.core.presentation.util.observeFlowAsEvent
import com.crezent.finalyearproject.domain.util.toErrorMessage
import com.crezent.finalyearproject.transaction.presentation.deposit.DepositScreenAction
import com.crezent.finalyearproject.transaction.presentation.deposit.component.AmountInputField
import com.crezent.finalyearproject.transaction.presentation.payment_method.component.CardMethod
import com.crezent.finalyearproject.transaction.presentation.payment_method.component.NewPayment
import com.crezent.finalyearproject.transaction.presentation.payment_method.util.PaymentMethod
import com.crezent.finalyearproject.ui.theme.background
import com.crezent.finalyearproject.ui.theme.largePadding
import com.crezent.finalyearproject.ui.theme.mediumPadding
import finalyearproject.composeapp.generated.resources.Res
import finalyearproject.composeapp.generated.resources.bank_transfer
import finalyearproject.composeapp.generated.resources.`continue`
import finalyearproject.composeapp.generated.resources.credit_debit
import finalyearproject.composeapp.generated.resources.deposit
import finalyearproject.composeapp.generated.resources.next
import finalyearproject.composeapp.generated.resources.payment_from_code
import finalyearproject.composeapp.generated.resources.payment_methods
import finalyearproject.composeapp.generated.resources.send
import finalyearproject.composeapp.generated.resources.transfer_from_bank
import finalyearproject.composeapp.generated.resources.ussd
import finalyearproject.composeapp.generated.resources.ussd_payment
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel


@OptIn(ExperimentalResourceApi::class)
@Composable
fun PaymentMethodRoute(
    screenNavigation: ScreenNavigation
) {
    val viewModel: PaymentScreenViewModel = koinViewModel()
    val state = viewModel.paymentMethodState.collectAsStateWithLifecycle().value

    PaymentMethodScreen(
        navigateBack = screenNavigation.navigateBack,
        state = state,
        onAction = viewModel::handlePaymentScreenAction,
        navigateToAddNewCard = screenNavigation.navigateToCreditCardRoute
    )

    val scope = rememberCoroutineScope()
    var loadingAnimationJson by remember {
        mutableStateOf<String?>(null)
    }
    observeFlowAsEvent(
        flow = viewModel.paymentEventChannel, onEvent = { event ->
            when (event) {
                // Show dialog
                //Show snackbar
                /// Navigate to sign home page
                // Navigate to ot else -> Unit
                is PaymentMethodEvent.CvvVerificationFailure -> {
                    scope.launch {
                        SnackBarController.sendEvent(
                            snackBarEvent = ShowSnackBar(
                                message = event.error.toErrorMessage(),
                                duration = SnackbarDuration.Long

                            )
                        )

                    }
                }
            }
        }
    )

    LaunchedEffect(state.isLoading) {
        if (state.isLoading && loadingAnimationJson == null) {
            loadingAnimationJson = Res.readBytes(Animations.LOADING_CIRCLE).decodeToString()
        }
    }

}


@Composable
fun PaymentMethodScreen(
    navigateBack: () -> Unit,
    state: PaymentMethodState = PaymentMethodState(),
    onAction: (PaymentScreenAction) -> Unit = {},
    navigateToAddNewCard: () -> Unit
) {

    var numberInputDialogVisible by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .background(background)
            .fillMaxSize()
            .navigationBarsPadding()
            .systemBarsPadding()

    ) {
        ///Scrollable Part


        val scrollState = rememberScrollState()

        CustomAppBar(
            modifier = Modifier
                .padding(top = mediumPadding, start = mediumPadding)
                .fillMaxWidth(),
            onNavigationIconClick = navigateBack,
            topText = stringResource(Res.string.payment_methods)

        )


        Column(
            modifier = Modifier
                .padding(horizontal = mediumPadding)
                .verticalScroll(state = rememberScrollState())
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            if (numberInputDialogVisible) {
                                numberInputDialogVisible = false
                            }
                        }
                    )
                }
                .weight(1f)
        ) {

            //Sign In Button
            Spacer(
                modifier = Modifier.height(50.dp)
            )
            SectionHeader(
                text = Res.string.credit_debit
            )
            Spacer(
                modifier = Modifier.height(mediumPadding)
            )
            CardMethod(
                modifier = Modifier.fillMaxWidth(),
                onCardClick = { selectedCard ->
                    if (state.verifiedCard[selectedCard.cardId] == true) {
                        onAction(
                            PaymentScreenAction.SelectPaymentMethod(
                                paymentMethod = PaymentMethod.CardPayment(
                                    selectedCard
                                )
                            )
                        )
                    } else {
                        onAction(PaymentScreenAction.OnSelectCurrentCard(card = selectedCard))
                    }
                },
                addNewCard = {
                    navigateToAddNewCard()
                },
                openKeyBoard = {
                    numberInputDialogVisible = true
                },
                verifyCvv = {
                    onAction(PaymentScreenAction.VerifyCvv)
                },
                cards = state.cards,
                selectedPayment = state.selectedPayment,
                currentSelectedCard = state.currentSelectedCard,
                cvvText = state.cvvText
            )
            Spacer(
                modifier = Modifier.height(mediumPadding)
            )
            SectionHeader(
                text = Res.string.ussd_payment
            )
            Spacer(
                modifier = Modifier.height(mediumPadding)
            )
            NewPayment(
                isSelected = state.selectedPayment != null && state.selectedPayment is PaymentMethod.UssdPayment,
                modifier = Modifier,
                canSelect = true,
                icon = Res.drawable.ussd,
                title = Res.string.ussd_payment,
                description = Res.string.payment_from_code,
                onClick = {
                    if (numberInputDialogVisible) {
                        numberInputDialogVisible = false
                    }

                    onAction(PaymentScreenAction.SelectPaymentMethod(PaymentMethod.UssdPayment))
                }
            )

            Spacer(
                modifier = Modifier.height(mediumPadding)
            )
            SectionHeader(
                text = Res.string.bank_transfer
            )
            Spacer(
                modifier = Modifier.height(mediumPadding)
            )
            NewPayment(
                isSelected = state.selectedPayment != null && state.selectedPayment is PaymentMethod.BankTransfer,
                modifier = Modifier,
                canSelect = true,
                icon = Res.drawable.send,
                title = Res.string.bank_transfer,
                description = Res.string.transfer_from_bank,
                onClick = {
                    if (numberInputDialogVisible) {
                        numberInputDialogVisible = false
                    }
                    onAction(PaymentScreenAction.SelectPaymentMethod(PaymentMethod.BankTransfer))

                }
            )


        }


        NumberInputDialog(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding(),
            isVisible = numberInputDialogVisible,
            onClick = {
                onAction(PaymentScreenAction.OnCvvEnter(it))
                //action(DepositScreenAction.EditInput(it))
            }

        )

        val isEnable = state.selectedPayment != null

        AnimatedVisibility(
            visible = !numberInputDialogVisible
        ) {
            ActionButton(
                modifier = Modifier
                    .navigationBarsPadding()
                    .padding(bottom = largePadding)
                    .fillMaxWidth(),
                text = stringResource(Res.string.`continue`),
                height = 50.dp,
                isEnable = isEnable,
                shouldEnableClick = true,
                onClick = {
                    //  onForgotPasswordAction(ForgotPasswordAction.SubmitOtp)
                }
            )
        }


    }
}