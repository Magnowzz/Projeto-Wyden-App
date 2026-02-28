package com.lanchonete.app.database.dao

import androidx.room.*
import com.lanchonete.app.database.entity.Venda
import kotlinx.coroutines.flow.Flow

@Dao
interface VendaDao {
    @Query("SELECT * FROM vendas ORDER BY dataVenda DESC")
    fun obterTodas(): Flow<List<Venda>>
    
    @Query("SELECT * FROM vendas WHERE id = :id")
    suspend fun obterPorId(id: Long): Venda?
    
    @Query("SELECT * FROM vendas WHERE pedidoId = :pedidoId")
    suspend fun obterPorPedido(pedidoId: Long): Venda?
    
    @Query("SELECT SUM(total) FROM vendas WHERE dataVenda >= :dataInicio AND dataVenda <= :dataFim")
    suspend fun obterTotalVendas(dataInicio: Long, dataFim: Long): Double?
    
    @Insert
    suspend fun inserir(venda: Venda): Long
    
    @Update
    suspend fun atualizar(venda: Venda)
    
    @Delete
    suspend fun deletar(venda: Venda)
}



