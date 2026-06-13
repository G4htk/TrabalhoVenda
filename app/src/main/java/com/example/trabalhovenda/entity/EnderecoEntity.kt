package com.example.trabalhovenda.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "endereco")
data class EnderecoEntity (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val codigo: Int,
    val nome: String,
    val logradouro: String,
    val numero: Int,
    val bairro: String,
    val cidade: String,
    val uf: String
)