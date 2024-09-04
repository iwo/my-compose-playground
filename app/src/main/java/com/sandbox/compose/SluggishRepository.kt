package com.sandbox.compose

interface SluggishRepository {
    suspend fun asyncOperation(value: Float): String
}