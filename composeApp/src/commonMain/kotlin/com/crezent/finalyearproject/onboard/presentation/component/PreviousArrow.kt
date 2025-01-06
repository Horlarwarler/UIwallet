package com.crezent.finalyearproject.onboard.presentation.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import com.crezent.finalyearproject.ui.theme.black
import com.crezent.finalyearproject.ui.theme.colorWhite
import finalyearproject.composeapp.generated.resources.Res
import finalyearproject.composeapp.generated.resources.arrow
import org.jetbrains.compose.resources.painterResource

@Composable
fun PreviousArrow(
    modifier: Modifier,
    onPreviousClick: () -> Unit
) {
    Card(
        modifier = modifier
            .size(45.dp),
        onClick = onPreviousClick,
        shape = CircleShape,
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 4.dp,
            pressedElevation = 10.dp
        ),
        colors = CardDefaults.elevatedCardColors(
            contentColor = black,
            containerColor = colorWhite
        ),
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                modifier = Modifier
                    .height(15.dp)
                    .width(25.dp)
                    .rotate(-180f),
                painter = painterResource(Res.drawable.arrow),
                contentDescription = "Previous",
                tint = black

            )
        }

    }
}