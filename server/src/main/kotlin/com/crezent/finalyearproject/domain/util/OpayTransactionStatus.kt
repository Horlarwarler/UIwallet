package com.crezent.finalyearproject.domain.util

sealed interface OpayTransactionStatus {

    data object Success : OpayTransactionStatus

    data object Fail : OpayTransactionStatus

    data object Close : OpayTransactionStatus

    data object CANCEL : OpayTransactionStatus

    data object Pending : OpayTransactionStatus
}