package com.sandbox.compose

import kotlinx.coroutines.flow.Flow

interface SluggishProcessor {
    val updatesFlow: Flow<String?>
    suspend fun request(value: Float)
}