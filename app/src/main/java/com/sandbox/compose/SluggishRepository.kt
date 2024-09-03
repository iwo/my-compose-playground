package com.sandbox.compose

import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit

class SluggishRepository {
    suspend fun asyncOperation(value: Float): String {
        delay(5000)
        return "$value * 2 = ${value * 2}"
    }
}