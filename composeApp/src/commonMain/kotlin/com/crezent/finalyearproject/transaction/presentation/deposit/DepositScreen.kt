package com.crezent.finalyearproject.transaction.presentation.deposit

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
import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.crezent.finalyearproject.app.ScreenNavigation
import com.crezent.finalyearproject.core.presentation.component.ActionButton
import com.crezent.finalyearproject.core.presentation.component.CustomAppBar
import com.crezent.finalyearproject.core.presentation.component.NumberInputDialog
import com.crezent.finalyearproject.core.presentation.component.TopBarLoadingIndicator
import com.crezent.finalyearproject.core.presentation.util.SnackBarController
import com.crezent.finalyearproject.core.presentation.util.SnackBarEvent.ShowSnackBar
import com.crezent.finalyearproject.core.presentation.util.observeFlowAsEvent
import com.crezent.finalyearproject.domain.util.toErrorMessage
import com.crezent.finalyearproject.transaction.presentation.deposit.component.AmountInputField
import com.crezent.finalyearproject.transaction.presentation.payment_method.PaymentMethodEvent
import com.crezent.finalyearproject.ui.theme.background
import com.crezent.finalyearproject.ui.theme.largePadding
import com.crezent.finalyearproject.ui.theme.mediumPadding
import finalyearproject.composeapp.generated.resources.Res
import finalyearproject.composeapp.generated.resources.deposit
import finalyearproject.composeapp.generated.resources.next
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable

fun DepositScreenRoot(
    screenNavigation: ScreenNavigation
) {
    val viewModel: DepositScreenViewmodel = koinViewModel()
    val state = viewModel.depositScreenState.collectAsStateWithLifecycle().value
    val scope = rememberCoroutineScope()

    observeFlowAsEvent(
        flow = viewModel.depositEventChannel, onEvent = { event ->
            when (event) {
                // Show dialog
                //Show snackbar
                /// Navigate to sign home page

                is DepositScreenEvent.NavigateToPayStack -> {
                    screenNavigation.navigateToPaymentMethod(
                        event.authorizationUrl,
                        event.reference
                    )
                }

                is DepositScreenEvent.ShowError -> {
                    scope.launch {
                        SnackBarController.sendEvent(
                            snackBarEvent = ShowSnackBar(
                                message = event.error,
                                duration = SnackbarDuration.Long

                            )
                        )
                    }


                }
            }
        }
    )
    DepositScreen(
        depositScreenState = state,
        action = viewModel::handleScreenAction,
        navigateBack = screenNavigation.navigateBack,

        )
}

@Composable
fun DepositScreen(
    depositScreenState: DepositScreenState = DepositScreenState(),
    action: (DepositScreenAction) -> Unit,
    navigateBack: () -> Unit,
) {
    var numberInputDialogVisible by remember {
        mutableStateOf(true)
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
        TopBarLoadingIndicator(isLoading = depositScreenState.isLoading)
        CustomAppBar(
            modifier = Modifier
                .padding(top = mediumPadding, start = mediumPadding)
                .fillMaxWidth(),
            onNavigationIconClick = navigateBack,
            topText = stringResource(Res.string.deposit)

        )
        Column(
            modifier = Modifier
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

            AmountInputField(
                currentIndex = depositScreenState.currentIndex,
                amount = depositScreenState.amount?.toString(),
                modifier = Modifier,
                onItemClick = { index ->
                    if (index == null) {
                        numberInputDialogVisible = true
                    } else {
                        if (!numberInputDialogVisible) {
                            numberInputDialogVisible = true
                        }
                        action(DepositScreenAction.EditCurrentIndex(index))
                    }
                }
            )
        }


        NumberInputDialog(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding(),
            isVisible = numberInputDialogVisible,
            onClick = {
                action(DepositScreenAction.EditInput(it))
            }

        )

        val isEnable by remember(depositScreenState.amount) {
            mutableStateOf(!depositScreenState.amount.isNullOrBlank() && !depositScreenState.isLoading)
        }

        AnimatedVisibility(
            visible = !numberInputDialogVisible
        ) {
            ActionButton(
                modifier = Modifier
                    .navigationBarsPadding()
                    .padding(bottom = largePadding)
                    .fillMaxWidth(),
                text = stringResource(Res.string.next),
                height = 50.dp,
                isEnable = isEnable,
                shouldEnableClick = true,
                onClick = {
                    action(DepositScreenAction.OpenPaymentPage)
                }
            )
        }


    }
}