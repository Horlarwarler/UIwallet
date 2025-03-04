package com.crezent.finalyearproject.authentication.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.crezent.finalyearproject.ui.theme.NunitoFontFamily
import com.crezent.finalyearproject.ui.theme.green

@Composable
fun TextButton(
    modifier: Modifier,
    enable: Boolean = true,
    fontSize: TextUnit = 14.sp,
    text: String,
    textAlign: TextAlign = TextAlign.Start,
    onClick: () -> Unit
) {
    Text(
        modifier = modifier.clickable(
            enabled = enable
        ) {
            onClick()
        },
        text = text,
        textAlign = textAlign,
        style = TextStyle(
            lineHeight = 30.sp,
            fontFamily = NunitoFontFamily(),
            fontWeight = FontWeight.SemiBold,
            fontSize = fontSize,
            color = if (enable) green else green.copy(0.3f)
        )
    )

}