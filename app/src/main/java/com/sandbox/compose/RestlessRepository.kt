package com.sandbox.compose

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RestlessRepository {
    val restlessUpdatesFlow: Flow<Int> = flow<Int> {
        var update = 0
        while (true) {
            emit(update++)
            delay(20)
        }
    }
}