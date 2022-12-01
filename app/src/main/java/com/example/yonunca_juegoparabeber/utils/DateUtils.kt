package com.example.yonunca_juegoparabeber.utils

import java.util.*

fun getYesterday(): Date {
    val cal = Calendar.getInstance()
    cal.add(Calendar.DATE, -1)
    return cal.time
}