package com.crezent.finalyearproject.authentication.presentation.signin

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.input.pointer.pointerInput
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
import com.crezent.finalyearproject.authentication.presentation.component.TextButton
import com.crezent.finalyearproject.core.presentation.component.ActionButton
import com.crezent.finalyearproject.ui.theme.NunitoFontFamily
import com.crezent.finalyearproject.ui.theme.background
import com.crezent.finalyearproject.ui.theme.grey
import com.crezent.finalyearproject.ui.theme.largePadding
import com.crezent.finalyearproject.ui.theme.mediumPadding
import com.crezent.finalyearproject.ui.theme.smallPadding
import finalyearproject.composeapp.generated.resources.Res
import finalyearproject.composeapp.generated.resources.email_id
import finalyearproject.composeapp.generated.resources.forgot_password
import finalyearproject.composeapp.generated.resources.lock
import finalyearproject.composeapp.generated.resources.login
import finalyearproject.composeapp.generated.resources.new_to_ui
import finalyearproject.composeapp.generated.resources.password
import finalyearproject.composeapp.generated.resources.register
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun SignInScreenRoot(
    screenNavigation: ScreenNavigation,

    ) {

    val viewModel: SignInViewModel = koinViewModel()
    val state = viewModel.signInScreenState.collectAsStateWithLifecycle().value
    SignInScreen(
        signInScreenState = state,
        onSignInAction = viewModel::handleUserAction,
        navigateToSignUpScreen = {
            screenNavigation.navigateToSignUp()
        },
        navigateToForgotScreen = screenNavigation.navigateToForgotPassword
    )
}

@Composable
fun SignInScreen(
    signInScreenState: SignInScreenState = SignInScreenState(),
    onSignInAction: (SignInAction) -> Unit = {},
    navigateToSignUpScreen: () -> Unit = {},
    navigateToForgotScreen: () -> Unit = {}
) {

    val localFocusManager = LocalFocusManager.current
    Column(
        modifier = Modifier
            .background(background)
            .fillMaxSize()
            .navigationBarsPadding()
            .padding(horizontal = smallPadding)
            .pointerInput(Unit){
                detectTapGestures(
                    onTap = {
                        localFocusManager.clearFocus()
                    }
                )
            }
    ) {
        ///Scrollable Part

        val scrollState = rememberScrollState()

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
                painter = painterResource(Res.drawable.login),
                contentDescription = "Login",
                contentScale = ContentScale.Fit
            )

            Spacer(
                modifier = Modifier.height(mediumPadding)
            )

            AuthenticationScreenTitle(title = stringResource(Res.string.login))
            Spacer(
                modifier = Modifier.height(mediumPadding)
            )
            AuthenticationInputField(
                leadingIcon = "@",
                modifier = Modifier,
                enable = true,
                placeHolder = stringResource(Res.string.email_id),
                value = signInScreenState.email,
                onValueChange = {
                    onSignInAction(SignInAction.EditEmail(it))
                },
                singleLine = true,
                imeAction = ImeAction.Next,
                errorMessage = signInScreenState.emailFieldError,
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

            AuthenticationInputField(
                modifier = Modifier,
                leadingIcon = Res.drawable.lock,

                enable = true,
                placeHolder = stringResource(Res.string.password),
                value = signInScreenState.password,
                onValueChange = {
                    onSignInAction(SignInAction.EditPassword(it))

                },
                singleLine = true,
                imeAction = ImeAction.Next,
                errorMessage = signInScreenState.passwordFieldError,
                keyboardType = KeyboardType.Password,
                keyboardActions = KeyboardActions(
                    onNext = {
                        localFocusManager.clearFocus()
                    }
                )
            )

            Spacer(
                modifier = Modifier.height(mediumPadding)
            )

            TextButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(Res.string.forgot_password),
                onClick = navigateToForgotScreen,
                textAlign = TextAlign.End
            )


        }


        //Sign In Button

        Spacer(
            modifier = Modifier.height(largePadding)
        )
        val isEnable =
            signInScreenState.passwordFieldError.isEmpty() && signInScreenState.emailFieldError.isEmpty() && signInScreenState.email.isNotBlank() && signInScreenState.password.isNotBlank()

        ActionButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(Res.string.login),
            height = 50.dp,
            isEnable = isEnable,
            shouldEnableClick = true,
            onClick = {
                onSignInAction(SignInAction.Login)
            }
        )

        Spacer(
            modifier = Modifier.height(largePadding)
        )
        Row(
            modifier = Modifier
                .navigationBarsPadding()
                .padding(bottom = 50.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(
                modifier = Modifier.padding(end = 4.dp),
                text = stringResource(Res.string.new_to_ui),
                textAlign = TextAlign.Start,
                style = TextStyle(
                    lineHeight = 30.sp,
                    fontFamily = NunitoFontFamily(),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = grey
                )
            )
            TextButton(
                modifier = Modifier,
                text = stringResource(Res.string.register),
                onClick = navigateToSignUpScreen
            )
        }
    }
}