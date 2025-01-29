package com.crezent.finalyearproject.core.presentation.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ViewConfiguration
import androidx.compose.ui.unit.DpSize


fun zeroMinTouchSizeConfig(viewConfiguration: ViewConfiguration): ViewConfiguration {
    return object : ViewConfiguration {
        override val longPressTimeoutMillis: Long
            get() = viewConfiguration.longPressTimeoutMillis
        override val doubleTapTimeoutMillis: Long
            get() = viewConfiguration.doubleTapTimeoutMillis
        override val doubleTapMinTimeMillis: Long
            get() = viewConfiguration.doubleTapMinTimeMillis
        override val touchSlop: Float
            get() = viewConfiguration.touchSlop
        override val minimumTouchTargetSize: DpSize
            // Zero size for views
            get() = DpSize.Zero
    }

}