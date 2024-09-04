package com.sandbox.compose

import kotlinx.coroutines.flow.Flow

interface RestlessRepository {
    val restlessUpdatesFlow: Flow<Int>
}