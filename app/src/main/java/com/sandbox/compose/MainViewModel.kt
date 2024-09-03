package com.sandbox.compose

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@Immutable
data class UiState(
    val restlessUpdatesActive: Boolean = false,
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

    private val restlessRepository = RestlessRepository()
    private val sluggishRepository = SluggishRepository()

    private val mutableUiState = MutableStateFlow(UiState())
    val uiState: Flow<UiState> = mutableUiState

    init {
        viewModelScope.launch {
            restlessRepository.restlessUpdatesFlow.collect { restlessUpdate ->
                mutableUiState.update { it.copy(restlessUpdate = restlessUpdate) }
            }
        }
    }

    @Stable
    fun onUiAction(action: UiAction) {
        when (action) {
            is UiAction.MakeSluggishRequest -> {
                viewModelScope.launch {
                    mutableUiState.update { it.copy(sluggishResponse = null) }
                    val response =
                        sluggishRepository.asyncOperation(mutableUiState.value.editableValue)
                    mutableUiState.update { it.copy(sluggishResponse = response) }
                }
            }

            is UiAction.UpdateEditableValue -> mutableUiState.update {
                it.copy(editableValue = action.value)
            }
        }
    }
}