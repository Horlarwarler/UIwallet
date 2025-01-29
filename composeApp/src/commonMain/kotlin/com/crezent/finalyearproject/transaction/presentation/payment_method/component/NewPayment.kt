package com.crezent.finalyearproject.transaction.presentation.payment_method.component

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.crezent.finalyearproject.home.presentation.component.CircleSelector
import com.crezent.finalyearproject.ui.theme.NunitoFontFamily
import com.crezent.finalyearproject.ui.theme.black
import com.crezent.finalyearproject.ui.theme.dividerColor
import com.crezent.finalyearproject.ui.theme.green
import com.crezent.finalyearproject.ui.theme.mediumPadding
import com.crezent.finalyearproject.ui.theme.smallPadding
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun NewPayment(
    modifier: Modifier = Modifier,
    canSelect: Boolean = true,
    isSelected: Boolean = false,
    icon: DrawableResource,
    title: StringResource,
    description: StringResource,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .clickable {
                onClick()
            }
            .border(
                width = 1.dp,
                color = if (canSelect) dividerColor else Color.Transparent,
                shape = RoundedCornerShape(10.dp)
            )
            .fillMaxWidth()
            .padding(horizontal = mediumPadding, vertical = 15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = dividerColor,
                    shape = RoundedCornerShape(5.dp)
                )
                .size(width = 57.dp, height = 39.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(icon),
                tint = green,
                contentDescription = stringResource(title)
            )
        }
        Spacer(
            modifier = Modifier.width(mediumPadding)
        )
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(title),
                textAlign = TextAlign.Left,
                style = TextStyle(

                    fontFamily = NunitoFontFamily(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                ),
                color = green
            )
            Text(
                text = stringResource(description),
                textAlign = TextAlign.Left,
                style = TextStyle(
                    //lineHeight = 30.sp,
                    fontFamily = NunitoFontFamily(),
                    fontWeight = FontWeight.Light,
                    fontSize = 12.sp
                ),
                color = black
            )
        }

        if (canSelect) {
            CircleSelector(
                isSelected = isSelected
            )
        }

    }
}