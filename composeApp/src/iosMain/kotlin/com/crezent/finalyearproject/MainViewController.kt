package com.crezent.finalyearproject

import androidx.compose.ui.window.ComposeUIViewController
import com.crezent.finalyearproject.app.App
import com.crezent.finalyearproject.di.initKoin

fun MainViewController() = ComposeUIViewController {
   // initKoin()
    App()
}