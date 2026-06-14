package com.example.trabalhovenda.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pedido_item")
data class PedidoItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val pedidoId: Int,
    val itemId: Int,
    val quantidade: Int,
    val valorUnitario: Double,
    val valorTotal: Double
)