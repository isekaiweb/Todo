package com.my.todo.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun Long.toDateString(pattern: String = DEFAULT_PATTERN): String {
    val dateFormat = SimpleDateFormat(pattern, Locale.getDefault()).apply {
        timeZone = TimeZone.getDefault()
    }
    return dateFormat.format(Date(this))
}

fun String.toMillis(pattern: String = DEFAULT_PATTERN): Long {
    return if (this.isEmpty()) 0 else SimpleDateFormat(
        pattern,
        Locale.getDefault()
    ).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }.parse(this)?.time ?: 0
}

const val DEFAULT_PATTERN = "dd MMM yyyy"