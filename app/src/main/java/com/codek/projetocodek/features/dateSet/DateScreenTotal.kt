package com.codek.projetocodek.features.dateSet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DateScreenTotal(modifier: Modifier = Modifier) {
    Column {
        DateScreen()
        Spacer(modifier = Modifier.height(16.dp))
        DateScreen1()
        Spacer(modifier = Modifier.height(16.dp))
        DateScreen2()
    }
}