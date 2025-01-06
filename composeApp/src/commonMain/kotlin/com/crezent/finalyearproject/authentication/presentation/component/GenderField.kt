package com.crezent.finalyearproject.authentication.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.crezent.finalyearproject.ui.theme.NunitoFontFamily
import com.crezent.finalyearproject.ui.theme.black
import com.crezent.finalyearproject.ui.theme.colorWhite
import com.crezent.finalyearproject.ui.theme.green
import com.crezent.finalyearproject.ui.theme.grey
import com.crezent.finalyearproject.ui.theme.mediumPadding
import com.crezent.finalyearproject.ui.theme.smallPadding
import finalyearproject.composeapp.generated.resources.Res
import finalyearproject.composeapp.generated.resources.checkmark
import finalyearproject.composeapp.generated.resources.gender
import finalyearproject.composeapp.generated.resources.vector
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun GenderField(
    modifier: Modifier,
    selected: String = "M",
    onGenderSelect: (String) -> Unit
) {

    Row(
        modifier = modifier.padding(horizontal = smallPadding),
        verticalAlignment = Alignment.CenterVertically

    ) {
        //
        Icon(
            modifier = Modifier
                .sizeIn(maxWidth = 24.dp, maxHeight = 24.dp),
            painter = painterResource(resource = Res.drawable.vector),
            contentDescription = "Leading Icon",
            tint = grey,
        )
        Spacer(Modifier.padding(smallPadding))
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(Res.string.gender),
            textAlign = TextAlign.Left,
            style = TextStyle(
                lineHeight = 30.sp,
                fontFamily = NunitoFontFamily(),
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp,
                color = grey
            )
        )

        GenderIndicator(
            isSelected = selected == "M",
            text = "Male",
            modifier = Modifier.clickable {
                onGenderSelect("M")
            }
        )
        Spacer(modifier = Modifier.width(5.dp))

        GenderIndicator(
            isSelected = selected == "F",
            text = "Female",
            modifier = Modifier.clickable {
                onGenderSelect("F")
            }
        )


    }
}

@Composable
fun GenderIndicator(
    modifier: Modifier,
    isSelected: Boolean,
    text: String
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Left,
            style = TextStyle(
                lineHeight = 30.sp,
                fontFamily = NunitoFontFamily(),
                fontWeight = FontWeight.Medium,
                fontSize = 15.sp,
                color = black
            )
        )
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .border(
                    width = 2.dp,
                    color = green,
                    shape = CircleShape
                )
                .background(if (isSelected) green else Color.Transparent)
                .size(30.dp),
            contentAlignment = Alignment.Center
        ) {
            if (isSelected) {
                Icon(
                    modifier = Modifier.size(15.dp),
                    painter = painterResource(Res.drawable.checkmark),
                    contentDescription = "Selected",
                    tint = colorWhite

                )
            }
        }

    }

}