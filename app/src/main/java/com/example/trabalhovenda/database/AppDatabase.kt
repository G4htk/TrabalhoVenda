package com.example.trabalhovenda.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.trabalhovenda.dao.ClienteDao
import com.example.trabalhovenda.entity.ClienteEntity
import com.example.trabalhovenda.entity.EnderecoEntity
import com.example.trabalhovenda.entity.ItemEntity
import com.example.trabalhovenda.converter.Converters
import com.example.trabalhovenda.dao.EnderecoDao
import com.example.trabalhovenda.dao.ItemDao
import com.example.trabalhovenda.dao.PedidoDao
import com.example.trabalhovenda.dao.PedidoItemDao
import com.example.trabalhovenda.entity.PedidoEntity
import com.example.trabalhovenda.entity.PedidoItemEntity

@TypeConverters(Converters::class)
@Database(
    entities = [
        ClienteEntity::class,
        EnderecoEntity::class,
        ItemEntity::class,
        PedidoEntity::class,
        PedidoItemEntity::class
    ],
    version = 1


)
abstract class AppDatabase : RoomDatabase() {
    abstract fun clienteDao(): ClienteDao
    abstract fun enderecoDao(): EnderecoDao
    abstract fun itemDao(): ItemDao
    abstract fun pedidoDao(): PedidoDao
    abstract fun pedidoItemDao(): PedidoItemDao
}
