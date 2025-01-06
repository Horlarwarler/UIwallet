package com.crezent.finalyearproject.authentication.presentation.signup

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.crezent.finalyearproject.authentication.presentation.component.GenderField
import com.crezent.finalyearproject.authentication.presentation.component.TextButton
import com.crezent.finalyearproject.core.presentation.component.ActionButton
import com.crezent.finalyearproject.ui.theme.NunitoFontFamily
import com.crezent.finalyearproject.ui.theme.background
import com.crezent.finalyearproject.ui.theme.grey
import com.crezent.finalyearproject.ui.theme.largePadding
import com.crezent.finalyearproject.ui.theme.mediumPadding
import com.crezent.finalyearproject.ui.theme.smallPadding
import finalyearproject.composeapp.generated.resources.Res
import finalyearproject.composeapp.generated.resources.confirm_password
import finalyearproject.composeapp.generated.resources.`continue`
import finalyearproject.composeapp.generated.resources.email_id
import finalyearproject.composeapp.generated.resources.first_name
import finalyearproject.composeapp.generated.resources.joined_before
import finalyearproject.composeapp.generated.resources.lock
import finalyearproject.composeapp.generated.resources.login
import finalyearproject.composeapp.generated.resources.matric
import finalyearproject.composeapp.generated.resources.password
import finalyearproject.composeapp.generated.resources.phone_number
import finalyearproject.composeapp.generated.resources.signup
import finalyearproject.composeapp.generated.resources.vector
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun SignUpScreenRoot(
    screenNavigation: ScreenNavigation,

    ) {

    val viewModel: SignUpViewModel = koinViewModel()
    val state = viewModel.signUpScreenState.collectAsStateWithLifecycle().value
    SignUpScreen(
        signUpScreenState = state,
        onSignUpAction = viewModel::handleUserAction,
        navigateToSignInScreen = {
            screenNavigation.navigateToSignIn()
        },

        )
}

