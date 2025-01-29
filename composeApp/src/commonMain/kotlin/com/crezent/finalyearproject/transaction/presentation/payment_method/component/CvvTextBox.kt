package com.crezent.finalyearproject.transaction.presentation.payment_method.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.crezent.finalyearproject.ui.theme.NunitoFontFamily
import com.crezent.finalyearproject.ui.theme.black
import com.crezent.finalyearproject.ui.theme.green
import finalyearproject.composeapp.generated.resources.Res
import finalyearproject.composeapp.generated.resources.cvv
import finalyearproject.composeapp.generated.resources.enter_amount
import org.jetbrains.compose.resources.stringResource

@Composable
fun CvvTextBox(
    cvvText: String?,
    openKeyboard: () -> Unit
) {

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(5.dp))
            .border(width = 1.dp, color = green, shape = RoundedCornerShape(5.dp))
            .size(width = 77.dp, height = 35.dp),
        contentAlignment = Alignment.Center
    ) {
        if (cvvText.isNullOrBlank()) {
            Text(
                modifier = Modifier
                    .clickable {
                        openKeyboard()
                    },
                text = stringResource(Res.string.cvv),
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 30.sp,
                    fontFamily = NunitoFontFamily(),
                    fontWeight = FontWeight.Light,
                    color = black,
                    letterSpacing = 2.sp,
                )
            )
        } else {
            Text(
                modifier = Modifier
                    .clickable {
                        openKeyboard()
                    },
                text = cvvText,
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 30.sp,
                    fontFamily = NunitoFontFamily(),
                    fontWeight = FontWeight.Bold,
                    color = black,
                    letterSpacing = 2.sp,
                )
            )
        }
    }
}