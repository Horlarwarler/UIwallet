package com.crezent.finalyearproject.core.presentation.component

import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.crezent.finalyearproject.ui.theme.NunitoFontFamily
import com.crezent.finalyearproject.ui.theme.black
import finalyearproject.composeapp.generated.resources.Res
import finalyearproject.composeapp.generated.resources.transfer
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun SectionHeader(
    modifier: Modifier = Modifier,
    text: StringResource
){
    Text(
        text = stringResource(text),
        textAlign = TextAlign.Left,
        style = TextStyle(
            lineHeight = 30.sp,
            fontFamily = NunitoFontFamily(),
            fontWeight = FontWeight.Normal,
            fontSize = 20.sp
        ),
        color = black
    )
}