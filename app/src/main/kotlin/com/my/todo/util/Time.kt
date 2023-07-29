package com.my.todo.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Long.toDateString(pattern: String = DEFAULT_PATTERN): String {
    val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())
    return dateFormat.format(Date(this))
}

fun String.toMillis(pattern: String = DEFAULT_PATTERN): Long {
    return SimpleDateFormat(pattern, Locale.getDefault()).parse(this)?.time ?: 0
}

const val DEFAULT_PATTERN = "dd MMM yyyy, HH:MM"