package com.codek.projetocodek.features.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.dieyteixeira.projetocodek.R
import com.codek.projetocodek.api.PensamentoApi
import com.codek.projetocodek.features.components.PensamentoCard1
import com.codek.projetocodek.features.components.PensamentoDialog
import com.codek.projetocodek.features.components.SkeletonCard
import com.codek.projetocodek.model.Pensamento
import com.codek.projetocodek.ui.theme.CodekTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.random.Random

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PensamentosScreen(
    modifier: Modifier = Modifier
) {
    val pensamentos = remember { mutableStateListOf<Pensamento>() }
    val scope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }
    var currentPensamento by remember { mutableStateOf<Pensamento?>(null) }
    var expandedPensamentoId by remember { mutableStateOf<Int?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var isRefreshing by remember { mutableStateOf(false) }

    val retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.5.249:3000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val pensamentoApi = retrofit.create(PensamentoApi::class.java)

    val loadPensamentos = {
        scope.launch {
            try {
                val response = pensamentoApi.getPensamentos()
                pensamentos.clear()
                pensamentos.addAll(response)
                errorMessage = null
                Log.d("API_SUCCESS", "Pensamentos carregados com sucesso")
            } catch (e: Exception) {
                e.printStackTrace()
                errorMessage = "$e"
                Log.e("API_ERROR", "Erro ao carregar pensamentos", e)
            } finally {
                isLoading = false
            }
        }
    }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            scope.launch {
                isRefreshing = true
                isLoading = true
                errorMessage = null
                delay(Random.nextLong(1000, 3000))
                loadPensamentos()
                isRefreshing = false
            }
        }
    )

    LaunchedEffect(Unit) {
        loadPensamentos()
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
        modifier
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
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

        Box(
            Modifier
                .fillMaxSize()
        ) {
            errorMessage?.let {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .fillMaxWidth()
                        .padding(vertical = 100.dp, horizontal = 20.dp)
                        .pullRefresh(state = pullRefreshState),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = "Não foi possível estabelecer\nconexão com o servidor.",
                                color = Color.Red,
                                modifier = Modifier
                                    .align(Alignment.Center),
                                textAlign = TextAlign.Center
                            )
                        }
                        Spacer(Modifier.size(15.dp))
                        Text(
                            text = it,
                            color = Color.Gray,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.size(15.dp))
                        Box(
                            modifier = Modifier
                                .height(35.dp)
                                .fillMaxWidth()
                                .background(
                                    brush = Brush.linearGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            Color.Red,
                                            Color.Transparent
                                        ),
                                    )
                                )
                        ) {
                            Text(
                                text = "Verifique sua conexão!",
                                color = Color.White,
                                modifier = Modifier
                                    .align(Alignment.Center),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            } ?: run {
                if (isLoading) {
                    LazyColumn(
                        modifier
                            .fillMaxSize()
                            .pullRefresh(state = pullRefreshState),
                        contentPadding = PaddingValues(10.dp),
                        verticalArrangement = Arrangement.spacedBy(15.dp)
                    ) {
                        items(10) {
                            SkeletonCard()
                        }
                    }
                } else {
                    LazyColumn(
                        modifier
                            .fillMaxSize()
                            .pullRefresh(state = pullRefreshState),
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
                                    expandedPensamentoId =
                                        if (expandedPensamentoId == pensamento.id) null else pensamento.id
                                }
                            )
                        }
                    }
                }
            }
            PullRefreshIndicator(
                refreshing = isRefreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
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