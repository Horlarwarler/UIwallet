package com.crezent.finalyearproject.onboard.presentation

import finalyearproject.composeapp.generated.resources.Res
import finalyearproject.composeapp.generated.resources.card_onboarding
import finalyearproject.composeapp.generated.resources.first_onboarding
import finalyearproject.composeapp.generated.resources.onboard_description_one
import finalyearproject.composeapp.generated.resources.onboard_description_three
import finalyearproject.composeapp.generated.resources.onboard_description_two
import finalyearproject.composeapp.generated.resources.onboard_title_one
import finalyearproject.composeapp.generated.resources.onboard_title_three
import finalyearproject.composeapp.generated.resources.onboard_title_two
import finalyearproject.composeapp.generated.resources.second_onboarding
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.Resource
import org.jetbrains.compose.resources.StringResource


enum class OnboardModel(
    val title: StringResource, val description: StringResource, val image: DrawableResource
) {
    FirstOnboard(
        title = Res.string.onboard_title_one,
        description = Res.string.onboard_description_one,
        image = Res.drawable.first_onboarding
    ),
    SecondOnboard(
        title = Res.string.onboard_title_two,
        description = Res.string.onboard_description_two,
        image = Res.drawable.second_onboarding
    ),
    ThirdOnboard(
        title = Res.string.onboard_title_three,
        description = Res.string.onboard_description_three,
        image = Res.drawable.card_onboarding
    )

}
