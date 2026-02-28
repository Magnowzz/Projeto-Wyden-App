package com.lanchonete.app.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "produtos")
data class Produto(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nome: String,
    val descricao: String = "",
    val preco: Double,
    val quantidadeEstoque: Int = 0,
    val categoria: String = "",
    val dataCadastro: Long = System.currentTimeMillis()
)



