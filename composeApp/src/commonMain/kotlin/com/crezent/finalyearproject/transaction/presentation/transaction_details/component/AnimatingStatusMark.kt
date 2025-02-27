package com.crezent.finalyearproject.transaction.presentation.transaction_details.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.crezent.finalyearproject.ui.theme.colorWhite
import finalyearproject.composeapp.generated.resources.Res
import finalyearproject.composeapp.generated.resources.checkmark
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun AnimatingStatusMark(
    backgroundColor: Color,
    animateColor: Color,
    icon: DrawableResource,
    modifier: Modifier = Modifier,
) {
    val infiniteTransition = rememberInfiniteTransition()
    val width = infiniteTransition.animateFloat(
        initialValue = 5f,
        targetValue = 20f, animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing)
        )
    )


    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(
                color = animateColor,
                shape = CircleShape
            )
            .padding(width.value.dp)
            .background(
                color = backgroundColor,
                shape = CircleShape
            )
            .size(50.dp),
        contentAlignment = Alignment.Center
    ) {

        Icon(
            painter = painterResource(icon),
            tint = colorWhite,
            contentDescription = "Icon",

            )
    }
}