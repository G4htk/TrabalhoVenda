package com.example.trabalhovenda.repository

import com.example.foradevenda.entity.ItemEntity
import com.example.trabalhovenda.dao.PedidoDao
import com.example.trabalhovenda.dao.PedidoItemDao
import com.example.trabalhovenda.entity.PedidoEntity
import com.example.trabalhovenda.entity.PedidoItemEntity

class PedidoRepository(public val pedidoDao: PedidoDao) {
    val pedidos = pedidoDao.buscar();

    suspend fun inserir(pedidoEntity: PedidoEntity): Long{
       return pedidoDao.inserir(pedido = pedidoEntity)
    }
    suspend fun atualizar(pedidoEntity: PedidoEntity){
        pedidoDao.atualizar(pedido = pedidoEntity)
    }
    suspend fun deletar(pedidoEntity: PedidoEntity){
        pedidoDao.deletar(pedido = pedidoEntity)
    }
}