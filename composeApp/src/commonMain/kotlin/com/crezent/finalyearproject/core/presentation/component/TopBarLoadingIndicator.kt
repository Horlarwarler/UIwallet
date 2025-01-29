package com.crezent.finalyearproject.core.presentation.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.crezent.finalyearproject.ui.theme.limeColor
import com.crezent.finalyearproject.ui.theme.limeGreen
import com.crezent.finalyearproject.ui.theme.mediumPadding

@Composable
fun TopBarLoadingIndicator(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
) {
    Box(
        modifier = modifier
            .height(mediumPadding)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
                color = limeGreen,
                trackColor = limeColor,
                strokeCap = ProgressIndicatorDefaults.CircularIndeterminateStrokeCap
            )
        }
    }
}