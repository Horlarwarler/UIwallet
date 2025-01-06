package com.crezent.finalyearproject.onboard.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.crezent.finalyearproject.ui.theme.background
import com.crezent.finalyearproject.ui.theme.green
import com.crezent.finalyearproject.ui.theme.lightGreen

@Composable
fun ProgressIndicator(
    isCurrent: Boolean
) {

    Box(
        modifier = Modifier
            .size(30.dp),
        contentAlignment = Alignment.Center,

        ) {

        // ShadowBox

        AnimatedVisibility(
            visible = isCurrent
        ) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .border(
                        width = 5.dp,
                        color = green,
                        shape = CircleShape
                    )
                    .background(
                        color = background
                    )

                    .size(25.dp)
            )
        }

        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(lightGreen)
                .size(if (isCurrent) 10.dp else 15.dp)
        )

    }
}