package com.crezent.finalyearproject

import android.app.Application
import com.crezent.finalyearproject.di.androidInitKoin
import com.crezent.finalyearproject.di.initKoin
import com.crezent.finalyearproject.platform.AndroidApplicationComponent
import com.crezent.finalyearproject.platform.application
import org.koin.android.ext.koin.androidContext


class UIWallletApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        androidInitKoin(
            config = {
                androidContext(this@UIWallletApplication)
            }
        )

       // application(AndroidApplicationComponent())
    }
}