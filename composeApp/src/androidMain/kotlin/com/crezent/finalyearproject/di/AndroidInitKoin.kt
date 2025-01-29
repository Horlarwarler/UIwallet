package com.crezent.finalyearproject.di

import com.crezent.finalyearproject.platform.AndroidApplicationComponent
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

fun androidInitKoin(
    config:KoinAppDeclaration
) {

    initKoin(
        additionalModules = listOf(module {
            single {
                AndroidApplicationComponent()
            }
        }),
        config = config
    )
}