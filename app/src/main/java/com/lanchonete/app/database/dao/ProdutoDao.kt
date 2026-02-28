package com.lanchonete.app.database.dao

import androidx.room.*
import com.lanchonete.app.database.entity.Produto
import kotlinx.coroutines.flow.Flow

@Dao
interface ProdutoDao {
    @Query("SELECT * FROM produtos ORDER BY nome ASC")
    fun obterTodos(): Flow<List<Produto>>
    
    @Query("SELECT * FROM produtos WHERE id = :id")
    suspend fun obterPorId(id: Long): Produto?
    
    @Query("SELECT * FROM produtos WHERE quantidadeEstoque > 0 ORDER BY nome ASC")
    fun obterComEstoque(): Flow<List<Produto>>
    
    @Query("SELECT * FROM produtos WHERE quantidadeEstoque <= :limite ORDER BY quantidadeEstoque ASC")
    fun obterEstoqueBaixo(limite: Int = 10): Flow<List<Produto>>
    
    @Query("SELECT * FROM produtos WHERE nome LIKE '%' || :busca || '%' OR descricao LIKE '%' || :busca || '%'")
    fun buscar(busca: String): Flow<List<Produto>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserir(produto: Produto): Long
    
    @Update
    suspend fun atualizar(produto: Produto)
    
    @Delete
    suspend fun deletar(produto: Produto)
    
    @Query("UPDATE produtos SET quantidadeEstoque = quantidadeEstoque - :quantidade WHERE id = :id")
    suspend fun reduzirEstoque(id: Long, quantidade: Int)
    
    @Query("UPDATE produtos SET quantidadeEstoque = quantidadeEstoque + :quantidade WHERE id = :id")
    suspend fun aumentarEstoque(id: Long, quantidade: Int)
}



