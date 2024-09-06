package com.codek.projetocodek.features.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.dieyteixeira.projetocodek.R
import com.codek.projetocodek.ui.theme.CodekTheme
import kotlin.random.Random

@Composable
fun CaronaScreen(
    modifier: Modifier = Modifier
) {

    Column {
        Column(
            Modifier
                .fillMaxWidth()
//            .height(20.dp)
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.btn_carona),
                contentDescription = null
            )
        }

        LazyColumn(
            modifier.fillMaxSize(),
            contentPadding = PaddingValues(10.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            // filtro
            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val filters = remember {
                        mutableStateListOf("Todos", "Data", "Valor")
                    }
                    filters.forEach { filter ->
                        Box(
                            Modifier
                                .clip(CircleShape)
                                .background(Color.Gray)
                                .padding(16.dp, 8.dp)
                        ) {
                            Text(text = filter)
                        }
                    }
                }
                Spacer(Modifier.size(15.dp))
            }
            // cards
            items(10) {
                val avatarSize = 80.dp
                val totalStars = 5
                val filledStars = remember { mutableStateOf(Random.nextInt(2, totalStars + 1)) }

                Row(
                    Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.LightGray)
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Primeira coluna (avatar e avaliação)
                    Column {
                        // Avatar
                        Box(
                            Modifier
                                .size(avatarSize)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.Gray)
                        )
                        Spacer(Modifier.size(15.dp))
                        // Avaliação (5 estrelas)
                        Row(
                            Modifier
                                .size(avatarSize),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            for (i in 1..totalStars) {
                                val starColor =
                                    if (i <= filledStars.value) Color.Yellow else Color.Gray
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = starColor,
                                    modifier = Modifier
                                        .padding(1.dp)
                                        .size(13.dp)
                                )
                            }
                        }
                    }

                    // Segunda coluna
                    Column {
                        // Informações sobre o perfil (nome e descrição)
                        Column {
                            Text(
                                text = "Nome Sobrenome",
                                maxLines = 1,
                                fontSize = 14.sp,
                                color = Color.Black,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Descrição Breve - Cidade-UF",
                                maxLines = 1,
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                            Spacer(Modifier.size(5.dp))
                            Text(
                                text = "Perfil do Usuário",
                                fontSize = 14.sp,
                                maxLines = 1,
                                color = Color.Black,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Terapeuta fora dos padrões que adora viajar e ama a natureza. Não dispensa uma boa comida e viagem. Ama música, carro cheio e todos cantando juntos.",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                        Spacer(Modifier.size(10.dp))
                        // Informação sobre o trajeto (origem e destino)
                        Column(
                            Modifier
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Torres   09:00",
                                    fontSize = 12.sp,
                                    color = Color.Black
                                )
                                Spacer(Modifier.size(10.dp))
                                Box(
                                    Modifier
                                        .height(1.dp)
                                        .width(40.dp)
                                        .background(Color.Black)
                                )
                                Spacer(Modifier.size(10.dp))
                                Text(
                                    text = "12:20   Floripa",
                                    fontSize = 12.sp,
                                    color = Color.Black
                                )
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
private fun ChatsListScreenPreview() {
    CodekTheme {
        CaronaScreen()
    }
}