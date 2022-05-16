package com.mhamdy.core.extentions

import java.text.SimpleDateFormat
import java.util.*

fun String.getYear(): Int {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    val calendar: Calendar = Calendar.getInstance()
    try {
        calendar.timeInMillis = dateFormat.parse(this).time
    } catch (e: Exception) {
        e.printStackTrace()
        return 0;
    }
    return calendar.get(Calendar.YEAR)
}