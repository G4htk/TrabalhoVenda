package com.example.trabalhovenda.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foradevenda.entity.ItemEntity
import com.example.trabalhovenda.repository.ItemRepository
import kotlinx.coroutines.launch

class ItemViewModel(private val repository : ItemRepository): ViewModel() {
    val itens = repository.item

    fun adicionarItem(codigo: Int, descricao: String, valor: Double, unMedia: Int): Boolean{
        if(codigo <=0 || descricao.isBlank() || valor <= 0 || unMedia <= 0) return false
        viewModelScope.launch {
            val item = ItemEntity(codigo = codigo, descricao = descricao, valor = valor, unMedia = unMedia)
            repository.inserir(item)
        }
        return true
    }

    fun atualizarItem(item: ItemEntity){
        viewModelScope.launch {
            repository.atualizar(item)
        }
    }
    fun deletarItem(item: ItemEntity){
        viewModelScope.launch {
            repository.deletar(item)
        }
    }
}