package com.crezent.finalyearproject.core.presentation.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimationDialog(
    modifier: Modifier,
    onAnimationCompleted: () -> Unit,
    animationSize: Dp = 200.dp,
    isPlaying: Boolean,
    animationJson: String,
    iterations: Int,
    closeDialog: () -> Unit
) {


    BasicAlertDialog(
        modifier = modifier,
        onDismissRequest = closeDialog,

        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {

        KottieCustomAnimation(
            modifier = Modifier.size(animationSize),
            onAnimationCompleted = onAnimationCompleted,
            isPlaying = isPlaying,
            animationJson = animationJson,
            iterations = iterations,
        )
    }
}