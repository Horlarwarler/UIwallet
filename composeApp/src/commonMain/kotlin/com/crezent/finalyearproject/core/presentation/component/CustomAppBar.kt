package com.crezent.finalyearproject.core.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.crezent.finalyearproject.ui.theme.NunitoFontFamily
import com.crezent.finalyearproject.ui.theme.black
import finalyearproject.composeapp.generated.resources.Res
import finalyearproject.composeapp.generated.resources.arrow
import org.jetbrains.compose.resources.painterResource

@Composable
fun CustomAppBar(
    modifier: Modifier,
    onNavigationIconClick: () -> Unit,
    topText: String? = null,
    rightAction: @Composable BoxScope.(

    ) -> Unit = {},
) {

    Box(
        modifier = modifier
            .fillMaxWidth()

    ) {
        Icon(
            modifier = Modifier
                .clickable {
                    onNavigationIconClick()
                }
                .align(Alignment.TopStart)
                .rotate(-180f),
            painter = painterResource(Res.drawable.arrow),
            contentDescription = "Next",
            tint = black,
        )

        topText?.let {
            Text(
                text = it,
                color = black,
                fontFamily = NunitoFontFamily(),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }
        rightAction()


    }
}