package com.example.trabalhovenda.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.trabalhovenda.entity.ItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserir(item: ItemEntity)

    @Query("SELECT * FROM item ORDER BY descricao")
    fun buscar(): Flow<List<ItemEntity>>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun atualizar(item: ItemEntity)

    @Delete
    suspend fun deletar(item: ItemEntity)
}
