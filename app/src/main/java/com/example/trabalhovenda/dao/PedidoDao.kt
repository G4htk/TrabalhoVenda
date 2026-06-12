package com.example.trabalhovenda.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.trabalhovenda.entity.PedidoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PedidoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserir(pedido: PedidoEntity): Long

    @Query("SELECT * FROM pedido ORDER BY clienteId")
    fun buscar(): Flow<List<PedidoEntity>>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun atualizar(pedido: PedidoEntity)

    @Delete()
    suspend fun deletar(pedido: PedidoEntity)
}
