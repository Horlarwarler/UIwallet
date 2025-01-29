package com.crezent.finalyearproject.home.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.crezent.finalyearproject.ui.theme.limeColor
import com.crezent.finalyearproject.ui.theme.limeGreen
import com.crezent.finalyearproject.ui.theme.smallPadding
import finalyearproject.composeapp.generated.resources.Res
import finalyearproject.composeapp.generated.resources.add
import finalyearproject.composeapp.generated.resources.chose_new_password
import finalyearproject.composeapp.generated.resources.payment
import finalyearproject.composeapp.generated.resources.send
import finalyearproject.composeapp.generated.resources.top_up
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource


@Composable
fun TransactionCards(
    navigateToDeposit :()-> Unit,
    navigateToPayment :() -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(50.dp)
    ) {
        HomeCard(
            modifier = Modifier
                .clickable {
                    navigateToDeposit()
                }
                .weight(1f)
                .fillMaxWidth()
                .aspectRatio(0.86f, true),
            icon = Res.drawable.add,
            text = Res.string.top_up,
            cardColor = limeColor
        )
        HomeCard(
            modifier = Modifier
                .clickable {
                    navigateToPayment()
                }
                .weight(1f)
                .fillMaxWidth()
                .aspectRatio(0.86f, true),
            icon = Res.drawable.send,
            text = Res.string.payment,
            cardColor = limeGreen
        )
    }

}

@Composable
private fun HomeCard(
    modifier: Modifier = Modifier,
    icon: DrawableResource,
    text: StringResource,
    cardColor: Color,
) {
    Card(
        modifier = modifier

            .sizeIn(maxWidth = 145.dp, maxHeight = 168.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp,
        ),
        colors = CardDefaults.cardColors(
            containerColor = cardColor
        ),
        shape = RoundedCornerShape(15.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(icon),
                contentDescription = stringResource(text)
            )
            Spacer(modifier = Modifier.height(smallPadding))
            Text(
                text = stringResource(text),
                textAlign = TextAlign.Center,
                style = TextStyle(
                    lineHeight = 41.sp,
                    fontFamily = NunitoFontFamily(),
                    fontWeight = FontWeight.W300,
                    fontSize = 20.sp
                )
            )


        }
    }

}