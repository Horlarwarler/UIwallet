package com.crezent.finalyearproject.onboard.presentation.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.crezent.finalyearproject.ui.theme.colorWhite
import com.crezent.finalyearproject.ui.theme.green
import finalyearproject.composeapp.generated.resources.Res
import finalyearproject.composeapp.generated.resources.arrow
import org.jetbrains.compose.resources.painterResource

@Composable
fun NextArrow(
    modifier: Modifier,
    onNextClick: () -> Unit
) {
    Card(
        modifier = modifier
            .clip(CircleShape)
            .size(60.dp),
        onClick = onNextClick,
        shape = CircleShape,
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 4.dp,
            pressedElevation = 10.dp

        ),
        colors = CardDefaults.elevatedCardColors(
            contentColor = colorWhite,
            containerColor = green
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Icon(

                painter = painterResource(Res.drawable.arrow),
                contentDescription = "Next",
                tint = colorWhite

            )

        }

    }
}