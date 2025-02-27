package com.crezent.finalyearproject.data.dto

import kotlinx.serialization.Serializable


@Serializable
data class PayStackTransaction(
    val amount: Int,
    val authorization: Authorization,
    val channel: String,
    //  val connect: Any,
    val createdAt: String,
    val created_at: String,
    val currency: String,
    val customer: Customer,
    val domain: String,
    val fees: Int?,
    // val fees_breakdown: Any,
    // val fees_split: Any,
    val gateway_response: String,
    // val helpdesk_link: Any,
    val id: Long,
    val ip_address: String,
    val log: Log,
    val message: String?,
   // val metadata: String,
    val order_id: String?,
    val paidAt: String?,
    val paid_at: String?,
    val receipt_number: String?,
    val reference: String,
    val requested_amount: Int,
    val source: Source?,
    val status: String,
)

@Serializable
data class Authorization(
    val account_name: String?,
    val authorization_code: String,
    val bank: String?,
    val bin: String?,
    val brand: String?,
    val card_type: String?,
    val channel: String?,
    val country_code: String?,
    val exp_month: String?,
    val exp_year: String?,
    val last4: String?,
    val reusable: Boolean?,
    val signature: String?
)

@Serializable

data class Customer(
    val customer_code: String,
    val email: String,
    val first_name: String?,
    val id: Int,
    val international_format_phone: String?,
    val last_name: String?,
    val metadata: Metadata?,
    val phone: String?,
    val risk_action: String,
)

@Serializable

data class Log(
    val attempts: Int,
    val errors: Int,
    val history: List<History>,
  //  val input: List<Any>,
    val mobile: Boolean,
    val start_time: Int,
    val success: Boolean,
    val time_spent: Int
)

@Serializable
data class Source(
    val identifier: String?,
    val source: String,
    val type: String
)


@Serializable
data class Metadata(
    val custom_fields: List<CustomField>
)

@Serializable
data class CustomField(
    val display_name: String,
    val value: String,
    val variable_name: String
)

@Serializable
data class History(
    val message: String,
    val time: Int,
    val type: String
)