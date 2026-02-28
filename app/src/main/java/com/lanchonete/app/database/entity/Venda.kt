package com.lanchonete.app.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vendas")
data class Venda(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val pedidoId: Long,
    val total: Double,
    val formaPagamento: String = "Dinheiro", // Dinheiro, Cartão, PIX
    val dataVenda: Long = System.currentTimeMillis(),
    val observacoes: String = ""
)



