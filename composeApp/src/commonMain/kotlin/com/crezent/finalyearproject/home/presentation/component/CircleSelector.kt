package com.crezent.finalyearproject.home.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
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
import com.crezent.finalyearproject.ui.theme.green
import com.crezent.finalyearproject.ui.theme.selectorColorBorder
import finalyearproject.composeapp.generated.resources.Res
import finalyearproject.composeapp.generated.resources.baseline_check_24
import org.jetbrains.compose.resources.painterResource

@Composable
fun CircleSelector(
    isSelected: Boolean = false
) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(if (isSelected) green else Color.Transparent)
            .border(
                width = 1.dp,
                color = if (isSelected) Color.Transparent else selectorColorBorder,
                shape = CircleShape
            )
            .size(15.dp),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = isSelected
        ) {
            Icon(
                modifier = Modifier.size(10.dp),
                painter = painterResource(Res.drawable.baseline_check_24),
                tint = colorWhite,
                contentDescription = "Check Mark"
            )
        }

    }
}