package com.lanchonete.app.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pedidos")
data class Pedido(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val cliente: String,
    val total: Double = 0.0,
    val status: String = "Pendente", // Pendente, Preparando, Pronto, Entregue, Cancelado
    val dataCriacao: Long = System.currentTimeMillis(),
    val dataAtualizacao: Long = System.currentTimeMillis(),
    val observacoes: String = "",
    val formaPagamento: String = "Dinheiro", // Dinheiro, Cartão, PIX
    val pago: Boolean = false // Novo campo para controlar se o pedido foi pago
)

