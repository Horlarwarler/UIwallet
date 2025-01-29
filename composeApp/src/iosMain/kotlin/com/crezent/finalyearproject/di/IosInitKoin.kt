package com.crezent.finalyearproject.di

import com.crezent.finalyearproject.platform.IosApplicationComponent
import org.koin.dsl.module


fun iosInitKoin(
    component: IosApplicationComponent
) {
    initKoin(
        additionalModules = listOf(module { single { component } })
    )
}