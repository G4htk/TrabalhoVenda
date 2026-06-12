package com.example.trabalhovenda.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.foradevenda.entity.ClienteEntity
import com.example.foradevenda.entity.EnderecoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EnderecoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserir(endereco: EnderecoEntity)

    @Query("SELECT * FROM endereco ORDER BY cidade")
    fun buscar(): Flow<List<EnderecoEntity>>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun atualizar(endereco: EnderecoEntity)

    @Delete()
    suspend fun deletar(endereco: EnderecoEntity)
}
