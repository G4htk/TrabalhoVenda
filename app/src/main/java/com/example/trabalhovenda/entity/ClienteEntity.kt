package com.example.trabalhovenda.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "cliente")
data class ClienteEntity(
    @PrimaryKey(true) val id:Int = 0,
    val codigo: Int,
    val nome: String,
    val cpf: String,
    val dataNac: LocalDate,
    val enderecoId: Int
)