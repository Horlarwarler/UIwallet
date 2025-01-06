package com.crezent.finalyearproject.onboard.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.crezent.finalyearproject.core.presentation.component.ActionButton
import com.crezent.finalyearproject.ui.theme.mediumPadding
import com.crezent.finalyearproject.ui.theme.smallPadding
import finalyearproject.composeapp.generated.resources.Res
import finalyearproject.composeapp.generated.resources.get_started
import org.jetbrains.compose.resources.stringResource

@Composable
fun OnboardingBottom(
    modifier: Modifier,
    onNextClick: () -> Unit,
    onPreviousClick: () -> Unit,
    getStartedClick: () -> Unit,
    currentIndex: Int

) {

    AnimatedVisibility(
        visible = currentIndex < 2
    ) {
        Box(
            modifier = modifier
                .padding(horizontal = smallPadding)
                .fillMaxWidth()
        ) {
            AnimatedVisibility(
                visible = currentIndex > 0
            ) {
                PreviousArrow(
                    modifier = Modifier.align(Alignment.CenterStart),
                    onPreviousClick = onPreviousClick
                )
            }

            NextArrow(
                modifier = Modifier.align(Alignment.CenterEnd),
                onNextClick = onNextClick
            )
            Row(
                modifier = Modifier.align(Alignment.Center),
                horizontalArrangement = Arrangement.spacedBy(smallPadding),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(3) {

                    ProgressIndicator(
                        isCurrent = it == currentIndex
                    )
                }
            }
        }
    }

    AnimatedVisibility(
        visible = currentIndex == 2
    ) {
        ActionButton(
            modifier = Modifier.padding(horizontal = mediumPadding)
                .fillMaxWidth(),
            text = stringResource(Res.string.get_started),
            onClick = getStartedClick,
            height = 70.dp,
            textSize = 24.sp

        )
    }

}