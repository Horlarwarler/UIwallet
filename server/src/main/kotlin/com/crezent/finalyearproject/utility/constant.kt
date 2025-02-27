package com.crezent.finalyearproject.utility

object Constant {

    private const val DEPOSIT_BASE_URL = "https://payapi.opayweb.com/api/v2/third/depositcode/"
    const val KEY_PAIR_ALGORITHM = "EC"
    const val RESET_PASSWORD_TEMPLATE_ID = "zr6ke4n7m89gon12"
    const val VERIFY_EMAIL_TEMPLATE_ID = "zr6ke4n7mr9gon12"
    const val RESET_PASSWORD_SUBJECT = "Please Reset Your Password"
    const val VERIFY_EMAIL_SUBJECT = "Please Verify Your Email"
    const val OPAY_BASE_API = "https://payapi.opayweb.com"
    const val WALLET_DETAILS_URL = "${DEPOSIT_BASE_URL}queryStaticDepositCodeInfo"
    const val CREATE_DETAILS_URL = "${DEPOSIT_BASE_URL}generateStaticDepositCode"
    const val TRANSACTION_HISTORY = "${DEPOSIT_BASE_URL}queryStaticDepositCodeTransList"
    const val WALLET_BALANCE_URL = "${DEPOSIT_BASE_URL}queryWalletBalance"
    const val SWEEP_TRANSFER_URL = "${DEPOSIT_BASE_URL}transferToMerchant"
    const val SWEEP_STATUS_URL = "${DEPOSIT_BASE_URL}queryTransferStatus"
    const val OPAY_PUBLIC_KEY = "OPAYPUB17383443013040.8590803721492223"

    private const val PAY_STACK_BASE_API = "https://api.paystack.co"
    const val INITIALIZE_TRANSACTION_PATH = "$PAY_STACK_BASE_API/transaction/initialize"
    const val VERIFY_TRANSACTION = "$PAY_STACK_BASE_API/transaction/verify"
    const val PAY_STACK_TRANSACTION = "/transaction"


}