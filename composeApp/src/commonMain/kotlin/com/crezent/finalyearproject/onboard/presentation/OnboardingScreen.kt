package com.crezent.finalyearproject.onboard.presentation

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.crezent.finalyearproject.app.ScreenNavigation
import com.crezent.finalyearproject.onboard.presentation.component.OnboardingBottom
import com.crezent.finalyearproject.onboard.presentation.component.OnboardingHeader
import com.crezent.finalyearproject.ui.theme.background
import com.crezent.finalyearproject.ui.theme.largePadding
import com.crezent.finalyearproject.ui.theme.mediumPadding
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun OnboardScreenRoot(
    screenNavigation: ScreenNavigation
) {
    val onboardViewmodel: OnboardViewmodel = koinViewModel()
    OnboardScreen(
        navigateToSignUp = {
            onboardViewmodel.saveOnboarding()
            screenNavigation.navigateToSignUp()
        }
    )

}

@Composable
fun OnboardScreen(
    navigateToSignUp: () -> Unit
) {
    val horizontalPagerState = rememberPagerState(initialPage = 1, pageCount = {
        3
    })

    var currentIndex by remember {
        mutableIntStateOf(0)
    }

    val onboardList by remember {
        mutableStateOf(OnboardModel.entries.toList())
    }
    LaunchedEffect(currentIndex) {
        if (currentIndex != horizontalPagerState.currentPage) {
            horizontalPagerState.animateScrollToPage(
                page = currentIndex,
                animationSpec = tween(

                )
            )
        }
    }
    Column(
        modifier = Modifier
            .background(background)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        HorizontalPager(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = largePadding)
                .weight(0.8f),
            state = horizontalPagerState,
            userScrollEnabled = false,

            ) {
            val currentOnboard = onboardList[currentIndex]
            val title = stringResource(currentOnboard.title)
            val description = stringResource(currentOnboard.description)
            val imageHeader = currentOnboard.image

            OnboardingHeader(
                title = title,
                description = description,
                imageHeader = imageHeader
            )
        }


        Box(
            modifier = Modifier
                .navigationBarsPadding()
                .padding(bottom = mediumPadding)
                .weight(0.2f),
            contentAlignment = Alignment.BottomCenter
        ) {
            OnboardingBottom(
                modifier = Modifier,
                onPreviousClick = {
                    currentIndex -= 1
                },
                onNextClick = {
                    currentIndex += 1
                },
                getStartedClick = navigateToSignUp,
                currentIndex = currentIndex
            )

        }
        // Spacer(Modifier.weight(1f))
    }


}