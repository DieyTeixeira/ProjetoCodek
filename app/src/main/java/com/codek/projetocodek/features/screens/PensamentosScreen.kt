package com.codek.projetocodek.features.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.dieyteixeira.projetocodek.R
import com.codek.projetocodek.api.PensamentoApi
import com.codek.projetocodek.features.components.PensamentoCard1
import com.codek.projetocodek.features.components.PensamentoCard2
import com.codek.projetocodek.features.components.PensamentoDialog
import com.codek.projetocodek.model.Pensamento
import com.codek.projetocodek.ui.theme.CodekTheme
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Composable
fun PensamentosScreen(
    modifier: Modifier = Modifier,
) {
    val pensamentos = remember { mutableStateListOf<Pensamento>() }
    val scope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }
    var currentPensamento by remember { mutableStateOf<Pensamento?>(null) }
    var expandedPensamentoId by remember { mutableStateOf<Int?>(null) }

    // Instância Retrofit
    val retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.5.249:3000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val pensamentoApi = retrofit.create(PensamentoApi::class.java)

    // Realiza a chamada da API quando a tela for carregada
    LaunchedEffect(Unit) {
        scope.launch {
            try {
                val response = pensamentoApi.getPensamentos()
                pensamentos.clear()
                pensamentos.addAll(response)
                Log.d("API_SUCCESS", "Pensamentos carregados com sucesso")
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("API_ERROR", "Erro ao carregar pensamentos", e)
            }
        }
    }

    if (showDialog) {
        PensamentoDialog(
            pensamento = currentPensamento,
            onDismiss = { showDialog = false },
            onSave = { newPensamento ->
                scope.launch {
                    if (currentPensamento != null) {
                        pensamentoApi.updatePensamento(currentPensamento!!.id.toString(), newPensamento)
                        val index = pensamentos.indexOfFirst { it.id == currentPensamento!!.id }
                        pensamentos[index] = newPensamento
                    } else {
                        val addedPensamento = pensamentoApi.createPensamento(newPensamento)
                        pensamentos.add(addedPensamento)
                    }
                    showDialog = false
                }
            }
        )
    }

    Column(
        modifier.clickable {
            expandedPensamentoId = null
        }
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                Modifier
                    .rotate(-90f)
                    .padding(start = 10.dp),
                Alignment.CenterStart
            ) {
                Text(
                    text = "CARDS",
                    fontSize = 20.sp,
                    color = Color(0xFF8F4A0E),
                    fontWeight = FontWeight.Bold
                )
            }
            Image(
                painter = painterResource(id = R.drawable.login_laranja),
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 10.dp)
                    .align(Alignment.CenterVertically)
            )
        }

        Row(
            Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            Row(
                Modifier
                    .clip(CircleShape)
                    .background(Color(0xFF8F4A0E))
                    .padding(10.dp, 5.dp)
                    .clickable {
                        currentPensamento = null
                        expandedPensamentoId = null
                        showDialog = true
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .padding(1.dp)

                )
                Text(
                    text = "NOVO CARD",
                    color = Color.White,
                    modifier = Modifier
                        .padding(1.dp)
                )
            }
        }
        Spacer(Modifier.size(15.dp))

        LazyColumn(
            modifier.fillMaxSize(),
            contentPadding = PaddingValues(10.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            // cards
            items(pensamentos) { pensamento ->
                PensamentoCard1(
                    pensamento = pensamento,
                    isExpanded = expandedPensamentoId == pensamento.id,
                    onEdit = {
                        currentPensamento = pensamento
                        showDialog = true
                        expandedPensamentoId = null
                    },
                    onDelete = {
                        scope.launch {
                            pensamentoApi.deletePensamento(pensamento.id.toString())
                            pensamentos.remove(pensamento)
                        }
                    },
                    onClick = {
                        expandedPensamentoId = if (expandedPensamentoId == pensamento.id) null else pensamento.id
                    }
                )
            }
        }
    }
}

fun String.toColor(): Color {
    return try {
        Color(Color(android.graphics.Color.parseColor(this)).toArgb())
    } catch (e: Exception) {
        Color.LightGray
    }
}

@Preview
@Composable
private fun ChatsListScreenPreview() {
    CodekTheme {
        PensamentosScreen()
    }
}