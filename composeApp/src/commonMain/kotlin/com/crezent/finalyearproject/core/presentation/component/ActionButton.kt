package com.crezent.finalyearproject.core.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.crezent.finalyearproject.ui.theme.NunitoFontFamily
import com.crezent.finalyearproject.ui.theme.colorWhite
import com.crezent.finalyearproject.ui.theme.green

@Composable

fun ActionButton(
    modifier: Modifier,
    text: String,
    height: Dp = 50.dp,
    isEnable: Boolean = true,
    shouldEnableClick: Boolean = true,
    enableTextColor: Color = colorWhite,
    enableBackgroundColor: Color = green,
    disableTextColor: Color = colorWhite.copy(alpha = 0.5f),
    disableBackgroundColor: Color = green.copy(alpha = 0.5f),
    textSize: TextUnit = 16.sp,
    onClick: () -> Unit

) {

    Box(
        modifier = modifier
            .clickable(
                enabled = shouldEnableClick,
                onClick = onClick
            )
            .clip(RoundedCornerShape(10.dp))
            .fillMaxWidth()
            .background(if (isEnable) enableBackgroundColor else disableBackgroundColor)
            .height(height),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = text,
            textAlign = TextAlign.Center,
            color = if (isEnable) enableTextColor else disableTextColor,
            fontSize = textSize,
            fontWeight = FontWeight.Bold,
            fontFamily = NunitoFontFamily()
        )
    }
}