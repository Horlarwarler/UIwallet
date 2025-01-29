package com.crezent.finalyearproject.core.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.platform.ViewConfiguration
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.PlatformSpanStyle
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.crezent.finalyearproject.ui.theme.NunitoFontFamily
import com.crezent.finalyearproject.ui.theme.background
import com.crezent.finalyearproject.ui.theme.black
import com.crezent.finalyearproject.ui.theme.colorWhite
import com.crezent.finalyearproject.ui.theme.dividerColor
import com.crezent.finalyearproject.ui.theme.errorColor
import com.crezent.finalyearproject.ui.theme.grey
import com.crezent.finalyearproject.ui.theme.largePadding
import com.crezent.finalyearproject.ui.theme.mediumPadding
import com.crezent.finalyearproject.ui.theme.otpBackground
import com.crezent.finalyearproject.ui.theme.smallPadding
import finalyearproject.composeapp.generated.resources.Res
import finalyearproject.composeapp.generated.resources.arrow
import finalyearproject.composeapp.generated.resources.baseline_backspace_24
import finalyearproject.composeapp.generated.resources.delete
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalLayoutApi::class)
//@OptIn(ExperimentalTextApi::class)
@Composable
fun NumberInputDialog(
    isVisible: Boolean = true,
    modifier: Modifier,
    onClick: (NumberInputType) -> Unit
) {


    val numberKeyboards by remember {
        val special =
            listOf(NumberInputType.Dot, NumberInputType.Number(0), NumberInputType.BackSpace)
        val number: List<NumberInputType> = (1..9).map {
            NumberInputType.Number(it)
        }
        mutableStateOf(number + special)
    }




    AnimatedVisibility(
        visible = isVisible,
        modifier = modifier
    ) {
        FlowRow(
            modifier = Modifier
                .padding(bottom = largePadding, start = 50.dp, end = 50.dp, top = mediumPadding)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalArrangement = Arrangement.spacedBy(20.dp),
            maxItemsInEachRow = 3,
        ) {
            repeat(numberKeyboards.size) {
                val input = numberKeyboards[it]
                NumberInput(
                    numberInputType = input,
                    modifier = Modifier.clickable {
                        onClick(input)
                    }
                )
            }
        }


    }

}

sealed interface NumberInputType {

    data object BackSpace : NumberInputType

    data object Dot : NumberInputType

    data class Number(val number: Int) : NumberInputType
}

@Composable
private fun NumberInput(
    numberInputType: NumberInputType,
    modifier: Modifier
) {


    Box(
        modifier = modifier
            .size(
                50.dp
            ),
        contentAlignment = Alignment.Center
    ) {
        when (numberInputType) {
            NumberInputType.BackSpace -> {
                Icon(
                    ///modifier = Modifier.rotate(180f),

                    painter = painterResource(Res.drawable.baseline_backspace_24),
                    contentDescription = stringResource(Res.string.delete)
                )

            }

            NumberInputType.Dot -> {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(black)
                        .size(5.dp)
                )

            }

            is NumberInputType.Number -> {
                Text(
                    modifier = Modifier,
                    text = "${numberInputType.number}",
                    style = TextStyle(
                        fontSize = 30.sp,
                        lineHeight = 30.sp,
                        fontFamily = NunitoFontFamily(),
                        fontWeight = FontWeight.SemiBold,
                        color = black,
                        letterSpacing = 2.sp,
                        lineHeightStyle = LineHeightStyle(
                            trim = LineHeightStyle.Trim.Both,
                            alignment = LineHeightStyle.Alignment.Center
                        ),
                    ),
                    textAlign = TextAlign.Center
                )
            }

        }

    }

}

