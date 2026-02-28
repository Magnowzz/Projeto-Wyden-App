package com.lanchonete.app.database.dao

import androidx.room.*
import com.lanchonete.app.database.entity.Pedido
import kotlinx.coroutines.flow.Flow

@Dao
interface PedidoDao {
    @Query("SELECT * FROM pedidos ORDER BY dataCriacao DESC")
    fun obterTodos(): Flow<List<Pedido>>
    
    @Query("SELECT * FROM pedidos WHERE id = :id")
    suspend fun obterPorId(id: Long): Pedido?
    
    @Query("SELECT * FROM pedidos WHERE status = :status ORDER BY dataCriacao DESC")
    fun obterPorStatus(status: String): Flow<List<Pedido>>
    
    @Query("SELECT * FROM pedidos WHERE cliente LIKE '%' || :busca || '%' ORDER BY dataCriacao DESC")
    fun buscar(busca: String): Flow<List<Pedido>>
    
    @Insert
    suspend fun inserir(pedido: Pedido): Long
    
    @Update
    suspend fun atualizar(pedido: Pedido)
    
    @Delete
    suspend fun deletar(pedido: Pedido)
    
    @Query("UPDATE pedidos SET status = :status, dataAtualizacao = :dataAtualizacao WHERE id = :id")
    suspend fun atualizarStatus(id: Long, status: String, dataAtualizacao: Long = System.currentTimeMillis())
    
    @Query("SELECT SUM(total) FROM pedidos WHERE status = 'Entregue' AND dataCriacao >= :dataInicio")
    suspend fun obterTotalVendas(dataInicio: Long): Double?
}



