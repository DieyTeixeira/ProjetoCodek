package com.codek.projetocodek.model

data class Pensamento(
    val id: Int? = null,
    val conteudo: String,
    val autoria: String,
    val cor_pri: String? = null,
    val cor_sec: String? = null
)