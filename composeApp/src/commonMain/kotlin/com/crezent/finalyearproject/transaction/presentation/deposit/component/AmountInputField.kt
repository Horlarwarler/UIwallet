package com.crezent.finalyearproject.transaction.presentation.deposit.component

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.crezent.finalyearproject.ui.theme.NunitoFontFamily
import com.crezent.finalyearproject.ui.theme.black
import com.crezent.finalyearproject.ui.theme.dividerColor
import com.crezent.finalyearproject.ui.theme.grey
import finalyearproject.composeapp.generated.resources.Res
import finalyearproject.composeapp.generated.resources.enter_amount
import finalyearproject.composeapp.generated.resources.new_to_ui
import org.jetbrains.compose.resources.stringResource

@Composable
fun AmountInputField(
    currentIndex: Int = 0,
    onItemClick: (Int?) -> Unit,
    modifier: Modifier,
    amount: String? = null
) {

    Column(
        modifier = modifier
            .padding(horizontal = 50.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (amount.isNullOrBlank()) {
            Text(
                modifier = Modifier
                    .clickable {
                        onItemClick(null)
                    },
                text = stringResource(Res.string.enter_amount),
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 24.sp,
                    lineHeight = 52.5.sp,
                    fontFamily = NunitoFontFamily(),
                    fontWeight = FontWeight(200),
                    color = black,
                    letterSpacing = 2.sp,
                )
            )
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                repeat(amount.length) {
                    InputBox(
                        currentIndex = it == currentIndex,
                        input = amount[it].toString(),
                        onClick = {
                            println("Index is $it")
                            onItemClick(it)
                        }
                    )
                }
            }
        }
        Spacer(
            modifier = Modifier.height(5.dp)
        )
        Box(
            modifier = Modifier
                .width(244.dp)
                .height(1.dp)
                .background(grey)
        )

    }
}

@Composable
private fun InputBox(
    currentIndex: Boolean,
    input: String,
    onClick: () -> Unit
) {

    val infiniteTransition = rememberInfiniteTransition()
    var isAnimated by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 =  currentIndex) {
        if (currentIndex){
            isAnimated = true
        }
    }

    val color by infiniteTransition.animateFloat(
        initialValue = if (isAnimated) 0.1f else 1f,
        targetValue = if (isAnimated) 1f else 0f,
        infiniteRepeatable(animation = tween(1000), repeatMode = RepeatMode.Restart)
    )
    Box(
        modifier = Modifier
            .clickable {
                onClick()
            }
            .width(IntrinsicSize.Min)
            .height(IntrinsicSize.Min)
    ) {
        Text(
            modifier = Modifier

                .align(alignment = Alignment.CenterStart),
            text = input,
            textAlign = TextAlign.Center,
            style = TextStyle(
                fontSize = 30.sp,
                lineHeight = 30.sp,
                fontFamily = NunitoFontFamily(),
                fontWeight = FontWeight.Bold,
                color = black,
                letterSpacing = 2.sp,
            )
        )
        if (currentIndex) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .clip(RoundedCornerShape(10.dp))
                    .background(black.copy(alpha = color))
                    .height(25.dp)
                    .width(1.dp)
            )
        }

    }
}