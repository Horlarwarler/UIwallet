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
import com.crezent.finalyearproject.core.data.network.BaseApi
import com.crezent.finalyearproject.core.data.network.BaseApiRepoImpl
import com.crezent.finalyearproject.core.data.network.KtorBaseApi
import com.crezent.finalyearproject.core.domain.BaseAppRepo
import com.crezent.finalyearproject.home.presentation.HomeScreenViewModel
import com.crezent.finalyearproject.onboard.presentation.OnboardViewmodel
import com.crezent.finalyearproject.splash.presesentation.SplashViewModel
import com.crezent.finalyearproject.transaction.data.KtorTransactionApi
import com.crezent.finalyearproject.transaction.data.TransactionApi
import com.crezent.finalyearproject.transaction.data.TransactionRepoImpl
import com.crezent.finalyearproject.transaction.domain.TransactionRepo
import com.crezent.finalyearproject.transaction.presentation.deposit.DepositScreenViewmodel
import com.crezent.finalyearproject.transaction.presentation.new_credit_card.NewCreditCardViewmodel
import com.crezent.finalyearproject.transaction.presentation.payment_method.PaymentScreenViewModel
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
    singleOf(::KtorBaseApi).bind<BaseApi>()
    singleOf(::BaseApiRepoImpl).bind<BaseAppRepo>()
    singleOf(::KtorTransactionApi).bind<TransactionApi>()
    singleOf(::TransactionRepoImpl).bind<TransactionRepo>()

    viewModelOf(::OnboardViewmodel)
    viewModelOf(::SignInViewModel)
    viewModelOf(::SignUpViewModel)
    viewModelOf(::ForgotPasswordViewModel)
    viewModelOf(::OtpViewModel)
    viewModelOf(::ResetPasswordScreenViewModel)
    viewModelOf(::HomeScreenViewModel)
    viewModelOf(::DepositScreenViewmodel)
    viewModelOf(::PaymentScreenViewModel)
    viewModelOf(::NewCreditCardViewmodel)
}

