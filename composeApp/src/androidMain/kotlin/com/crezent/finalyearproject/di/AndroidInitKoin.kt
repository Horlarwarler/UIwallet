package com.crezent.finalyearproject.di

import com.crezent.finalyearproject.platform.AndroidApplicationComponent
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

fun androidInitKoin(
    config: KoinAppDeclaration,
    //activity: Activity
) {

    initKoin(
        additionalModules = listOf(module {
//            single<PaymentScreenInterface> {
//                AndroidPayStackPaymentInterface(
////                    activity = activity
//                )
//            }
            single {
                AndroidApplicationComponent()
            }
        }),
        config = config
    )
}