@Composable
fun SignUpScreen(
    signUpScreenState: SignUpScreenState = SignUpScreenState(),
    onSignUpAction: (SignUpAction) -> Unit = {},
    navigateToSignInScreen: () -> Unit = {},
) {

    val localFocusManager = LocalFocusManager.current

    val continueIsEnable by remember(signUpScreenState) {
        val emailIsValid =
            signUpScreenState.email.isNotBlank() && signUpScreenState.emailFieldError.isEmpty()
        val fullNameIsValid =
            signUpScreenState.fullName.isNotBlank() && signUpScreenState.fullNameError.isEmpty()

        val passwordIsValid =
            signUpScreenState.password.isNotBlank() && signUpScreenState.passwordFieldError.isEmpty()

        val confirmPasswordIsValid =
            signUpScreenState.confirmPassword.isNotBlank() && signUpScreenState.confirmPasswordFieldError.isEmpty()
        val phoneNumberIsValid =
            signUpScreenState.phoneNumber.isNotBlank() && signUpScreenState.phoneNumberFieldError.isEmpty()
        val matricNumberIsValid =
            signUpScreenState.matricNumber.isNotBlank() && signUpScreenState.matricNumberFieldError.isEmpty()
        mutableStateOf(emailIsValid && fullNameIsValid && passwordIsValid && confirmPasswordIsValid && phoneNumberIsValid && matricNumberIsValid)
    }


    Column(
        modifier = Modifier
            .background(background)
            .fillMaxSize()
            .navigationBarsPadding()
            .padding(horizontal = smallPadding)
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
                painter = painterResource(Res.drawable.signup),
                contentDescription = "Sign Up",
                contentScale = ContentScale.Fit
            )

            Spacer(
                modifier = Modifier.height(mediumPadding)
            )

            AuthenticationScreenTitle(title = stringResource(Res.string.signup))
            Spacer(
                modifier = Modifier.height(mediumPadding)
            )
            AuthenticationInputField(
                leadingIcon = "@",
                modifier = Modifier,
                enable = true,
                placeHolder = stringResource(Res.string.email_id),
                value = signUpScreenState.email,
                onValueChange = {
                    onSignUpAction(SignUpAction.EditEmail(it))
                },
                singleLine = true,
                imeAction = ImeAction.Next,
                errorMessage = signUpScreenState.emailFieldError,
                keyboardType = KeyboardType.Email,
                keyboardActions = KeyboardActions(
                    onNext = {
                        localFocusManager.moveFocus(FocusDirection.Down)
                    }
                )
            )

            Spacer(
                modifier = Modifier.height(mediumPadding)
            )



            AuthenticationInputField(
                leadingIcon = Res.drawable.vector,
                modifier = Modifier,
                enable = true,
                placeHolder = stringResource(Res.string.first_name),
                value = signUpScreenState.fullName,
                onValueChange = {
                    onSignUpAction(SignUpAction.EditFullName(it))
                },
                singleLine = true,
                imeAction = ImeAction.Next,
                errorMessage = signUpScreenState.fullNameError,
                keyboardType = KeyboardType.Text,
                keyboardActions = KeyboardActions(
                    onNext = {
                        localFocusManager.moveFocus(FocusDirection.Right)
                    }
                )
            )

            GenderField(
                modifier = Modifier
                    .fillMaxWidth(),
                selected = signUpScreenState.gender,
                onGenderSelect = {
                    onSignUpAction(SignUpAction.EditGender(it))
                }
            )


            Spacer(
                modifier = Modifier.height(mediumPadding)
            )
            AuthenticationInputField(
                modifier = Modifier.imePadding(),
                leadingIcon = Res.drawable.lock,

                enable = true,
                placeHolder = stringResource(Res.string.password),
                value = signUpScreenState.password,
                onValueChange = {
                    onSignUpAction(SignUpAction.EditPassword(it))

                },
                singleLine = true,
                imeAction = ImeAction.Next,
                errorMessage = signUpScreenState.passwordFieldError,
                keyboardType = KeyboardType.Password,
                keyboardActions = KeyboardActions(
                    onNext = {
                        localFocusManager.moveFocus(FocusDirection.Down)

                    }
                )
            )

            Spacer(
                modifier = Modifier.height(mediumPadding)
            )
            AuthenticationInputField(
                modifier = Modifier.imePadding(),
                leadingIcon = Res.drawable.lock,

                enable = true,
                placeHolder = stringResource(Res.string.confirm_password),
                value = signUpScreenState.confirmPassword,
                onValueChange = {
                    onSignUpAction(SignUpAction.EditConfirmPassword(it))

                },
                singleLine = true,
                imeAction = ImeAction.Next,
                errorMessage = signUpScreenState.confirmPasswordFieldError,
                keyboardType = KeyboardType.Password,
                keyboardActions = KeyboardActions(
                    onNext = {
                        localFocusManager.moveFocus(FocusDirection.Down)
                    }
                )
            )

            Spacer(
                modifier = Modifier.height(mediumPadding)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                AuthenticationInputField(
                    leadingIcon = Res.drawable.phone_number,
                    modifier = Modifier.weight(1f),
                    enable = true,
                    placeHolder = stringResource(Res.string.phone_number),
                    value = signUpScreenState.phoneNumber,
                    onValueChange = {
                        onSignUpAction(SignUpAction.EditPhoneNumber(it))
                    },
                    singleLine = true,
                    imeAction = ImeAction.Next,
                    errorMessage = signUpScreenState.phoneNumberFieldError,
                    keyboardType = KeyboardType.Phone,
                    keyboardActions = KeyboardActions(
                        onNext = {
                            localFocusManager.moveFocus(FocusDirection.Down)
                        }
                    )
                )

                AuthenticationInputField(
                    leadingIcon = Res.drawable.vector,
                    modifier = Modifier.weight(1f),
                    enable = true,
                    placeHolder = stringResource(Res.string.matric),
                    value = signUpScreenState.matricNumber,
                    onValueChange = {
                        onSignUpAction(SignUpAction.EditMatric(it))
                    },
                    singleLine = true,
                    imeAction = ImeAction.Next,
                    errorMessage = signUpScreenState.matricNumberFieldError,
                    keyboardType = KeyboardType.Text,
                    keyboardActions = KeyboardActions(
                        onNext = {
                            localFocusManager.clearFocus()
                        }
                    )
                )
            }


        }


        //Sign In Button

        Spacer(
            modifier = Modifier.height(largePadding)
        )
        ActionButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(Res.string.`continue`),
            height = 50.dp,
            isEnable = continueIsEnable,
            shouldEnableClick = true,
            onClick = {
                onSignUpAction(SignUpAction.SignUp)
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
                text = stringResource(Res.string.joined_before),
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
                text = stringResource(Res.string.login),
                onClick = navigateToSignInScreen
            )
        }
    }
}