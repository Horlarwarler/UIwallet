package com.crezent.finalyearproject.authentication.presentation.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.crezent.finalyearproject.ui.theme.NunitoFontFamily
import com.crezent.finalyearproject.ui.theme.black

@Composable
fun AuthenticationScreenTitle(
    title :String
){
    Text(
        modifier = Modifier.fillMaxWidth(),
       text = title,
        color = black,
        fontFamily = NunitoFontFamily(),
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp,
        lineHeight = 50.sp,
        textAlign = TextAlign.Left
    )
}