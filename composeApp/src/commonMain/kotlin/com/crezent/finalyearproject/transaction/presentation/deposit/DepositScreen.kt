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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.crezent.finalyearproject.app.ScreenNavigation
import com.crezent.finalyearproject.core.presentation.component.ActionButton
import com.crezent.finalyearproject.core.presentation.component.CustomAppBar
import com.crezent.finalyearproject.core.presentation.component.NumberInputDialog
import com.crezent.finalyearproject.transaction.presentation.deposit.component.AmountInputField
import com.crezent.finalyearproject.ui.theme.background
import com.crezent.finalyearproject.ui.theme.largePadding
import com.crezent.finalyearproject.ui.theme.mediumPadding
import finalyearproject.composeapp.generated.resources.Res
import finalyearproject.composeapp.generated.resources.deposit
import finalyearproject.composeapp.generated.resources.next
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable

fun DepositScreenRoot(
    screenNavigation: ScreenNavigation
) {
    val viewmodel: DepositScreenViewmodel = koinViewModel()
    val state = viewmodel.depositScreenState.collectAsStateWithLifecycle().value
    DepositScreen(
        depositScreenState = state,
        action = viewmodel::handleScreenAction,
        navigateBack = screenNavigation.navigateBack,
        navigateToPaymentMethod = {
            screenNavigation.navigateToPaymentMethod(state.depositAmount!!)
        }
    )
}

@Composable
fun DepositScreen(
    depositScreenState: DepositScreenState = DepositScreenState(),
    action: (DepositScreenAction) -> Unit,
    navigateBack: () -> Unit,
    navigateToPaymentMethod: () -> Unit
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
                amount = depositScreenState.depositAmount?.toString(),
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

        val isEnable by remember(depositScreenState.depositAmount) {
            mutableStateOf(!depositScreenState.depositAmount.isNullOrBlank())
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
                onClick = navigateToPaymentMethod
            )
        }


    }
}