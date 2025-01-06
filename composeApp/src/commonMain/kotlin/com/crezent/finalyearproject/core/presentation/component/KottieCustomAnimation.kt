package com.crezent.finalyearproject.core.presentation.component

import KottieAnimation
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kottieComposition.KottieCompositionSpec
import kottieComposition.animateKottieCompositionAsState
import kottieComposition.rememberKottieComposition

@Composable
fun KottieCustomAnimation(
    modifier: Modifier,
    onAnimationCompleted: () -> Unit,
    isPlaying: Boolean,
    animationJson: String,
    iterations: Int,

    ) {

    val kottieComposition = rememberKottieComposition(
        spec = KottieCompositionSpec.JsonString(animationJson)
    )





    val animationState by animateKottieCompositionAsState(
        composition = kottieComposition,
        isPlaying = isPlaying,
        iterations = iterations
    )

    LaunchedEffect(animationState.isCompleted) {
        if (animationState.isCompleted) {
            onAnimationCompleted()
        }
    }

    KottieAnimation(
        modifier = modifier.size(90.dp),
        composition = kottieComposition,
        progress = {
            animationState.progress
        },
        contentScale = contentScale.ContentScale.Fit
    )
}