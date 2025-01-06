package com.crezent.finalyearproject.authentication.presentation.forgot_password

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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.crezent.finalyearproject.app.ScreenNavigation
import com.crezent.finalyearproject.authentication.presentation.component.AuthenticationInputField
import com.crezent.finalyearproject.authentication.presentation.component.AuthenticationScreenTitle
import com.crezent.finalyearproject.core.domain.util.Animations
import com.crezent.finalyearproject.core.presentation.component.ActionButton
import com.crezent.finalyearproject.core.presentation.component.AnimationDialog
import com.crezent.finalyearproject.core.presentation.component.CustomAppBar
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
import kotlinx.coroutines.channels.consumeEach
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
    var showSentMessageAnimation by remember {
        mutableStateOf(false)
    }

    var animationJson by remember {
        mutableStateOf<String?>(null)
    }

    LaunchedEffect(Unit) {
        viewModel.channel.consumeEach { event ->

            when (event) {
                ForgotPasswordEvent.OtpRequested -> {
                    animationJson = Res.readBytes(Animations.MAIL_SENT).decodeToString()
                    showSentMessageAnimation = true

                }

                is ForgotPasswordEvent.SendMessage -> Unit
            }
        }
    }

    if (showSentMessageAnimation && animationJson != null) {
        AnimationDialog(
            modifier = Modifier,
            isPlaying = true,
            onAnimationCompleted = {
                showSentMessageAnimation = false
                screenNavigation.navigateToOtpScreen(state.email)
            },
            animationJson = animationJson!!,
            iterations = 2,
            closeDialog = {
                showSentMessageAnimation = false
            }
        )
    }

    ForgotPasswordScreen(
        forgotPasswordScreenState = state,
        onForgotPasswordAction = viewModel::handleUserAction,
        navigateBack = screenNavigation.navigateBack
    )

}

@Composable
fun ForgotPasswordScreen(
    forgotPasswordScreenState: ForgotPasswordScreenState = ForgotPasswordScreenState(),
    onForgotPasswordAction: (ForgotPasswordAction) -> Unit = {},
    navigateBack: () -> Unit = {},
) {

    val localFocusManager = LocalFocusManager.current
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
            AuthenticationInputField(
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
                onForgotPasswordAction(ForgotPasswordAction.RequestOtp)
            }
        )


    }
}