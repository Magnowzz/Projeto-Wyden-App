package com.lanchonete.app.database.dao

import androidx.room.*
import com.lanchonete.app.database.entity.ItemPedido
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemPedidoDao {
    @Query("SELECT * FROM itens_pedido WHERE pedidoId = :pedidoId")
    fun obterPorPedido(pedidoId: Long): Flow<List<ItemPedido>>
    
    @Query("SELECT * FROM itens_pedido WHERE pedidoId = :pedidoId")
    suspend fun obterPorPedidoSync(pedidoId: Long): List<ItemPedido>
    
    @Insert
    suspend fun inserir(item: ItemPedido)
    
    @Insert
    suspend fun inserirTodos(itens: List<ItemPedido>)
    
    @Update
    suspend fun atualizar(item: ItemPedido)
    
    @Delete
    suspend fun deletar(item: ItemPedido)
    
    @Query("DELETE FROM itens_pedido WHERE pedidoId = :pedidoId")
    suspend fun deletarPorPedido(pedidoId: Long)
}

