package com.sandbox.compose

import android.util.Log
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

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

    private val restlessRepository = RestlessRepository()
    private val sluggishRepository = SluggishRepository()

    private val sluggishRequestFlow = MutableSharedFlow<Float>()
    private val editableValueFlow = MutableStateFlow(0f)

    val uiState: Flow<UiState> = combine(
        restlessRepository.restlessUpdatesFlow,
        getSluggishResponseFlow(),
        editableValueFlow,
    ) { relentlessUpdate, sluggishResponse, editableValue ->
        UiState(
            restlessUpdate = relentlessUpdate,
            editableValue = editableValue,
            sluggishResponse = sluggishResponse,
        )
    }.stateIn(scope = viewModelScope, started = SharingStarted.Lazily, initialValue = UiState())

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun getSluggishResponseFlow() = sluggishRequestFlow.flatMapLatest {
        flow {
            emit(null)
            val response = sluggishRepository.asyncOperation(it)
            emit(response)
        }
    }.onStart { emit(null) }

    @Stable
    fun onUiAction(action: UiAction) {
        when (action) {
            is UiAction.MakeSluggishRequest -> viewModelScope.launch {
                sluggishRequestFlow.emit(value = editableValueFlow.value)
            }

            is UiAction.UpdateEditableValue -> editableValueFlow.value = action.value
        }
    }
}