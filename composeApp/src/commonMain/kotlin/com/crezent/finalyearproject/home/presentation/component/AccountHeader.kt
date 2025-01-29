package com.crezent.finalyearproject.home.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.core.uri.Uri
import com.crezent.finalyearproject.ui.theme.NunitoFontFamily
import com.crezent.finalyearproject.ui.theme.black
import finalyearproject.composeapp.generated.resources.Res
import finalyearproject.composeapp.generated.resources.available_balance
import finalyearproject.composeapp.generated.resources.chose_new_password
import finalyearproject.composeapp.generated.resources.ui_logo
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun AccountHeader(
    profilePicture: Uri? = null,
    availableBalance: Double,
    onProfileClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Image(
            painter = painterResource(Res.drawable.ui_logo),
            modifier = Modifier
                .clickable {
                    onProfileClick()
                }
                .clip(CircleShape)
//                .background(
//                    color = Color(0xFFe6eef5)
//                )
//                .padding(7.dp)
                .size(50.dp),
            contentDescription = "User Dp"
        )
        Column(
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = stringResource(Res.string.available_balance),
                textAlign = TextAlign.Left,
                style = TextStyle(
                    lineHeight = 30.sp,
                    fontFamily = NunitoFontFamily(),
                    fontWeight = FontWeight.Normal,
                    fontSize = 15.sp
                ),
                color = black
            )
            Text(
                text = "#$availableBalance",
                textAlign = TextAlign.Left,
                style = TextStyle(
                    lineHeight = 30.sp,
                    fontFamily = NunitoFontFamily(),
                    fontWeight = FontWeight.Normal,
                    fontSize = 30.sp
                ),
                color = black
            )
        }
    }

}