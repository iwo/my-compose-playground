package com.sandbox.compose

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart

class SluggishProcessor {
    private val sluggishRepository = SluggishRepository()

    private val sluggishRequestFlow = MutableSharedFlow<Float>()

    @OptIn(ExperimentalCoroutinesApi::class)
    val updatesFlow: Flow<String?> = sluggishRequestFlow.flatMapLatest {
        flow {
            emit(null)
            val response = sluggishRepository.asyncOperation(it)
            emit(response)
        }
    }.onStart { emit(null) }

    suspend fun request(value: Float) {
        sluggishRequestFlow.emit(value)
    }
}