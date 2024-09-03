package com.sandbox.compose

import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit

class SluggishRepository {
    suspend fun asyncOperation(name: String): String {
        delay(5000)
        return "Hello $name!"
    }
}