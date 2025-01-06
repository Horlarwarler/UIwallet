package com.crezent.finalyearproject

import android.app.Application
import com.crezent.finalyearproject.di.initKoin
import org.koin.android.ext.koin.androidContext


class UIWallletApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@UIWallletApplication)
        }
    }
}