package com.feup.jtp.checkout_terminal.presentation

import android.os.CountDownTimer
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FailureScreen(onTimeout: () -> Unit) {
    var timerStarted by remember { mutableStateOf(false) }

    // Start the timer only once when the screen appears
    LaunchedEffect(Unit) {
        if (!timerStarted) {
            timerStarted = true
            object : CountDownTimer(20_000, 1000) {
                override fun onTick(millisUntilFinished: Long) {}
                override fun onFinish() {
                    onTimeout()
                }
            }.start()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF44336)), // Red background
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "The transaction failed.",
                fontSize = 24.sp,
                color = Color.White // White text for readability
            )
        }
    }
}
