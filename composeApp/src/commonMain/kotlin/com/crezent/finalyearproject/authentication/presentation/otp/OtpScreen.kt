package com.crezent.finalyearproject.authentication.presentation.otp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
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
import com.crezent.finalyearproject.app.Route
import com.crezent.finalyearproject.app.ScreenNavigation
import com.crezent.finalyearproject.authentication.presentation.component.AuthenticationScreenTitle
import com.crezent.finalyearproject.authentication.presentation.component.TextButton
import com.crezent.finalyearproject.core.domain.util.Animations
import com.crezent.finalyearproject.core.presentation.component.ActionButton
import com.crezent.finalyearproject.core.presentation.component.AnimationDialog
import com.crezent.finalyearproject.core.presentation.component.CustomAppBar
import com.crezent.finalyearproject.core.presentation.component.OtpInput
import com.crezent.finalyearproject.core.presentation.util.SnackBarController
import com.crezent.finalyearproject.core.presentation.util.SnackBarEvent
import com.crezent.finalyearproject.core.presentation.util.observeFlowAsEvent
import com.crezent.finalyearproject.domain.util.toErrorMessage
import com.crezent.finalyearproject.ui.theme.NunitoFontFamily
import com.crezent.finalyearproject.ui.theme.background
import com.crezent.finalyearproject.ui.theme.black
import com.crezent.finalyearproject.ui.theme.grey
import com.crezent.finalyearproject.ui.theme.largePadding
import com.crezent.finalyearproject.ui.theme.mediumPadding
import com.crezent.finalyearproject.ui.theme.smallPadding
import finalyearproject.composeapp.generated.resources.Res
import finalyearproject.composeapp.generated.resources.enter_otp
import finalyearproject.composeapp.generated.resources.otp
import finalyearproject.composeapp.generated.resources.otp_description
import finalyearproject.composeapp.generated.resources.request_new_button
import finalyearproject.composeapp.generated.resources.request_otp
import finalyearproject.composeapp.generated.resources.submit
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel


@OptIn(ExperimentalResourceApi::class)
@Composable
fun OtpScreenRoot(
    screenNavigation: ScreenNavigation,

    ) {

    val viewModel: OtpViewModel = koinViewModel()
    val state = viewModel.otpScreenState.collectAsStateWithLifecycle().value
    val keyboardController = LocalSoftwareKeyboardController.current
    val scope = rememberCoroutineScope()

    val previousRoute = screenNavigation.navController.previousBackStackEntry?.destination


    var mailSentAnimation by remember {
        mutableStateOf<String?>(null)
    }

    var showMailSentAnimation by remember {
        mutableStateOf(false)
    }

    observeFlowAsEvent(flow = viewModel.channel) { event ->
        when (event) {
            is OtpScreenEvent.OtpScreenError -> {
                scope.launch {
                    SnackBarController.sendEvent(
                        snackBarEvent = SnackBarEvent.ShowSnackBar(
                            message = event.error.toErrorMessage(),
                        )
                    )
                }
            }


            OtpScreenEvent.OtpScreenSuccessful -> {


            }

            OtpScreenEvent.OtpSentSuccessful -> {
                scope.launch {
                    showMailSentAnimation = true
                    if (mailSentAnimation == null) {
                        mailSentAnimation = Res.readBytes(Animations.MAIL_SENT).decodeToString()
                    }
                }

            }

            OtpScreenEvent.NavigateToHome -> {
                screenNavigation.navigateToHome()
                println("Sign in")

            }

            OtpScreenEvent.NavigateToResetPassword -> {
                screenNavigation.navigateToResetPassword()
            }
        }
    }

    var loadingAnimationJson by remember {
        mutableStateOf<String?>(null)
    }

    LaunchedEffect(state.isLoading) {
        if (state.isLoading && loadingAnimationJson == null) {
            loadingAnimationJson = Res.readBytes(Animations.LOADING_CIRCLE).decodeToString()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        OtpScreen(
            modifier = Modifier
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            keyboardController?.hide()
                        }
                    )
                },
            otpScreenState = state,
            otpAction = viewModel::handleUserAction,
            navigateBack = screenNavigation.navigateBack
        )
        if (mailSentAnimation != null && !state.isLoading) {
            AnimationDialog(
                modifier = Modifier,
                isPlaying = true,
                onAnimationCompleted = {
                    showMailSentAnimation = false
                },
                animationJson = mailSentAnimation!!,
                iterations = 1,
                closeDialog = {
                }
            )
        }

        if (state.isLoading && loadingAnimationJson != null) {
            AnimationDialog(
                modifier = Modifier.fillMaxSize(),
                onAnimationCompleted = {

                },
                isPlaying = true,
                iterations = Int.MAX_VALUE,
                closeDialog = {

                },
                animationJson = loadingAnimationJson!!
            )
        }

    }


}

@Composable
fun OtpScreen(
    modifier: Modifier = Modifier,
    otpScreenState: OtpScreenState = OtpScreenState(),
    otpAction: (OtpScreenAction) -> Unit = {},
    navigateBack: () -> Unit = {},
) {


    Column(
        modifier = modifier
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
                contentDescription = "Otp",
                contentScale = ContentScale.Fit
            )

            Spacer(
                modifier = Modifier.height(largePadding)
            )

            AuthenticationScreenTitle(title = stringResource(Res.string.enter_otp))
            Spacer(
                modifier = Modifier.height(mediumPadding)
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
                    append(otpScreenState.emailAddress)
                }

                // append(forgotPasswordDescription)
            }
            Text(
                text = annotatedString,
                textAlign = TextAlign.Left,
                lineHeight = 30.sp,
            )

            Spacer(
                modifier = Modifier.height(mediumPadding)
            )

            OtpInput(
                modifier = Modifier.fillMaxWidth(),
                otp = otpScreenState.otp,
                onInputChange = {
                    otpAction(OtpScreenAction.EditOtp(it))
                },
                isError = otpScreenState.wrongOtp
            )

            Spacer(
                modifier = Modifier.height(smallPadding)
            )
            Row(
                modifier = Modifier

                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val requestNew = stringResource(Res.string.request_new_button)

                val requestNewDescription = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontFamily = NunitoFontFamily(),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = grey
                        )
                    ) {
                        append(requestNew)
                    }
                    withStyle(
                        style = SpanStyle(
                            fontFamily = NunitoFontFamily(),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = grey,
                            fontStyle = FontStyle.Italic
                        )
                    ) {
                        append(otpScreenState.remainingTime)
                    }
                }

                Text(
                    modifier = Modifier.padding(end = 4.dp),
                    text = requestNewDescription,
                    textAlign = TextAlign.Start,
                )
                Spacer(Modifier.width(5.dp))
                TextButton(
                    modifier = Modifier,
                    text = stringResource(Res.string.request_otp),
                    onClick = {
                        otpAction(OtpScreenAction.RequestNewOtp)
                    },
                    fontSize = 16.sp,
                    enable = otpScreenState.requestNewButtonEnable
                )
            }
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
            text = stringResource(Res.string.submit),
            height = 50.dp,
            isEnable = otpScreenState.otp.length == 6,
            shouldEnableClick = otpScreenState.otp.length == 6,
            onClick = {
                otpAction(OtpScreenAction.Submit)
            }
        )


    }
}