package com.example.foradevenda.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "item")
class ItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val codigo: Int,
    val descricao: String,
    val valor: Double,
    val unMedia: Int
) {
}