package com.crezent.finalyearproject.home.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.crezent.finalyearproject.VERIFY_EMAIL_PURPOSE
import com.crezent.finalyearproject.app.ScreenNavigation
import com.crezent.finalyearproject.authentication.presentation.signin.SignInEvent
import com.crezent.finalyearproject.core.domain.model.FundingSource
import com.crezent.finalyearproject.core.domain.model.Transaction
import com.crezent.finalyearproject.core.presentation.component.TopBarLoadingIndicator
import com.crezent.finalyearproject.core.presentation.util.SnackBarController
import com.crezent.finalyearproject.core.presentation.util.SnackBarEvent
import com.crezent.finalyearproject.core.presentation.util.observeFlowAsEvent
import com.crezent.finalyearproject.domain.util.toErrorMessage
import com.crezent.finalyearproject.home.presentation.component.AccountHeader
import com.crezent.finalyearproject.home.presentation.component.TransactionCard
import com.crezent.finalyearproject.home.presentation.component.TransactionCards
import com.crezent.finalyearproject.transaction.TransactionStatus
import com.crezent.finalyearproject.transaction.TransactionType
import com.crezent.finalyearproject.ui.theme.NunitoFontFamily
import com.crezent.finalyearproject.ui.theme.background
import com.crezent.finalyearproject.ui.theme.black
import com.crezent.finalyearproject.ui.theme.largePadding
import com.crezent.finalyearproject.ui.theme.mediumPadding
import com.crezent.finalyearproject.ui.theme.smallPadding
import finalyearproject.composeapp.generated.resources.Res
import finalyearproject.composeapp.generated.resources.available_balance
import finalyearproject.composeapp.generated.resources.transfer
import finalyearproject.composeapp.generated.resources.your_activity
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreenRoot(
    navigation: ScreenNavigation
) {

    val homeScreenViewModel: HomeScreenViewModel = koinViewModel()
    val state = homeScreenViewModel.homeScreenState.collectAsStateWithLifecycle().value
    val scope = rememberCoroutineScope()


    observeFlowAsEvent(flow = homeScreenViewModel.homeScreenEvent, onEvent = { event ->
        when (event) {
            is HomeScreenEvent.AuthenticatedUserError -> {
                scope.launch {
                    SnackBarController.sendEvent(
                        snackBarEvent = SnackBarEvent.ShowSnackBar(
                            message = event.error.toErrorMessage(),
                            duration = SnackbarDuration.Long

                        )
                    )

                }
            }
        }
    })


    HomeScreen(
        state = state,
        onProfileClick = navigation.navigateToSetting,
        navigateToDeposit = navigation.navigateToTransactionRoute
    )

}

@Composable
fun HomeScreen(
    state: HomeScreenState = HomeScreenState(),
    onProfileClick: () -> Unit,
    navigateToDeposit: () -> Unit = {},
    navigateToPayment: () -> Unit = {}
) {
    Column(
        modifier = Modifier.background(background)
            .fillMaxSize()
            .navigationBarsPadding()
            .systemBarsPadding()
            .padding(horizontal = mediumPadding, vertical = mediumPadding)
    ) {
        TopBarLoadingIndicator(isLoading = state.isLoading)
        val balance = state.user?.wallet?.accountBalance ?: state.lastBalance ?: 0.0
        AccountHeader(
            availableBalance = balance,
            onProfileClick = onProfileClick
        )
        Spacer(
            modifier = Modifier.height(30.dp)
        )
        Text(
            text = stringResource(Res.string.transfer),
            textAlign = TextAlign.Left,
            style = TextStyle(
                lineHeight = 30.sp,
                fontFamily = NunitoFontFamily(),
                fontWeight = FontWeight.Normal,
                fontSize = 20.sp
            ),
            color = black
        )
        Spacer(modifier = Modifier.height(largePadding))
        TransactionCards(
            navigateToDeposit = navigateToDeposit,
            navigateToPayment = navigateToPayment
        )
        Spacer(modifier = Modifier.height(largePadding))
        Text(
            text = stringResource(Res.string.your_activity),
            textAlign = TextAlign.Left,
            style = TextStyle(
                lineHeight = 30.sp,
                fontFamily = NunitoFontFamily(),
                fontWeight = FontWeight.Normal,
                fontSize = 20.sp
            ),
            color = black
        )

        Spacer(modifier = Modifier.height(smallPadding))

        val transactions = state.user?.wallet?.transactions ?: emptyList()

        LazyColumn {
            items(items = transactions) {
                TransactionCard(
                    transaction = it,
                    onClick = {

                    }
                )
            }
        }


    }
}