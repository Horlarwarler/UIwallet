package com.crezent.finalyearproject.transaction.presentation.transaction_details.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.crezent.finalyearproject.core.domain.model.FundingSource
import com.crezent.finalyearproject.core.domain.model.Transaction
import com.crezent.finalyearproject.core.presentation.util.formatReadableDate
import com.crezent.finalyearproject.transaction.TransactionStatus
import com.crezent.finalyearproject.ui.theme.NunitoFontFamily
import com.crezent.finalyearproject.ui.theme.black
import com.crezent.finalyearproject.ui.theme.colorWhite
import com.crezent.finalyearproject.ui.theme.errorColor
import com.crezent.finalyearproject.ui.theme.limeColor
import com.crezent.finalyearproject.ui.theme.mediumPadding
import com.crezent.finalyearproject.ui.theme.smallPadding
import finalyearproject.composeapp.generated.resources.Res
import finalyearproject.composeapp.generated.resources.account_number
import finalyearproject.composeapp.generated.resources.amount
import finalyearproject.composeapp.generated.resources.bank
import finalyearproject.composeapp.generated.resources.card_number
import finalyearproject.composeapp.generated.resources.email
import finalyearproject.composeapp.generated.resources.funding_source
import finalyearproject.composeapp.generated.resources.payer_name
import finalyearproject.composeapp.generated.resources.payment_status
import finalyearproject.composeapp.generated.resources.payment_time
import finalyearproject.composeapp.generated.resources.transaction_id
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun TransactionDetailsBox(
    transaction: Transaction,
    email: String,
    name: String,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(15.dp))
            .background(limeColor)
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 15.dp, vertical = mediumPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TransactionDetailsCard(
            title = Res.string.amount,
            description = {
                Text(
                    text = "# ${transaction.transactionAmount}",
                    fontFamily = NunitoFontFamily(),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                    color = Color(0XFFD91C20)
                )
            }
        )
        Spacer(modifier = Modifier.height(mediumPadding))

        TransactionDetailsCard(
            title = Res.string.payer_name,
            description = {
                TransactionDescription(text = name)
            }
        )
        Spacer(modifier = Modifier.height(mediumPadding))


        TransactionDetailsCard(
            title = Res.string.payment_status,
            description = {
                TransactionStatus(
                    status = transaction.transactionStatus,
                    color = color
                )
            }
        )
        Spacer(modifier = Modifier.height(mediumPadding))

        TransactionDivider()

        Spacer(modifier = Modifier.height(mediumPadding))

        TransactionDetailsCard(
            title = Res.string.funding_source,
            description = {
                TransactionDescription(text = transaction.fundingSource.channel)
            }
        )
        Spacer(
            modifier = Modifier.height(mediumPadding)

        )

        if (transaction.fundingSource is FundingSource.CardPayment) {
            TransactionDetailsCard(
                title = Res.string.card_number,
                description = {
                    TransactionDescription(text = "XXXX XXXX ${transaction.fundingSource.cardNumber}")
                }
            )
        } else if (transaction.fundingSource is FundingSource.UssdPayment) {
            TransactionDetailsCard(
                title = Res.string.bank,
                description = {
                    TransactionDescription(text = transaction.fundingSource.bank)
                }
            )
        }

        if (transaction.fundingSource is FundingSource.Bank) {
            TransactionDetailsCard(
                title = Res.string.bank,
                description = {
                    TransactionDescription(text = transaction.fundingSource.bank)
                }
            )
            Spacer(
                modifier = Modifier.height(mediumPadding)

            )
            TransactionDetailsCard(
                title = Res.string.account_number,
                description = {
                    TransactionDescription(text = transaction.fundingSource.lastFourDigit)
                }
            )
        }

        Spacer(
            modifier = Modifier.height(mediumPadding)

        )
        TransactionDetailsCard(
            title = Res.string.payment_time,
            description = {

                TransactionDescription(text = transaction.paidAt.formatReadableDate())
            }
        )

        Spacer(
            modifier = Modifier.height(mediumPadding)

        )
        TransactionDetailsCard(
            title = Res.string.transaction_id,
            description = {
                TransactionDescription(text = transaction.transactionId)
            }
        )
        Spacer(
            modifier = Modifier.height(mediumPadding)

        )
        TransactionDetailsCard(
            title = Res.string.email,
            description = {
                TransactionDescription(text = email)
            }
        )
        Spacer(
            modifier = Modifier.height(mediumPadding)

        )


    }
}

@Composable
private fun TransactionDetailsCard(
    title: StringResource,
    description: @Composable () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(title),
            fontFamily = NunitoFontFamily(),
            fontWeight = FontWeight.Light,
            fontSize = 15.sp,
            color = black
        )
        Spacer(
            modifier = Modifier.padding(end = smallPadding)
        )

        description()
    }
}

@Composable
private fun TransactionDescription(
    text: String
) {
    Text(
        text = text,
        fontFamily = NunitoFontFamily(),
        fontWeight = FontWeight.Bold,
        fontSize = 15.sp,
        color = black,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1
    )
}

@Composable
private fun TransactionDivider(
    count: Int = 20,
    spacer: Int = 20
) {
    Canvas(
        modifier = Modifier.fillMaxWidth()
    ) {
        val width = (size.width) / (count + spacer)
        var x = 0f
        repeat(count) {
            drawLine(
                color = Color(0XFFDADADA),
                start = _root_ide_package_.androidx.compose.ui.geometry.Offset(x = x, y = 0F),
                end = _root_ide_package_.androidx.compose.ui.geometry.Offset(x = x + width, y = 0f),
                strokeWidth = 5f
            )
            x += width + spacer
        }
    }
}

@Composable
private fun TransactionStatus(
    color: Color,
    status: TransactionStatus
) {

    Text(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(color.copy(0.6f))
            .padding(horizontal = 15.dp, vertical = 5.dp),
        text = status.toString().capitalize(),
        color = colorWhite,
        fontFamily = NunitoFontFamily(),
        fontSize = 15.sp,
        fontWeight = FontWeight.Medium

    )
}