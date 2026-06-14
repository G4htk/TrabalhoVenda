package com.example.trabalhovenda.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.trabalhovenda.entity.ClienteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ClienteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserir(cliente: ClienteEntity)

    @Query("SELECT * FROM cliente ORDER BY nome")
    fun buscar(): Flow<List<ClienteEntity>>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun atualizar(cliente: ClienteEntity)

    @Delete
    suspend fun deletar(cliente: ClienteEntity)
}