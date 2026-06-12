package com.example.trabalhovenda.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pedido")
class PedidoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val codigo: String,
    val clienteId: Int,
    val enderecoId: Int,
    val condicaoPagamento: String,
    val valorFrete: Double,
    val valorTotal: Double,
    val totalItens: Int

){}