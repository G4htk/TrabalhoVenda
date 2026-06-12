package com.example.trabalhovenda.repository

import com.example.foradevenda.dao.ClienteDao
import com.example.foradevenda.entity.ClienteEntity

class ClienteRepository(private val clienteDao: ClienteDao) {
    val cliente = clienteDao.buscar();

    suspend fun inserir(clienteEntity: ClienteEntity){
        clienteDao.inserir(cliente = clienteEntity)
    }
    suspend fun atualizar(clienteEntity: ClienteEntity){
        clienteDao.atualizar(cliente = clienteEntity)
    }
    suspend fun deletar(clienteEntity: ClienteEntity){
        clienteDao.deletar(cliente = clienteEntity)
    }
}