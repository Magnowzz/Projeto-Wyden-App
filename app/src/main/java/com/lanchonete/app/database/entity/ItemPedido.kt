package com.lanchonete.app.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "itens_pedido",
    primaryKeys = ["pedidoId", "produtoId"],
    foreignKeys = [
        ForeignKey(
            entity = Pedido::class,
            parentColumns = ["id"],
            childColumns = ["pedidoId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Produto::class,
            parentColumns = ["id"],
            childColumns = ["produtoId"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [Index("pedidoId"), Index("produtoId")]
)
data class ItemPedido(
    val pedidoId: Long,
    val produtoId: Long,
    val quantidade: Int,
    val precoUnitario: Double,
    val subtotal: Double
)

