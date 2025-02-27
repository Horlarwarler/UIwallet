package com.crezent.finalyearproject.core.data.util

import android.app.Activity
import java.lang.ref.WeakReference

object CurrentActivityHolder {
    private var currentActivity: WeakReference<Activity>? = null

    fun set(activity: Activity) {
        currentActivity = WeakReference(activity)
    }

    fun get(): Activity? = currentActivity?.get()
}