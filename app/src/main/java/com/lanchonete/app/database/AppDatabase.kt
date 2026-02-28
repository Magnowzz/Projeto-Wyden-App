package com.lanchonete.app.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.lanchonete.app.database.dao.ItemPedidoDao
import com.lanchonete.app.database.dao.PedidoDao
import com.lanchonete.app.database.dao.ProdutoDao
import com.lanchonete.app.database.dao.VendaDao
import com.lanchonete.app.database.entity.ItemPedido
import com.lanchonete.app.database.entity.Pedido
import com.lanchonete.app.database.entity.Produto
import com.lanchonete.app.database.entity.Venda

@Database(
    entities = [Produto::class, Pedido::class, ItemPedido::class, Venda::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun produtoDao(): ProdutoDao
    abstract fun pedidoDao(): PedidoDao
    abstract fun itemPedidoDao(): ItemPedidoDao
    abstract fun vendaDao(): VendaDao
    
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "lanchonete_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}


