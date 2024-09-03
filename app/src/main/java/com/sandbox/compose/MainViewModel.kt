package com.sandbox.compose

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf

@Immutable
data class UiState(
    val restlessUpdate: Int = 0,
    val editableValue: Float = 0f,
    val sluggishResponse: String? = null,
)

sealed interface UiAction {
    @Immutable
    data object MakeSluggishRequest : UiAction

    @Immutable
    data class UpdateEditableValue(val value: Float) : UiAction
}

class MainViewModel : ViewModel() {

    val uiState: Flow<UiState> = flowOf(UiState())

    fun onUiAction(action: UiAction) {
        when (action) {
            is UiAction.MakeSluggishRequest -> TODO()
            is UiAction.UpdateEditableValue -> TODO()
        }
    }
}