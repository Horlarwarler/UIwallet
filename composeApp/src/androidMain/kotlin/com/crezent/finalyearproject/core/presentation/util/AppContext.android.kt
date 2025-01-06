package com.crezent.finalyearproject.core.presentation.util

import android.app.Application
import android.content.Context

actual object AppContext {
    private lateinit var application: Application
    fun setUpContext(context: Context) {
        application = context as Application
    }

    fun getContext(): Context {
        if (!::application.isInitialized) {
            throw (Exception("Application context not initialized"))
        }
        return application.applicationContext
    }
}