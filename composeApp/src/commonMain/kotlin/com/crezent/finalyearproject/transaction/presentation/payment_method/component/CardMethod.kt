package com.crezent.finalyearproject.transaction.presentation.payment_method.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.crezent.finalyearproject.core.domain.model.Card
import com.crezent.finalyearproject.core.presentation.component.ActionButton
import com.crezent.finalyearproject.home.presentation.component.CircleSelector
import com.crezent.finalyearproject.transaction.presentation.payment_method.util.PaymentMethod
import com.crezent.finalyearproject.ui.theme.NunitoFontFamily
import com.crezent.finalyearproject.ui.theme.black
import com.crezent.finalyearproject.ui.theme.dividerColor
import com.crezent.finalyearproject.ui.theme.mediumPadding
import com.crezent.finalyearproject.ui.theme.smallPadding
import finalyearproject.composeapp.generated.resources.Res
import finalyearproject.composeapp.generated.resources.add
import finalyearproject.composeapp.generated.resources.master_card
import finalyearproject.composeapp.generated.resources.new_card
import finalyearproject.composeapp.generated.resources.save_pay_with_card
import finalyearproject.composeapp.generated.resources.verify_cvv
import finalyearproject.composeapp.generated.resources.visa
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun CardMethod(
    modifier: Modifier,
    onCardClick: (Card) -> Unit,
    addNewCard: () -> Unit,
    openKeyBoard: (String) -> Unit,
    currentSelectedCard: Card? = null,
    verifyCvv: () -> Unit,
    cvvText: String?,
    selectedPayment: PaymentMethod?,
    cards: List<Card> = emptyList()
) {
    //val verticalScroll = rememberScrollState()
    Column(
        modifier = modifier
           // .verticalScroll(verticalScroll)
            .clip(RoundedCornerShape(10.dp))
            .border(color = dividerColor, shape = RoundedCornerShape(10.dp), width = 1.dp)
            .fillMaxWidth(),

        ) {
        NewPayment(
            canSelect = false,
            icon = Res.drawable.add,
            title = Res.string.new_card,
            description = Res.string.save_pay_with_card,
            onClick = addNewCard
        )

        repeat(cards.size) {
            val card = cards[it]
            val isSelected =
                selectedPayment != null && selectedPayment is PaymentMethod.CardPayment && selectedPayment.card.cardId == card.cardId
            ExistingCard(
                openKeyBoard = {
                    openKeyBoard(card.cardId)
                },
                card = card,
                icon = if (it % 2 == 1) Res.drawable.visa else Res.drawable.master_card,
                isSelected = isSelected,
                onClick = {
                    onCardClick(card)
                },
                verifyCvv = verifyCvv,
                showCVV = currentSelectedCard == card,
                cvvText = if (currentSelectedCard == card) cvvText else null,
                )
        }
    }

}

@Composable
private fun ExistingCard(
    modifier: Modifier = Modifier,
    openKeyBoard: () -> Unit,
    card: Card,
    cvvText: String? = null,
    isSelected: Boolean = false,
    icon: DrawableResource,
    onClick: () -> Unit,
    verifyCvv: () -> Unit,
    showCVV: Boolean
) {
    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(start = mediumPadding, end = mediumPadding, top = 15.dp, bottom = 10.dp),
    ) {
        Row(
            modifier = modifier.fillMaxWidth()
                .clickable {
                    onClick()
                },

            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                modifier = Modifier
                    .size(width = 57.dp, height = 39.dp),
                painter = painterResource(icon),
                contentDescription = "Card Image",
            )
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
                    text = card.holderName,
                    textAlign = TextAlign.Left,
                    style = TextStyle(

                        fontFamily = NunitoFontFamily(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    ),
                    color = black
                )
                Text(
                    text = "........${card.lastFourDigit}",
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
            CircleSelector(
                isSelected = isSelected
            )

        }
        Spacer(
            modifier = Modifier.height(smallPadding)
        )
        AnimatedVisibility(
            visible = showCVV
        ) {
            Row(
                modifier = modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(width = 57.dp, height = 39.dp),
                )
                Spacer(
                    modifier = Modifier.width(mediumPadding)
                )

                AnimatedVisibility(
                    visible = showCVV
                ) {
                    CvvTextBox(
                        cvvText = cvvText,
                        openKeyboard = openKeyBoard

                    )
                }
                Spacer(
                    modifier = Modifier.weight(1f)
                )
                AnimatedVisibility(
                    visible = cvvText?.length == 3
                ) {
                    ActionButton(
                        modifier = Modifier
                            .clip(RoundedCornerShape(5.dp))
                            .sizeIn(
                                maxHeight = 35.dp,
                                maxWidth = 77.dp
                            ),
                        text = stringResource(Res.string.verify_cvv),
                        onClick = verifyCvv
                    )
                }
            }
        }
        Spacer(
            modifier = Modifier.height(5.dp)
        )
        HorizontalDivider(
            color = dividerColor,
            thickness = 1.dp
        )

    }
}