package com.example.trabalhovenda.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.trabalhovenda.entity.EnderecoEntity
import com.example.trabalhovenda.repository.EnderecoRepository
import kotlinx.coroutines.launch



class EnderecoViewModel(private val repository: EnderecoRepository): ViewModel() {

    val enderecos = repository.endereco

    fun adicionarEndereco
                (codigo:Int,
                 nome:String, logradouro:String,
                 numero:Int, bairro:String,
                 cidade:String, uf:String): Boolean{
        if(codigo <= 0 || nome.isBlank() || logradouro.isBlank() || numero <= 0 || bairro.isBlank() || cidade.isBlank() || uf.isBlank())
            return false
                    viewModelScope.launch {
                        val endereco = EnderecoEntity(codigo = codigo, nome = nome, logradouro = logradouro, numero = numero, bairro = bairro, cidade = cidade, uf = uf)
                        repository.inserir(endereco)
                        }
        return true
                    }

    fun atualizarEndereco(endereco: EnderecoEntity){
        viewModelScope.launch {
            repository.atualizar(endereco)
        }
    }
    fun deletarEndereco(endereco: EnderecoEntity){
        viewModelScope.launch {
            repository.deletar(endereco)
        }
    }


}

class EnderecoViewModelFactory(private val repository: EnderecoRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EnderecoViewModel(repository) as T
    }
}