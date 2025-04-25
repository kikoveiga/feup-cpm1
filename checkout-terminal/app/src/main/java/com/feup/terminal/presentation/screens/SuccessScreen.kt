package com.feup.terminal.presentation.screens

import android.os.CountDownTimer
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SuccessScreen(onTimeout: () -> Unit) {
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
            .background(Color(0xFF4CAF50)), // Green background
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "The transaction was a success",
                fontSize = 24.sp,
                color = Color.White // White text for readability
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "The gate should now open",
                fontSize = 20.sp,
                color = Color.White // White text for readability
            )
        }
    }
}
