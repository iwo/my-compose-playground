package com.sandbox.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.sandbox.compose.ui.theme.ComposeSandboxTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val stateFlow = MutableStateFlow(UiState(title = Title("Title"), counter = 1))

        lifecycleScope.launch {
            while (true) {
                delay(20L)
                stateFlow.value = stateFlow.value.let { it.copy(counter = it.counter + 1) }
            }
        }
        setContent {
            val state by stateFlow.collectAsState()
            ComposeSandboxTheme {
                ScreenContent(state)
            }
        }
    }
}

data class UiState(
    val title: Title,
    val counter: Int,
)

data class Title(val text: String, val externalData: ExternalData = ExternalData(""))

class ExternalData(var data: String)

@Composable
fun ScreenContent(state: UiState) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            TitleHeader(title = state.title)
            CounterContent(counter = state.counter)
        }
    }
}

@Composable
fun TitleHeader(title: Title) {
    Text(text = title.text)
}

@Composable
fun CounterContent(counter: Int) {
    Text(text = "Counter value $counter")
}