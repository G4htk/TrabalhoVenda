package com.example.trabalhovenda.repository

import com.example.trabalhovenda.entity.EnderecoEntity
import com.example.trabalhovenda.dao.EnderecoDao

class EnderecoRepository(val enderecoDao: EnderecoDao) {
    val endereco = enderecoDao.buscar()

    suspend fun inserir(enderecoEntity: EnderecoEntity){
        enderecoDao.inserir(endereco = enderecoEntity)
    }
    suspend fun atualizar(enderecoEntity: EnderecoEntity){
        enderecoDao.atualizar(endereco = enderecoEntity)
    }
    suspend fun deletar(enderecoEntity: EnderecoEntity){
        enderecoDao.deletar(endereco = enderecoEntity)
    }
}