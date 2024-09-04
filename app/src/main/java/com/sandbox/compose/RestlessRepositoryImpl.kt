package com.sandbox.compose

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RestlessRepositoryImpl : RestlessRepository{
    override val restlessUpdatesFlow: Flow<Int> = flow {
        var update = 0
        while (true) {
            emit(update++)
            delay(20)
        }
    }
}