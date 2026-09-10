package com.example.strovo.data.services.intervals

import android.util.Base64

fun basicAuthHeader(password: String): String {
    val credentials = "API_KEY:$password"
    return "Basic " + Base64.encodeToString(credentials.toByteArray(), Base64.NO_WRAP)
}