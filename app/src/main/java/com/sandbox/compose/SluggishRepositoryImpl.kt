package com.sandbox.compose

import kotlinx.coroutines.delay

class SluggishRepositoryImpl: SluggishRepository {
    override suspend fun asyncOperation(value: Float): String {
        delay(5000)
        return "$value * 2 = ${value * 2}"
    }
}