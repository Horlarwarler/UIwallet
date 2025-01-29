package com.crezent.finalyearproject.home.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.crezent.finalyearproject.core.domain.model.Transaction
import com.crezent.finalyearproject.transaction.TransactionType
import com.crezent.finalyearproject.ui.theme.NunitoFontFamily
import com.crezent.finalyearproject.ui.theme.black
import com.crezent.finalyearproject.ui.theme.dividerColor
import com.crezent.finalyearproject.ui.theme.errorColor
import com.crezent.finalyearproject.ui.theme.green
import com.crezent.finalyearproject.ui.theme.limeGreen
import com.crezent.finalyearproject.ui.theme.limePurple
import finalyearproject.composeapp.generated.resources.Res
import finalyearproject.composeapp.generated.resources.available_balance
import finalyearproject.composeapp.generated.resources.thin_arrow
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun TransactionCard(
    transaction: Transaction,
    onClick: () -> Unit
) {
    val isDebit = transaction.transactionType == TransactionType.Debit
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = 10.dp
            )
            .clickable {
                onClick()
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(color = if (isDebit) limePurple else limeGreen)
                    .size(45.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.rotate(
                        if (isDebit) 180f else 0f
                    ),
                    painter = painterResource(Res.drawable.thin_arrow),
                    contentDescription = transaction.transactionType.toString()
                )
            }
            Spacer(modifier = Modifier.width(15.dp))
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                Text(
                    text = transaction.transactionTitle,
                    textAlign = TextAlign.Left,
                    style = TextStyle(
                        lineHeight = 30.sp,
                        fontFamily = NunitoFontFamily(),
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    ),
                    color = black
                )
                Text(
                    text = transaction.transactionDescription,
                    textAlign = TextAlign.Left,
                    style = TextStyle(
                        //lineHeight = 30.sp,
                        fontFamily = NunitoFontFamily(),
                        fontWeight = FontWeight.Light,
                        fontSize = 14.sp
                    ),
                    color = black
                )
            }

            Text(
                text = "#${transaction.transactionAmount}",
                textAlign = TextAlign.Left,
                style = TextStyle(
                    lineHeight = 30.sp,
                    fontFamily = NunitoFontFamily(),
                    fontWeight = FontWeight.W300,
                    fontSize = 14.sp
                ),
                color = if (isDebit) errorColor else green
            )


        }
        Spacer(modifier = Modifier.height(10.dp))
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            color = dividerColor,
            thickness = 1.dp
        )

    }
}