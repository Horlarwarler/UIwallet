package com.crezent.finalyearproject.core.presentation.util

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun String.formatReadableDate(): String {
    val instant = Instant.parse(this)
    val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    return "${
        localDateTime.month.name.lowercase().replaceFirstChar { it.uppercase() }.take(3)
    } ${localDateTime.dayOfMonth}, ${localDateTime.year}, " +
            "${localDateTime.hour.toString().padStart(2, '0')}:${
                localDateTime.minute.toString().padStart(2, '0')
            }:${localDateTime.second.toString().padStart(2, '0')}"
}