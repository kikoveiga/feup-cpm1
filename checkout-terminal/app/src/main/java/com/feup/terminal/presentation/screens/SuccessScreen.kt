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
import com.feup.terminal.data.dto.TransactionFromServerDto

@Composable
fun SuccessScreen(
    transactionResult: TransactionFromServerDto,
    onTimeout: () -> Unit
) {
    var timerStarted by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (!timerStarted) {
            timerStarted = true
            object : CountDownTimer(10_000, 1000) {
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
            .background(if (transactionResult.isSuccess) Color(0xFF4CAF50) else Color(0xFFF44336)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = if (transactionResult.isSuccess) "Transaction Successful!" else "Transaction Failed!",
                fontSize = 24.sp,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Total Paid: €${transactionResult.totalPaid.setScale(2)}",
                fontSize = 18.sp,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Accumulated Discount: €${transactionResult.totalAccDiscount.setScale(2)}",
                fontSize = 18.sp,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))

            transactionResult.message?.let { message ->
                Text(
                    text = message,
                    fontSize = 16.sp,
                    color = Color.White
                )
            }
        }
    }
}
