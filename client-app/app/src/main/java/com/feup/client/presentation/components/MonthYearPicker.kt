package com.feup.client.presentation.components

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MonthYearPicker(
    visible: Boolean,
    currentMonth: Int,
    currentYear: Int,
    onConfirm: (Int, Int) -> Unit,
    onCancel: () -> Unit,
    primaryColor: Color = MaterialTheme.colorScheme.primary,
    highlightColor: Color = MaterialTheme.colorScheme.secondary,
    dialogShape: androidx.compose.foundation.shape.CornerBasedShape = MaterialTheme.shapes.large,
) {
    if (!visible) return

    val months = listOf(
        "JAN", "FEB", "MAR", "APR", "MAY", "JUN",
        "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"
    )

    var selectedMonth by remember { mutableIntStateOf(currentMonth) }
    var selectedYear by remember { mutableIntStateOf(currentYear) }

    AlertDialog(
        onDismissRequest = onCancel,
        shape = dialogShape,
        containerColor = MaterialTheme.colorScheme.surface,
        title = { Text("Pick date") },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Year selection Row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(onClick = { selectedYear-- }) {
                        Icon(
                            imageVector = Icons.Rounded.KeyboardArrowDown,
                            contentDescription = "Previous year",
                            modifier = Modifier.rotate(90f)
                        )
                    }
                    Text(
                        text = selectedYear.toString(),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                    IconButton(onClick = { selectedYear++ }) {
                        Icon(
                            imageVector = Icons.Rounded.KeyboardArrowDown,
                            contentDescription = "Next year",
                            modifier = Modifier.rotate(-90f)
                        )
                    }
                }

                // Month selection
                Spacer(Modifier.height(24.dp))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalArrangement = Arrangement.Center,
                    maxItemsInEachRow = 4,
                    itemVerticalAlignment = Alignment.CenterVertically
                ) {
                    months.forEachIndexed { index, monthName ->
                        val isSelected = selectedMonth == index
                        val animatedSize by animateDpAsState(
                            targetValue = if (isSelected) 56.dp else 0.dp,
                            animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing),
                            label = "monthCircleAnim"
                        )

                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(56.dp)
                                .clickable {
                                    selectedMonth = index
                                }
                        ) {
                            if (isSelected) {
                                Box(
                                    modifier = Modifier
                                        .size(animatedSize)
                                        .background(
                                            color = primaryColor,
                                            shape = CircleShape
                                        )
                                )
                            }
                            Text(
                                text = monthName,
                                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(selectedMonth, selectedYear)
                },
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
            ) {
                Text("OK", color = MaterialTheme.colorScheme.onPrimary, fontSize = 18.sp)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onCancel,
                shape = CircleShape
            ) {
                Text("Cancel", fontSize = 18.sp)
            }
        }
    )
}
