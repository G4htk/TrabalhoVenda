package com.example.trabalhovenda.repository

import com.example.trabalhovenda.entity.ItemEntity
import com.example.trabalhovenda.dao.ItemDao

class ItemRepository(val itemDao: ItemDao) {
    val item = itemDao.buscar()

    suspend fun inserir(itemEntity: ItemEntity){
        itemDao.inserir(item = itemEntity)
    }
    suspend fun atualizar(itemEntity: ItemEntity){
        itemDao.atualizar(item = itemEntity)
    }
    suspend fun deletar(itemEntity: ItemEntity){
        itemDao.deletar(item = itemEntity)
    }
}