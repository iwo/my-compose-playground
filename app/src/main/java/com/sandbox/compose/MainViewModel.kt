package com.sandbox.compose

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
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

    // In the real app this would be injected by DI
    private val restlessRepository: RestlessRepository = RestlessRepositoryImpl()
    private val sluggishProcessor:SluggishProcessor = SluggishProcessorImpl()

    private val editableValueFlow = MutableStateFlow(0f)

    val uiState: Flow<UiState> = combine(
        restlessRepository.restlessUpdatesFlow,
        sluggishProcessor.updatesFlow,
        editableValueFlow,
    ) { relentlessUpdate, sluggishResponse, editableValue ->
        UiState(
            restlessUpdate = relentlessUpdate,
            editableValue = editableValue,
            sluggishResponse = sluggishResponse,
        )
    }.stateIn(scope = viewModelScope, started = SharingStarted.Lazily, initialValue = UiState())

    @Stable
    fun onUiAction(action: UiAction) {
        when (action) {
            is UiAction.MakeSluggishRequest -> viewModelScope.launch {
                sluggishProcessor.request(editableValueFlow.value)
            }

            is UiAction.UpdateEditableValue -> editableValueFlow.value = action.value
        }
    }
}