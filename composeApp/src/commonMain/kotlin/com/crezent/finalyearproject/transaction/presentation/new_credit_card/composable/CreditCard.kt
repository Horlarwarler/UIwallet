package com.crezent.finalyearproject.transaction.presentation.new_credit_card.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.crezent.finalyearproject.core.presentation.util.transformString
import com.crezent.finalyearproject.ui.theme.colorWhite
import com.crezent.finalyearproject.ui.theme.largePadding
import com.crezent.finalyearproject.ui.theme.mediumPadding
import finalyearproject.composeapp.generated.resources.Res
import finalyearproject.composeapp.generated.resources.credit_card_background
import finalyearproject.composeapp.generated.resources.cvv
import finalyearproject.composeapp.generated.resources.valid_thru
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun CreditCard(
    creditCardNumber: String?,
    expirationDate: String? = null,
    hideCreditCard: Boolean = true,
    hideCvv: Boolean = true,
    cvv: String? = null

) {
    Box(
        modifier = Modifier
            .sizeIn(
                maxWidth = 368.dp,
                maxHeight = 250.dp
            )
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(Res.drawable.credit_card_background),
            contentDescription = "Credit Card"
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = largePadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val creditCardText =
                creditCardNumber.hideNumber(16, hideCreditCard).transformString(" ", 4)
            val cvvText = cvv.hideNumber(3, hideCvv)

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = creditCardText,
                textAlign = TextAlign.Center,
                lineHeight = 30.sp,
                fontSize = 25.sp,
                fontWeight = FontWeight.Medium,
                color = colorWhite.copy(0.5f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(
                modifier = Modifier.height(mediumPadding)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CardInformation(
                    title = Res.string.valid_thru,
                    information = expirationDate?.transformString("/", 2) ?: "XX/XX"

                )
                CardInformation(
                    title = Res.string.cvv,
                    information = cvvText
                )

            }
            Spacer(
                modifier = Modifier.height(mediumPadding)
            )

        }
    }
}

private fun String?.hideNumber(
    numberOfX: Int,
    shouldHide: Boolean

): String {
    var x = ""
    repeat(numberOfX) {
        x += "X"
    }
    if (shouldHide || this.isNullOrBlank()) {
        return x
    }

    return this + x.dropLast(this.length)

}

@Composable
private fun CardInformation(
    title: StringResource,
    information: String
) {
    Column(
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = stringResource(title),
            textAlign = TextAlign.Left,
            lineHeight = 30.sp,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            color = colorWhite.copy(0.5f),
            maxLines = 1
        )
        Text(
            text = information,
            textAlign = TextAlign.Left,
            lineHeight = 30.sp,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            color = colorWhite.copy(0.5f),
            maxLines = 1
        )
    }

}