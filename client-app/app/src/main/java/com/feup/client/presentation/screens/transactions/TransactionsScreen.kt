package com.feup.client.presentation.screens.transactions

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun TransactionsScreen(viewModel: TransactionsViewModel = hiltViewModel()) {
    val state = viewModel.uiState.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(state.value.error) {
        state.value.error?.let { errorMsg ->
            coroutineScope.launch {
                snackbarHostState.showSnackbar(errorMsg)
                viewModel.clearError()
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->

        Column(
            modifier = Modifier.fillMaxSize().padding(padding),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                if (state.value.transactions.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxWidth().weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No transactions found.")
                    }
                } else {

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        items(
                            state.value.transactions,
                            key = { it.date }
                        ) { transaction ->

                            var expanded by remember { mutableStateOf(false) }
                            val rotation by animateFloatAsState(targetValue = if (expanded) 180f else 0f)

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable { expanded = !expanded }
                                    .animateContentSize(),
                                shape = RoundedCornerShape(12.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                ) {

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {

                                            Text(
                                                text = formatDate(transaction.date),
                                                style = MaterialTheme.typography.titleSmall,
                                                color = Color.Gray
                                            )

                                            Text(
                                                text = "Total: %.2f€".format(transaction.price),
                                                style = MaterialTheme.typography.titleMedium
                                            )
                                        }

                                        Icon(
                                            imageVector = Icons.Default.ExpandMore,
                                            contentDescription = if (expanded) "Collapse" else "Expand",
                                            modifier = Modifier.size(24.dp).rotate(rotation)
                                        )
                                    }

                                    if (expanded) {
                                        Spacer(modifier = Modifier.height(8.dp))

                                        if (transaction.discount > 0.0) {
                                            Text(
                                                text = "Discount: ${transaction.discount}%",
                                                style = MaterialTheme.typography.bodySmall
                                            )
                                        }

                                        transaction.voucherUsed?.let { voucher ->
                                            Text(
                                                text = "Voucher used: ${voucher.voucherUuid}",
                                                style = MaterialTheme.typography.bodySmall
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(8.dp))

                                        transaction.products.forEach { product ->
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(vertical = 2.dp),
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Text(
                                                    text = product.name,
                                                    style = MaterialTheme.typography.bodyMedium
                                                )
                                                Text(
                                                    text = "x${product.quantity}",
                                                    style = MaterialTheme.typography.bodyMedium
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }

                Button(onClick = { viewModel.updateTransactions() }) {
                    Text("Load transactions")
                }
            }
        }
    }
}

fun formatDate(dateString: String): String {
    return try {
        val parsedDate = LocalDateTime.parse(dateString)
        parsedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
    } catch (e: Exception) {
        dateString // fallback to raw if parsing fails
    }
}
