package com.sandbox.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sandbox.compose.ui.theme.ComposeSandboxTheme
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val viewModel: MainViewModel = viewModel()
            val state by viewModel.uiState.collectAsState(UiState())
            ComposeSandboxTheme {
                ScreenContent(state, onUiAction = viewModel::onUiAction)
            }
        }
    }
}

@Composable
fun ScreenContent(state: UiState, onUiAction: (UiAction) -> Unit) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(modifier = Modifier
            .padding(innerPadding)
            .padding(16.dp)) {
            CounterContent(counter = state.restlessUpdate)
            InputSection(editableValue = state.editableValue, onUiAction = onUiAction)
            SluggishResponseSection(response = state.sluggishResponse)
        }
    }
}


@Composable
fun InputSection(editableValue: Float, onUiAction: (UiAction) -> Unit) {
    Column {
        Slider(value = editableValue, onValueChange = {
            onUiAction(UiAction.UpdateEditableValue(it))
        })
        Button(onClick = { onUiAction(UiAction.MakeSluggishRequest)}) {
            Text(text = "Make a sluggish request")
        }
    }
}

@Composable
fun SluggishResponseSection(response: String?) {
    Row (verticalAlignment = Alignment.CenterVertically){
        Text(text = "Sluggish response:", modifier = Modifier.padding(vertical = 16.dp))
        if (response == null) {
            CircularProgressIndicator()
        } else {
            Text(text = response)
        }
    }
}

@Composable
fun CounterContent(counter: Int) {
    Row {
        Text(text = "Restless updates:")
        Text(text = "$counter")
    }
}