package com.crezent.finalyearproject.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import finalyearproject.composeapp.generated.resources.Res
import finalyearproject.composeapp.generated.resources.nunito__bold
import finalyearproject.composeapp.generated.resources.nunito_medium
import finalyearproject.composeapp.generated.resources.nunito_semibold
import org.jetbrains.compose.resources.Font

@Composable
fun NunitoFontFamily() = FontFamily(
    Font(Res.font.nunito_medium, weight = FontWeight.Medium),
    Font(Res.font.nunito_semibold, weight = FontWeight.SemiBold),
    Font(Res.font.nunito__bold, weight = FontWeight.Bold)

)