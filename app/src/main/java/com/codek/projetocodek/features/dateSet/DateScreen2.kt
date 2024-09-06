package com.codek.projetocodek.features.dateSet

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
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
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.app.ComponentActivity
import com.codek.projetocodek.ui.theme.CodekTheme
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.flow.collectLatest
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@SuppressLint("NewApi", "RestrictedApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateScreen2(
    modifier: Modifier = Modifier
) {
    Scaffold(
        Modifier
            .fillMaxSize()
    ) { innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
        ) {
            var openDatePicker by remember { mutableStateOf(false) }
            var dateIni by remember { mutableStateOf("") }
            var dateFim by remember { mutableStateOf("") }
            var selectedTab by remember { mutableStateOf(0) }
            var isConfirmEnabled by remember { mutableStateOf(false) }

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                TextField(
                    dateIni,
                    onValueChange = {},
                    Modifier
                        .weight(1f)
                        .padding(end = 4.dp),
                    label = {
                        Text(text = "Data Inicial")
                    },
                    readOnly = true,

                    interactionSource = remember {
                        MutableInteractionSource()
                    }.also {
                        LaunchedEffect(it) {
                            it.interactions.collectLatest { interaction ->
                                if (interaction is PressInteraction.Release) {
                                    selectedTab = 0
                                    openDatePicker = true
                                }
                            }
                        }
                    }
                )

                TextField(
                    dateFim,
                    onValueChange = {},
                    Modifier
                        .weight(1f)
                        .padding(start = 4.dp),
                    label = {
                        Text(text = "Data Final")
                    },
                    readOnly = true,
                    interactionSource = remember {
                        MutableInteractionSource()
                    }.also {
                        LaunchedEffect(it) {
                            it.interactions.collectLatest { interaction ->
                                if (interaction is PressInteraction.Release) {
                                    selectedTab = 1
                                    openDatePicker = true
                                }
                            }
                        }
                    }
                )
            }

            val dateRangePickerState = rememberDateRangePickerState()
            var startDateMillis by remember { mutableStateOf<Long?>(null) }
            var endDateMillis by remember { mutableStateOf<Long?>(null) }

            LaunchedEffect(dateRangePickerState.selectedStartDateMillis, dateRangePickerState.selectedEndDateMillis) {
                startDateMillis = dateRangePickerState.selectedStartDateMillis
                endDateMillis = dateRangePickerState.selectedEndDateMillis
                if (startDateMillis != null && endDateMillis != null) {
                    dateIni = Instant.ofEpochMilli(startDateMillis!!)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                        .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                    dateFim = Instant.ofEpochMilli(endDateMillis!!)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                        .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                }
            }

            AnimatedVisibility(openDatePicker) {
                Dialog(onDismissRequest = { openDatePicker = false }) {
                    Surface(
                        shape = MaterialTheme.shapes.medium,
                        tonalElevation = 3.dp,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Column {
                            Row{
                                DateRangePicker(
                                    state = dateRangePickerState,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(1.dp), // Padding interno
                                    title = {
                                        Text(
                                            text = "Selecione um intervalo",
                                            style = MaterialTheme.typography.titleMedium,
                                            modifier = Modifier.padding(start = 16.dp, top = 16.dp)
                                        )
                                    },
                                    headline = {
                                        Text(
                                            text = "${dateRangePickerState.selectedStartDateMillis?.let { Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) } ?: "Início"} - ${dateRangePickerState.selectedEndDateMillis?.let { Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) } ?: "Fim"}",
                                            style = MaterialTheme.typography.bodyMedium,
                                            modifier = Modifier.padding(start = 16.dp, bottom = 16.dp)
                                        )
                                    },
                                    colors = DatePickerDefaults.colors(
                                        // Customize colors here
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.End
                            ) {
                                TextButton(onClick = { openDatePicker = false }) {
                                    Text("Cancelar")
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Button(
                                    onClick = {
                                        startDateMillis = dateRangePickerState.selectedStartDateMillis
                                        endDateMillis = dateRangePickerState.selectedEndDateMillis
                                        if (startDateMillis != null && endDateMillis != null) {
                                            dateIni = Instant.ofEpochMilli(startDateMillis!!)
                                                .atZone(ZoneId.systemDefault())
                                                .toLocalDate()
                                                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                                            dateFim = Instant.ofEpochMilli(endDateMillis!!)
                                                .atZone(ZoneId.systemDefault())
                                                .toLocalDate()
                                                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                                        }
                                        openDatePicker = false
                                    }
                                ) {
                                    Text("OK")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun DateScreenPreview() {
    CodekTheme {
        DateScreen()
    }
}