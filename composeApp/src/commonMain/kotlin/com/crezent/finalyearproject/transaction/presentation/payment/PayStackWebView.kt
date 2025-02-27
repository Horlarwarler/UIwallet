package com.crezent.finalyearproject.transaction.presentation.payment

import androidx.compose.runtime.Composable

@Composable
expect fun PayStackWebView(
    authorizationUrl: String,
    verifyPayment: () -> Unit,
    closeDialog: () -> Unit,
)