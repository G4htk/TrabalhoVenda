package com.example.trabalhovenda.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.trabalhovenda.entity.PedidoItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PedidoItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserir(pedidoItem: PedidoItemEntity): Long

    @Query("SELECT * FROM pedido_item ORDER BY pedidoId")
    fun buscar(): Flow<List<PedidoItemEntity>>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun atualizar(pedidoItem: PedidoItemEntity)

    @Delete
    suspend fun deletar(pedidoItem: PedidoItemEntity)
}
