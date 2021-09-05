package com.example.bankapp.utils

import java.text.NumberFormat
import java.util.*

fun Double.toCurrency(): String {
    return "$ ".plus(NumberFormat.getNumberInstance(Locale.US).format(this))
}