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
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
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
fun DateScreen1(modifier: Modifier = Modifier) {
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

            val dateIniPickerState = rememberDatePickerState()
            val dateFimPickerState = rememberDatePickerState()
            var startDateMillis by remember { mutableStateOf<Long?>(null) }
            var endDateMillis by remember { mutableStateOf<Long?>(null) }

            LaunchedEffect(startDateMillis, endDateMillis) {
                isConfirmEnabled = dateIniPickerState != null && dateFimPickerState != null
            }

            AnimatedVisibility(openDatePicker) {
                Dialog(onDismissRequest = { openDatePicker = false }) {
                    Surface(
                        shape = MaterialTheme.shapes.medium,
                        tonalElevation = 3.dp
                    ) {
                        Column(
                            Modifier
                                .fillMaxWidth()
                        ) {
                            // Tabs for Date Selection
                            TabRow(selectedTabIndex = selectedTab) {
                                Tab(
                                    selected = selectedTab == 0,
                                    onClick = { selectedTab = 0 }
                                ) {
                                    Text("Data Inicial", modifier = Modifier.padding(16.dp))
                                }
                                Tab(
                                    selected = selectedTab == 1,
                                    onClick = { selectedTab = 1 }
                                ) {
                                    Text("Data Final", modifier = Modifier.padding(16.dp))
                                }
                            }

                            // Date Picker based on selected tab
                            when (selectedTab) {
                                0 -> {
                                    DatePicker(dateIniPickerState)
                                }
                                1 -> {
                                    DatePicker(dateFimPickerState)
                                }
                            }

                            // Action Buttons
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.End
                            ) {
                                TextButton(
                                    onClick = {
                                        openDatePicker = false
                                        dateIni = ""
                                        dateFim = ""
                                        dateIniPickerState.selectedDateMillis = null
                                        dateFimPickerState.selectedDateMillis = null
                                    }) {
                                    Text("Cancelar")
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Button(
                                    onClick = {

                                        startDateMillis = dateIniPickerState.selectedDateMillis
                                        dateIni = Instant.ofEpochMilli(startDateMillis!!)
                                            .atZone(ZoneId.of("UTC"))
                                            .toLocalDate()
                                            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))

                                        endDateMillis = dateFimPickerState.selectedDateMillis
                                        dateFim = Instant.ofEpochMilli(endDateMillis!!)
                                            .atZone(ZoneId.of("UTC"))
                                            .toLocalDate()
                                            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))

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