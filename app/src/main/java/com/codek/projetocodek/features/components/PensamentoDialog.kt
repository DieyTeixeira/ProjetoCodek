package com.codek.projetocodek.features.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.codek.projetocodek.features.screens.toColor
import com.codek.projetocodek.model.Pensamento

@Composable
fun PensamentoDialog(
    pensamento: Pensamento?,
    onDismiss: () -> Unit,
    onSave: (Pensamento) -> Unit
) {
    var conteudo by remember { mutableStateOf(pensamento?.conteudo ?: "") }
    var autoria by remember { mutableStateOf(pensamento?.autoria ?: "") }
    var selectedPrimaryColor by remember { mutableStateOf(pensamento?.cor_pri ?: "") }
    var selectedSecondaryColor by remember { mutableStateOf(pensamento?.cor_sec ?: "") }

    val colorOptions = listOf(
        // aspas to card
        "#727171" to "#E7E6E6",
        "#36AFCE" to "#D6EFF5",
        "#8BC145" to "#E7F2D9",
        "#FFC000" to "#FFF2CC",
        "#B74919" to "#F7D7C9"
    )

    Dialog(onDismissRequest = { onDismiss() }) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(Color.White, RoundedCornerShape(8.dp))
                .padding(16.dp)
        ) {
            Column {
                Text(
                    text = if (pensamento == null) "Novo Pensamento" else "Editar Pensamento",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.size(8.dp))
                TextField(
                    value = autoria,
                    onValueChange = { autoria = it },
                    label = { Text("Autoria") }
                )
                Spacer(Modifier.size(8.dp))
                TextField(
                    value = conteudo,
                    onValueChange = { conteudo = it },
                    label = { Text("Conteúdo") }
                )
                Spacer(Modifier.size(16.dp))
                // Opções de cores
                Text("Escolha um modelo de cor:")
                Spacer(Modifier.size(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    colorOptions.forEach { (primary, secondary) ->
                        ColorOption(
                            primaryColor = primary.toColor(),
                            secondaryColor = secondary.toColor(),
                            isSelected = primary == selectedPrimaryColor && secondary == selectedSecondaryColor,
                            onClick = {
                                selectedPrimaryColor = primary
                                selectedSecondaryColor = secondary
                            }
                        )
                    }
                }
                Spacer(Modifier.size(16.dp))
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = { onDismiss() }) {
                        Text("Cancelar")
                    }
                    Spacer(Modifier.size(8.dp))
                    TextButton(onClick = {
                        val newPensamento = Pensamento(
                            id = pensamento?.id,
                            conteudo = conteudo,
                            autoria = autoria,
                            cor_pri = selectedPrimaryColor,
                            cor_sec = selectedSecondaryColor
                        )
                        onSave(newPensamento)
                    }) {
                        Text("Salvar")
                    }
                }
            }
        }
    }
}