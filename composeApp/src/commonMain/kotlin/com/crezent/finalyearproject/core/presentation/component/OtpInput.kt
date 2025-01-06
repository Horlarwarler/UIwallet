package com.crezent.finalyearproject.core.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.crezent.finalyearproject.ui.theme.NunitoFontFamily
import com.crezent.finalyearproject.ui.theme.errorColor
import com.crezent.finalyearproject.ui.theme.green
import com.crezent.finalyearproject.ui.theme.lightBlack
import com.crezent.finalyearproject.ui.theme.otpBackground

@Composable
fun OtpInput(
    modifier: Modifier,
    onInputChange: (String) -> Unit,
    isError: Boolean,
    otp: String
) {

   
    BasicTextField(
        modifier = modifier.fillMaxWidth(),
        value = otp,
        onValueChange = onInputChange,
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.NumberPassword
        ),
        decorationBox = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,

                ) {
                repeat(6) { index ->
                    val charText = when {
                        index >= otp.length -> ""
                        else -> otp[index].toString()
                    }
                    OtpBox(
                        modifier = Modifier
                            .weight(1f)
                            .padding(if (index != 6) 6.dp else 0.dp),
                        isError = isError,
                        isCurrent = index == otp.length,
                        value = charText
                    )
                }
            }

        }
    )
}

@Composable
private fun OtpBox(
    modifier: Modifier,
    isError: Boolean,
    isCurrent: Boolean,
    value: String
) {

    val borderColor = when {
        isError -> errorColor
        isCurrent -> green
        else -> Color.Transparent
    }

    val displayText = when {
        value.isEmpty() && !isCurrent -> "-"
        else -> value
    }
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .border(
                width = 1.dp, color = borderColor, shape = RoundedCornerShape(10.dp)
            )
            .sizeIn(maxWidth = 60.dp, maxHeight = 60.dp)
            .background(otpBackground)
            .aspectRatio(1f),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = displayText,
            fontFamily = NunitoFontFamily(),
            fontWeight = FontWeight.Medium,
            fontSize = 25.sp,
            color = green,
            textAlign = TextAlign.Center
        )
    }
}