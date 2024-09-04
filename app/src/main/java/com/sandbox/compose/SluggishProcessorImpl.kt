package com.sandbox.compose

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart

class SluggishProcessorImpl: SluggishProcessor {
    private val sluggishRepository: SluggishRepository = SluggishRepositoryImpl()

    private val sluggishRequestFlow = MutableSharedFlow<Float>()

    @OptIn(ExperimentalCoroutinesApi::class)
    override val updatesFlow: Flow<String?> = sluggishRequestFlow.flatMapLatest {
        flow {
            emit(null)
            val response = sluggishRepository.asyncOperation(it)
            emit(response)
        }
    }.onStart { emit(null) }

    override suspend fun request(value: Float) {
        sluggishRequestFlow.emit(value)
    }
}