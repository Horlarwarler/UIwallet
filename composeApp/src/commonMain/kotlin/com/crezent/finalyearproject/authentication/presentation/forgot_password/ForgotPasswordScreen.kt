package com.crezent.finalyearproject.authentication.presentation.forgot_password

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.crezent.finalyearproject.RESET_PASSWORD_PURPOSE
import com.crezent.finalyearproject.app.ScreenNavigation
import com.crezent.finalyearproject.core.presentation.component.CustomInputField
import com.crezent.finalyearproject.authentication.presentation.component.AuthenticationScreenTitle
import com.crezent.finalyearproject.core.domain.util.Animations
import com.crezent.finalyearproject.core.presentation.component.ActionButton
import com.crezent.finalyearproject.core.presentation.component.AnimationDialog
import com.crezent.finalyearproject.core.presentation.component.CustomAppBar
import com.crezent.finalyearproject.core.presentation.util.SnackBarController
import com.crezent.finalyearproject.core.presentation.util.SnackBarEvent
import com.crezent.finalyearproject.core.presentation.util.observeFlowAsEvent
import com.crezent.finalyearproject.domain.util.toErrorMessage
import com.crezent.finalyearproject.ui.theme.NunitoFontFamily
import com.crezent.finalyearproject.ui.theme.background
import com.crezent.finalyearproject.ui.theme.largePadding
import com.crezent.finalyearproject.ui.theme.mediumPadding
import com.crezent.finalyearproject.ui.theme.smallPadding
import finalyearproject.composeapp.generated.resources.Res
import finalyearproject.composeapp.generated.resources.email_id
import finalyearproject.composeapp.generated.resources.forgot_password
import finalyearproject.composeapp.generated.resources.forgot_password_description
import finalyearproject.composeapp.generated.resources.request_otp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel


@OptIn(ExperimentalResourceApi::class)
@Composable
fun ForgotPasswordScreenRoot(
    screenNavigation: ScreenNavigation,

    ) {

    val viewModel: ForgotPasswordViewModel = koinViewModel()
    val state = viewModel.forgotPasswordScreenState.collectAsStateWithLifecycle().value


    var mailSentAnimation by remember {
        mutableStateOf<String?>(null)
    }

    val keyboardController = LocalSoftwareKeyboardController.current


    var loadingAnimation by remember {
        mutableStateOf<String?>(null)
    }
    val scope = rememberCoroutineScope()
    LaunchedEffect(state.isLoading) {
        if (state.isLoading && loadingAnimation == null) {
            loadingAnimation = Res.readBytes(Animations.LOADING_CIRCLE).decodeToString()
        }
    }

    observeFlowAsEvent(
        flow = viewModel.channel
    ) { event ->
        when (event) {
            ForgotPasswordEvent.OtpRequested -> {
                scope.launch {
                    mailSentAnimation = Res.readBytes(Animations.MAIL_SENT).decodeToString()
                }

            }

            is ForgotPasswordEvent.SendMessage -> {
                scope.launch {
                    SnackBarController.sendEvent(
                        snackBarEvent = SnackBarEvent.ShowSnackBar(
                            message = event.networkError.toErrorMessage()
                        )
                    )
                }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        ForgotPasswordScreen(
            modifier = Modifier
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            keyboardController?.hide()
                        }
                    )
                },
            forgotPasswordScreenState = state,
            onForgotPasswordAction = viewModel::handleUserAction,
            navigateBack = screenNavigation.navigateBack
        )
        if (mailSentAnimation != null && !state.isLoading) {
            AnimationDialog(
                modifier = Modifier,
                isPlaying = true,
                onAnimationCompleted = {
                    scope.launch {
                        delay(1500)
                        screenNavigation.navigateToOtpScreen(state.email, RESET_PASSWORD_PURPOSE)

                    }
                },
                animationJson = mailSentAnimation!!,
                iterations = 1,
                closeDialog = {
                }
            )
        }
        if (state.isLoading && loadingAnimation != null) {
            AnimationDialog(
                modifier = Modifier.fillMaxSize(),
                onAnimationCompleted = {

                },
                isPlaying = true,
                iterations = Int.MAX_VALUE,
                closeDialog = {

                },
                animationJson = loadingAnimation!!
            )
        }

    }

}

@Composable
fun ForgotPasswordScreen(
    modifier: Modifier = Modifier,
    forgotPasswordScreenState: ForgotPasswordScreenState = ForgotPasswordScreenState(),
    onForgotPasswordAction: (ForgotPasswordAction) -> Unit = {},
    navigateBack: () -> Unit = {},
) {

    val localFocusManager = LocalFocusManager.current
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
                painter = painterResource(Res.drawable.forgot_password),
                contentDescription = "Login",
                contentScale = ContentScale.Fit
            )

            Spacer(
                modifier = Modifier.height(largePadding)
            )

            AuthenticationScreenTitle(title = stringResource(Res.string.forgot_password))
            Spacer(
                modifier = Modifier.height(largePadding)
            )

            Text(
                text = stringResource(Res.string.forgot_password_description),
                textAlign = TextAlign.Left,
                style = TextStyle(
                    lineHeight = 30.sp,
                    fontFamily = NunitoFontFamily(),
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp
                )
            )
            Spacer(
                modifier = Modifier.height(largePadding)
            )
            CustomInputField(
                leadingIcon = "@",
                modifier = Modifier,
                enable = true,
                placeHolder = stringResource(Res.string.email_id),
                value = forgotPasswordScreenState.email,
                onValueChange = {
                    onForgotPasswordAction(ForgotPasswordAction.EditEmail(it))
                },
                singleLine = true,
                imeAction = ImeAction.Next,
                errorMessage = forgotPasswordScreenState.emailFieldError,
                keyboardType = KeyboardType.Email,
                keyboardActions = KeyboardActions(
                    onNext = {
                        localFocusManager.moveFocus(FocusDirection.Down)
                    }
                )
            )
            Spacer(
                modifier = Modifier.height(largePadding)
            )
        }

        //Sign In Button

        Spacer(
            modifier = Modifier.height(largePadding)
        )
        val isEnable =
            forgotPasswordScreenState.emailFieldError.isEmpty() && forgotPasswordScreenState.email.isNotBlank()

        ActionButton(
            modifier = Modifier
                .navigationBarsPadding()
                .padding(bottom = largePadding)
                .fillMaxWidth(),
            text = stringResource(Res.string.request_otp),
            height = 50.dp,
            isEnable = isEnable,
            shouldEnableClick = true,
            onClick = {
                onForgotPasswordAction(ForgotPasswordAction.SubmitOtp)
            }
        )


    }
}