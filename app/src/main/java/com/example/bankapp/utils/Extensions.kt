package com.example.bankapp.utils

import org.json.JSONObject
import java.lang.Exception
import java.text.NumberFormat
import java.util.*

fun Double.toCurrency(): String {
    return "$ ".plus(NumberFormat.getNumberInstance(Locale.US).format(this))
}

fun String.isJson(): Boolean {
    return try {
        JSONObject(this)
        true
    } catch (e: Exception) {
        false
    }
}

fun String.toJson(): JSONObject {
    return JSONObject(this)
}

fun JSONObject.hasProperties(keys: List<String>): Boolean {
    keys.forEach { if (!this.has(it)) return false }
    return true
}

fun String.isNumber(): Boolean {
    return this.matches("-?\\d+(\\.\\d+)?".toRegex())
}