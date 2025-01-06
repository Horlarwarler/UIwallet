package com.crezent.finalyearproject.onboard.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.crezent.finalyearproject.ui.theme.NunitoFontFamily
import com.crezent.finalyearproject.ui.theme.black
import com.crezent.finalyearproject.ui.theme.largePadding
import com.crezent.finalyearproject.ui.theme.mediumPadding
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable

fun OnboardingHeader(
    title: String,
    description: String,
    imageHeader: DrawableResource
) {
    Column(
    ) {
        Box(
            modifier = Modifier

                .weight(0.6f)
                .heightIn(max = 461.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                modifier = Modifier.padding(top = mediumPadding),
                painter = painterResource(imageHeader),
                contentDescription = "title",
                contentScale = ContentScale.Fit
            )
        }

        Spacer(Modifier.height(mediumPadding))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.4f)
        ) {
            val boldText = title.split(" ").first()
            val remainingText = title.split(" ").drop(1).joinToString(" ")
            val buildAnnotatedString = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = black,
                        fontFamily = NunitoFontFamily(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 36.sp
                    )
                ) {
                    append(boldText)
                }

                withStyle(
                    style = SpanStyle(
                        color = black,
                        fontFamily = NunitoFontFamily(),
                        fontWeight = FontWeight.Normal,
                        fontSize = 36.sp
                    )
                ) {
                    append(" ")
                    append(remainingText)
                }
            }
            Text(

                // .weight(0.21f)
                text = buildAnnotatedString,
                textAlign = TextAlign.Left,
                lineHeight = 50.sp
            )
            Spacer(Modifier.height(mediumPadding))
            Text(
                text = description,
                textAlign = TextAlign.Left,
                style = TextStyle(
                    lineHeight = 30.sp,
                    fontFamily = NunitoFontFamily(),
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp
                )
            )
        }


    }


}