package com.example.trabalhovenda.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.trabalhovenda.entity.ClienteEntity
import com.example.trabalhovenda.repository.ClienteRepository
import kotlinx.coroutines.launch
import java.time.LocalDate

class ClienteViewModel(private val repository: ClienteRepository)
    : ViewModel() {

    val clientes = repository.cliente

    fun adicionarCliente
                (codigo:Int, nome: String, cpf: String, dataNac: LocalDate, enderecoId:Int): Boolean{
                    if(nome.isBlank() || cpf.isBlank() || codigo <= 0 || enderecoId <= 0 ) return false
        viewModelScope.launch {
            val cliente = ClienteEntity(
                codigo = codigo,
                nome = nome,
                cpf = cpf,
                dataNac = dataNac,
                enderecoId = enderecoId
            )
            repository.inserir(cliente)
        }
        return true
    }

    fun atualizarCliente(cliente: ClienteEntity){
        viewModelScope.launch {
            repository.atualizar(cliente)
        }
    }
    fun deletarCliente(cliente: ClienteEntity){
        viewModelScope.launch {
            repository.deletar(cliente )
        }
    }
}
class ClienteViewModelFactory(private val repository: ClienteRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ClienteViewModel(repository) as T
    }
}