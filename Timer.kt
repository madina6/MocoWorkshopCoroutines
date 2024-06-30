package com.example.mococoroutineexample

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Timer(viewModel: ViewModelTimer) {
    // Hier holen wir die Zustände aus dem ViewModel
    val viewModel: ViewModelTimer = viewModel
    val timeInSeconds by viewModel.timeInSeconds.observeAsState(25 * 60)
    val isRunning by viewModel.isRunning.observeAsState(false)
    val timerColor by viewModel.timerColor.observeAsState(listOf(Color(0xFF4B4A4D), Color(0xFF8C8B8D)))

    // UI Layout für die Timer-Anzeige und Buttons
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFA3A3A3))
    ) {
        Spacer(modifier = Modifier.height(200.dp))
        TimerDisplay(timeInSeconds, isRunning, timerColor, onStart = viewModel::onStart)
        Spacer(modifier = Modifier.height(32.dp))
        StartButton(
            isRunning = isRunning ?: false,
            onStart = viewModel::onStart,
            onPause = viewModel::onPause
        )
    }
}

@Composable
fun TimerDisplay(timeInSeconds: Int?, isRunning: Boolean?, timerColor: List<Color>?, onStart: () -> Unit) {
    // Berechnung der Minuten und Sekunden aus der Gesamtzeit in Sekunden
    val minutes = timeInSeconds?.div(60) ?: 0
    val seconds = timeInSeconds?.rem(60) ?: 0
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(230.dp)
    ) {
        // Anzeige der verbleibenden Zeit
        Text(
            text = "%02d:%02d".format(minutes, seconds),
            color = Color.White,
            fontSize = 60.sp,
            fontWeight = FontWeight.Bold,
        )
        // Start-Icon anzeigen, wenn der Timer nicht läuft
        if (!(isRunning ?: false)) {
            IconButton(onClick = onStart) {
                // Icon (kann ein Start-Symbol hinzufügen)
            }
        }
    }
}

@Composable
fun StartButton(isRunning: Boolean, onStart: () -> Unit, onPause: () -> Unit) {
    // Button, der je nach Zustand des Timers "Start Timer" oder "Pause Timer" anzeigt
    Button(
        onClick = { if (isRunning) onPause() else onStart() },
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF272727)),
        modifier = Modifier
            .width(160.dp)
            .height(100.dp)
            .padding(vertical = 16.dp)
    ) {
        Text(
            text = if (isRunning) "Pause Timer" else "Start Timer",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    // Vorschau der Timer-Komponente mit einem Beispiel-ViewModel
    Timer(viewModel = ViewModelTimer())
}