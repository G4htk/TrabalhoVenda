package com.example.trabalhovenda.repository

import com.example.trabalhovenda.dao.PedidoItemDao
import com.example.trabalhovenda.entity.PedidoItemEntity

class PedidoItemRepository(val pedidoItemDao: PedidoItemDao) {
    val item = pedidoItemDao.buscar()

    suspend fun inserir(pedidoItemEntity: PedidoItemEntity): Long {
       return pedidoItemDao.inserir(pedidoItem = pedidoItemEntity)
    }

    suspend fun atualizar(pedidoItemEntity: PedidoItemEntity) {
        pedidoItemDao.atualizar(pedidoItem = pedidoItemEntity)
    }

    suspend fun deletar(pedidoItemEntity: PedidoItemEntity) {
        pedidoItemDao.deletar(pedidoItem = pedidoItemEntity)
    }
}