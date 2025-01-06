package com.crezent.finalyearproject.authentication.presentation.otp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.crezent.finalyearproject.app.ScreenNavigation
import com.crezent.finalyearproject.authentication.presentation.component.AuthenticationScreenTitle
import com.crezent.finalyearproject.core.presentation.component.ActionButton
import com.crezent.finalyearproject.core.presentation.component.CustomAppBar
import com.crezent.finalyearproject.core.presentation.component.OtpInput
import com.crezent.finalyearproject.ui.theme.NunitoFontFamily
import com.crezent.finalyearproject.ui.theme.background
import com.crezent.finalyearproject.ui.theme.black
import com.crezent.finalyearproject.ui.theme.largePadding
import com.crezent.finalyearproject.ui.theme.mediumPadding
import com.crezent.finalyearproject.ui.theme.smallPadding
import finalyearproject.composeapp.generated.resources.Res
import finalyearproject.composeapp.generated.resources.enter_otp
import finalyearproject.composeapp.generated.resources.otp
import finalyearproject.composeapp.generated.resources.otp_description
import finalyearproject.composeapp.generated.resources.request_otp
import kotlinx.coroutines.channels.consumeEach
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun OtpScreenRoot(
    screenNavigation: ScreenNavigation,

    ) {

    val viewModel: OtpViewModel = koinViewModel()
    val state = viewModel.otpScreenState.collectAsStateWithLifecycle().value
    val keyboardController = LocalSoftwareKeyboardController.current
    LaunchedEffect(state.otp){
        if (state.otp.length == 6){
            keyboardController?.hide()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.channel.consumeEach { event ->

            when (event) {
                is OtpScreenEvent.OtpScreenError -> {
                }


                OtpScreenEvent.OtpScreenSuccessful -> {
                    screenNavigation.navigateToResetPassword()
                }
            }
        }
    }

    OtpScreen(
        otpScreenState = state,
        otpAction = viewModel::handleUserAction,
        navigateBack = screenNavigation.navigateBack
    )
}

@Composable
fun OtpScreen(
    otpScreenState: OtpScreenState = OtpScreenState(),
    otpAction: (OtpScreenAction) -> Unit = {},
    navigateBack: () -> Unit = {},
) {


    Column(
        modifier = Modifier
            .background(background)
            .fillMaxSize()
            .navigationBarsPadding()
            .systemBarsPadding()
            .padding(horizontal = smallPadding)
    ) {
        ///Scrollable Part

        val scrollState = rememberScrollState()

        CustomAppBar(
            modifier = Modifier
                .padding(top = mediumPadding, start = mediumPadding)
                .fillMaxWidth(),
            onNavigationIconClick = navigateBack,

            )


        Column(
            modifier = Modifier
                .weight(1f)
                .padding(top = largePadding)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
        )
        {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(313.dp),
                painter = painterResource(Res.drawable.otp),
                contentDescription = "Login",
                contentScale = ContentScale.Fit
            )

            Spacer(
                modifier = Modifier.height(largePadding)
            )

            AuthenticationScreenTitle(title = stringResource(Res.string.enter_otp))
            Spacer(
                modifier = Modifier.height(largePadding)
            )
            val forgotPasswordDescription = stringResource(Res.string.otp_description)

            val annotatedString = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontFamily = NunitoFontFamily(),
                        fontWeight = FontWeight.Medium,
                        fontSize = 18.sp,
                        color = black
                    )
                ) {
                    append(forgotPasswordDescription)
                }
                append(" ")
                withStyle(
                    style = SpanStyle(
                        fontFamily = NunitoFontFamily(),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp,
                        color = black,
                        fontStyle = FontStyle.Italic

                    )
                ) {
                    append(otpScreenState.recoveredEmail)
                }

                // append(forgotPasswordDescription)
            }
            Text(
                text = annotatedString,
                textAlign = TextAlign.Left,
                lineHeight = 30.sp,
            )


            Spacer(
                modifier = Modifier.height(largePadding)
            )

            OtpInput(
                modifier = Modifier.fillMaxWidth(),
                otp = otpScreenState.otp,
                onInputChange = {
                    otpAction(OtpScreenAction.EditOtp(it))
                },
                isError = otpScreenState.wrongOtp
            )
        }

        //Sign In Button

        Spacer(
            modifier = Modifier.height(largePadding)
        )

        ActionButton(
            modifier = Modifier
                .navigationBarsPadding()
                .padding(bottom = largePadding)
                .fillMaxWidth(),
            text = stringResource(Res.string.request_otp),
            height = 50.dp,
            isEnable = otpScreenState.otp.length == 6,
            shouldEnableClick = true,
            onClick = {
                otpAction(OtpScreenAction.Submit)
            }
        )


    }
}