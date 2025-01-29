package com.crezent.finalyearproject.transaction.presentation.new_credit_card

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.crezent.finalyearproject.app.ScreenNavigation
import com.crezent.finalyearproject.core.presentation.component.ActionButton
import com.crezent.finalyearproject.core.presentation.component.CustomAppBar
import com.crezent.finalyearproject.core.presentation.component.CustomInputField
import com.crezent.finalyearproject.core.presentation.component.NumberInputDialog
import com.crezent.finalyearproject.core.presentation.component.TopBarLoadingIndicator
import com.crezent.finalyearproject.core.presentation.util.SnackBarController
import com.crezent.finalyearproject.core.presentation.util.SnackBarEvent
import com.crezent.finalyearproject.core.presentation.util.observeFlowAsEvent
import com.crezent.finalyearproject.domain.util.toErrorMessage
import com.crezent.finalyearproject.transaction.presentation.new_credit_card.composable.CreditCard
import com.crezent.finalyearproject.ui.theme.background
import com.crezent.finalyearproject.ui.theme.largePadding
import com.crezent.finalyearproject.ui.theme.mediumPadding
import finalyearproject.composeapp.generated.resources.Res
import finalyearproject.composeapp.generated.resources.add_credit_card
import finalyearproject.composeapp.generated.resources.card_number
import finalyearproject.composeapp.generated.resources.card_place_holder
import finalyearproject.composeapp.generated.resources.cvv
import finalyearproject.composeapp.generated.resources.cvv_place_holder
import finalyearproject.composeapp.generated.resources.expiration_date
import finalyearproject.composeapp.generated.resources.expiration_place_holder
import finalyearproject.composeapp.generated.resources.holder_name
import finalyearproject.composeapp.generated.resources.name_place_holder
import finalyearproject.composeapp.generated.resources.save
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun NewCreditCardScreenRoot(
    screenNavigation: ScreenNavigation
) {

    val viewmodel: NewCreditCardViewmodel = koinViewModel()
    val state = viewmodel.state.collectAsStateWithLifecycle().value
    val scope = rememberCoroutineScope()

    observeFlowAsEvent(
        flow = viewmodel.newCreditChannelEvent
    ) { event ->
        when (event) {
            is NewCreditCardEvent.CardCreationError -> {
                scope.launch {
                    SnackBarController.sendEvent(
                        snackBarEvent = SnackBarEvent.ShowSnackBar(
                            message = event.error.toErrorMessage(),
                        )
                    )
                }

            }

            NewCreditCardEvent.CardCreationSuccessful -> {
                scope.launch {
                    SnackBarController.sendEvent(
                        snackBarEvent = SnackBarEvent.ShowSnackBar(
                            message = "Card Successfully Added",
                        )
                    )
                }
                screenNavigation.navigateBack()
            }
        }
    }
    NewCreditCardScreen(
        navigateBack = screenNavigation.navigateBack,
        state = state,
        onAction = viewmodel::handleScreenAction
    )
}


@Composable
fun NewCreditCardScreen(
    navigateBack: () -> Unit,
    state: NewCreditCardScreenState,
    onAction: (NewCreditCardScreenAction) -> Unit = {},
) {

    var numberInputDialogVisible by remember {
        mutableStateOf(false)
    }
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .background(background)
            .fillMaxSize()
            .navigationBarsPadding()
            .systemBarsPadding()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        focusManager.clearFocus()
                    }
                )
            }

    ) {
        ///Scrollable Part


        val scrollState = rememberScrollState()
        var hideCreditCardNumber by remember(true) {
            mutableStateOf(true)
        }
        var hideCvvNumber by remember(true) {
            mutableStateOf(true)
        }

        TopBarLoadingIndicator(
            isLoading = state.isLoading
        )
        CustomAppBar(
            modifier = Modifier
                .padding(top = mediumPadding, start = mediumPadding)
                .fillMaxWidth(),
            onNavigationIconClick = navigateBack,
            topText = stringResource(Res.string.add_credit_card)

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
            Spacer(
                modifier = Modifier.height(50.dp)
            )
            CreditCard(
                creditCardNumber = state.cardNumber,
                expirationDate = state.expirationDate,
                cvv = state.cvv,
                hideCreditCard = hideCreditCardNumber,
                hideCvv = hideCvvNumber
            )
            Spacer(modifier = Modifier.height(largePadding))

            CustomInputField(
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = null,
                enable = true,
                title = Res.string.holder_name,
                placeHolder = stringResource(Res.string.name_place_holder),
                value = state.holderName ?: "",
                onValueChange = {
                    onAction(NewCreditCardScreenAction.OnHolderNameChange(it))
                },
                singleLine = true,
                imeAction = ImeAction.Next,
                errorMessage = state.cardHolderNameError,
                keyboardType = KeyboardType.Text,
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                )
            )

            Spacer(modifier = Modifier.height(mediumPadding))

            CustomInputField(
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = null,
                enable = true,
                title = Res.string.card_number,
                placeHolder = stringResource(Res.string.card_place_holder),
                value = state.cardNumber ?: "",
                onValueChange = {
                    onAction(NewCreditCardScreenAction.OnCardNumberChange(it))

                },
                singleLine = true,
                imeAction = ImeAction.Next,
                errorMessage = state.cardHolderNameError,
                keyboardType = KeyboardType.NumberPassword,
                onTransform = {
                    hideCreditCardNumber = it
                },
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                )
            )
            Spacer(modifier = Modifier.height(mediumPadding))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically

            ) {


                CustomInputField(
                    modifier = Modifier.weight(1f),
                    leadingIcon = null,
                    enable = true,
                    title = Res.string.expiration_date,
                    placeHolder = stringResource(Res.string.expiration_place_holder),
                    value = state.expirationDate ?: "",
                    onValueChange = {
                        onAction(NewCreditCardScreenAction.OnExpirationDateChange(it))

                    },
                    singleLine = true,
                    imeAction = ImeAction.Next,
                    errorMessage = state.expirationDateError,
                    keyboardType = KeyboardType.Number,
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Right)
                        }
                    )
                )
                CustomInputField(
                    modifier = Modifier.weight(1f),
                    leadingIcon = null,
                    enable = true,
                    title = Res.string.cvv,
                    placeHolder = stringResource(Res.string.cvv_place_holder),
                    value = state.cvv ?: "",
                    onValueChange = {
                        onAction(NewCreditCardScreenAction.Cvv(it))

                    },
                    singleLine = true,
                    imeAction = ImeAction.Next,
                    errorMessage = state.cvvError,
                    keyboardType = KeyboardType.NumberPassword,
                    onTransform = {
                        hideCvvNumber = it
                    },
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.clearFocus()
                        }
                    )
                )
            }


        }


        NumberInputDialog(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding(),
            isVisible = numberInputDialogVisible,
            onClick = {
                //  onAction(PaymentScreenAction.OnCvvEnter(it))
                //action(DepositScreenAction.EditInput(it))
            }

        )

        val isEnable = true

        AnimatedVisibility(
            visible = !numberInputDialogVisible
        ) {
            ActionButton(
                modifier = Modifier
                    .navigationBarsPadding()
                    .padding(bottom = largePadding)
                    .fillMaxWidth(),
                text = stringResource(Res.string.save),
                height = 50.dp,
                isEnable = isEnable,
                shouldEnableClick = true,
                onClick = {
                    onAction(NewCreditCardScreenAction.Save)
                    //  onForgotPasswordAction(ForgotPasswordAction.SubmitOtp)
                }
            )
        }


    }
}