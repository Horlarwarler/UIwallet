package com.crezent.finalyearproject.data

import com.crezent.finalyearproject.data.dto.*
import com.crezent.finalyearproject.domain.util.RemoteError
import com.crezent.finalyearproject.domain.util.Result

interface OpayApi {
    suspend fun createWallet(
        opayMerchantId: String,
        name: String,
        email: String,
        phone: String,
        accountType: String
    ): Result<OpayResponse<AccountCreationData>, RemoteError>

    suspend fun getWalletDetails(
        opayMerchantId: String,
        depositCode: String
    ): Result<OpayResponse<WalletDetailsData>, RemoteError>

    suspend fun getTransactionHistory(
        opayMerchantId: String,
        depositCodes: List<String>?,
        pageNo: Long,
        pageSize: Long,
        startTime: Long?,
        endTime: Long?
    ): Result<OpayResponse<OpayTransactionData>, RemoteError>

    suspend fun getWalletBalance(
        opayMerchantId: String,
        depositCode: String
    ): Result<OpayResponse<WalletBalanceData>, RemoteError>

    suspend fun transferToSchoolAccount(
        amount: String,
        depositCode: String,
        opayMerchantId: String,
        requestSerialNo: String,
        description: String
    ): Result<OpayResponse<SweepResponseData>, RemoteError>

    suspend fun getTransferToSchoolStatus(
        opayMerchantId: String,
        orderNo: String,
        requestSerialNo: String,
    ): Result<OpayResponse<SweepStatusData>, RemoteError>

    suspend fun createCardPayment(
        authorization: String,
        merchantId: String,
        paymentRequest: CardPaymentRequestDto
    ): Result<OpayResponse<CardPaymentData>, RemoteError>


}

//"amount": "123",
//"depositCode": "6126390625",
//"opayMerchantId":"256621072768092",
//"collectionMerchantId": "256621072768092",
//"requestSerialNo": "2333232322323233",
//"description": "message"