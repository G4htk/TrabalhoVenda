package com.example.trabalhovenda.repository

import com.example.trabalhovenda.dao.PedidoDao
import com.example.trabalhovenda.entity.PedidoEntity

class PedidoRepository(val pedidoDao: PedidoDao) {
    val pedidos = pedidoDao.buscar()

    suspend fun inserir(pedidoEntity: PedidoEntity): Long{
       return pedidoDao.inserir(pedido = pedidoEntity)
    }

    suspend fun buscarPorCodigo(codigo: String): PedidoEntity? {
        return pedidoDao.buscarPorCodigo(codigo)
    }

    suspend fun atualizar(pedidoEntity: PedidoEntity){
        pedidoDao.atualizar(pedido = pedidoEntity)
    }
    suspend fun deletar(pedidoEntity: PedidoEntity){
        pedidoDao.deletar(pedido = pedidoEntity)
    }
}