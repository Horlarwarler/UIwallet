package com.crezent.finalyearproject.di

import com.crezent.finalyearproject.authentication.data.AuthenticationRepo
import com.crezent.finalyearproject.authentication.data.network.AuthenticationRemote
import com.crezent.finalyearproject.authentication.data.network.KtorAuthenticationRemote
import com.crezent.finalyearproject.authentication.data.repo.AuthenticationRepoImpl
import com.crezent.finalyearproject.authentication.presentation.forgot_password.ForgotPasswordViewModel
import com.crezent.finalyearproject.authentication.presentation.otp.OtpViewModel
import com.crezent.finalyearproject.authentication.presentation.recovery_password.ResetPasswordScreenViewModel
import com.crezent.finalyearproject.authentication.presentation.signin.SignInViewModel
import com.crezent.finalyearproject.authentication.presentation.signup.SignUpViewModel
import com.crezent.finalyearproject.core.data.HttpClientFactory
import com.crezent.finalyearproject.onboard.presentation.OnboardViewmodel
import com.crezent.finalyearproject.splash.presesentation.SplashViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformModule: Module

val sharedModule = module {

    viewModelOf(::SplashViewModel)
    single { HttpClientFactory.create(get()) }
    singleOf(::KtorAuthenticationRemote).bind<AuthenticationRemote>()
    singleOf(::AuthenticationRepoImpl).bind<AuthenticationRepo>()

    viewModelOf(::OnboardViewmodel)
    viewModelOf(::SignInViewModel)
    viewModelOf(::SignUpViewModel)
    viewModelOf(::ForgotPasswordViewModel)
    viewModelOf(::OtpViewModel)
    viewModelOf(::ResetPasswordScreenViewModel)


    //viewModelOf(SplashViewModel())
}

