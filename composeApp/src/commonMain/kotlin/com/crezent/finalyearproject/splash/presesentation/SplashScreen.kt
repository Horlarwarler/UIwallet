package com.crezent.finalyearproject.splash.presesentation

import KottieAnimation
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.crezent.finalyearproject.app.ScreenNavigation
import com.crezent.finalyearproject.core.data.security.encryption.KeyPairGenerator
import com.crezent.finalyearproject.core.domain.util.Animations
import com.crezent.finalyearproject.ui.theme.NunitoFontFamily
import com.crezent.finalyearproject.ui.theme.background
import com.crezent.finalyearproject.ui.theme.black
import com.crezent.finalyearproject.ui.theme.largePadding
import com.crezent.finalyearproject.ui.theme.smallPadding
import finalyearproject.composeapp.generated.resources.Res
import finalyearproject.composeapp.generated.resources.app_name
import finalyearproject.composeapp.generated.resources.right_reserved
import finalyearproject.composeapp.generated.resources.ui_logo
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.onEach
import kottieComposition.KottieCompositionSpec
import kottieComposition.animateKottieCompositionAsState
import kottieComposition.rememberKottieComposition
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SplashScreenRoot(
    screenNavigation: ScreenNavigation
) {

    val splashViewModel: SplashViewModel = koinViewModel()
    val state = splashViewModel.splashScreenState.collectAsStateWithLifecycle().value

    LaunchedEffect(Unit){
        splashViewModel.channel.consumeEach {
                splashScreenEvent ->
            println("Received data $splashScreenEvent")
            when (splashScreenEvent) {
                SplashScreenEvent.NavigateToOnboard -> {
                    println("Navigate to onboard")
                    screenNavigation.navigateToOnboard()
                }
                SplashScreenEvent.NavigateToPinScreen -> screenNavigation.navigateToPin()
                SplashScreenEvent.NavigateToSignInScreen -> screenNavigation.navigateToSignIn()
                is SplashScreenEvent.SendErrorMessage -> Unit
            }

        }
    }
    splashViewModel.channel.consumeAsFlow().onEach { splashScreenEvent ->

        println("Received Consume data $splashScreenEvent")

    }
    SplashScreen(
        splashScreenState = state,
    )


}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun SplashScreen(
    splashScreenState: SplashScreenState = SplashScreenState()
) {

    var splashLoadingAnimation by remember {
        mutableStateOf("")
    }

    var playing by remember {
        mutableStateOf(false)
    }


    LaunchedEffect(key1 = Unit) {

        playing = splashScreenState.isLoading
        splashLoadingAnimation =
            Res.readBytes(Animations.LOADING_REPLACE).decodeToString()
    }

    val kottieComposition = rememberKottieComposition(
        spec = KottieCompositionSpec.JsonString(splashLoadingAnimation)
    )

    val animationState by animateKottieCompositionAsState(
        composition = kottieComposition,
        isPlaying = playing,
        iterations = Int.MAX_VALUE
    )


    Box(
        modifier = Modifier
            .background(background)
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                modifier = Modifier.size(100.dp),
                painter = painterResource(resource = Res.drawable.ui_logo),
                contentDescription = "UI_LOGO",
                contentScale = ContentScale.Fit
            )

            Text(
                text = stringResource(Res.string.app_name),
                fontSize = 30.sp,
                color = black,
                fontFamily = NunitoFontFamily(),
                fontWeight = FontWeight.Medium
            )
        }

        Column(
            modifier = Modifier
                .navigationBarsPadding()
                .padding(bottom = largePadding)
                .align(Alignment.BottomCenter)

                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(smallPadding)
        ) {
            KottieAnimation(
                composition = kottieComposition,
                progress = {
                    animationState.progress
                },
                modifier = Modifier.size(90.dp),
                contentScale = contentScale.ContentScale.Fit
            )

            Text(
                text = stringResource(Res.string.right_reserved),
                fontSize = 13.sp,
                color = black,
                fontFamily = NunitoFontFamily(),
                fontWeight = FontWeight.SemiBold
            )
        }
        //Animation


    }
}