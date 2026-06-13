package com.example.trabalhovenda.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.trabalhovenda.entity.PedidoEntity
import com.example.trabalhovenda.entity.PedidoItemEntity
import com.example.trabalhovenda.repository.PedidoItemRepository
import com.example.trabalhovenda.repository.PedidoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PedidoViewModel(
    private val pedidoRepository: PedidoRepository,
    private val pedidoItemRepository: PedidoItemRepository
) : ViewModel() {

    val pedidos = pedidoRepository.pedidos

    private val _itensPedido = MutableStateFlow<List<PedidoItemEntity>>(emptyList())
    val itensPedido: StateFlow<List<PedidoItemEntity>> = _itensPedido

    private val _valorTotal = MutableStateFlow(0.0)
    val valorTotal: StateFlow<Double> = _valorTotal

    private val _totalItens = MutableStateFlow(0)
    val totalItens: StateFlow<Int> = _totalItens

    private val _parcelas = MutableStateFlow<List<Double>>(emptyList())
    val parcelas: StateFlow<List<Double>> = _parcelas

    private val _pedidoEncontrado = MutableStateFlow<PedidoEntity?>(null)
    val pedidoEncontrado: StateFlow<PedidoEntity?> = _pedidoEncontrado

    fun adicionarItem(itemId: Int, quantidade: Int, valorUnitario: Double) {
        if (quantidade <= 0 || valorUnitario <= 0) return

        val novoItem = PedidoItemEntity(
            pedidoId = 0,
            itemId = itemId,
            quantidade = quantidade,
            valorUnitario = valorUnitario,
            valorTotal = quantidade * valorUnitario
        )

        _itensPedido.value = _itensPedido.value + novoItem
        recalcularTotais()
    }

    private fun recalcularTotais() {
        _valorTotal.value = _itensPedido.value.sumOf { it.valorTotal }
        _totalItens.value = _itensPedido.value.sumOf { it.quantidade }
    }

    fun calcularFrete(cidade: String, estado: String): Double {
        return when {
            estado != "PR" -> 50.0
            cidade != "Toledo" -> 20.0
            else -> 0.0
        }
    }

    fun calcularValorFinal(condicao: String, frete: Double): Double {
        val subtotal = _valorTotal.value
        val comCondicao = when (condicao) {
            "avista" -> subtotal * 0.95
            "aprazo" -> subtotal * 1.05
            else -> subtotal
        }
        return comCondicao + frete
    }

    fun calcularParcelas(numeroParcelas: Int, valorFinal: Double) {
        if (numeroParcelas <= 0) return
        val valorParcela = valorFinal / numeroParcelas
        _parcelas.value = List(numeroParcelas) { valorParcela }
    }

    private fun gerarCodigo(): String {
        return "PED${System.currentTimeMillis()}"
    }

    fun concluirPedido(
        clienteId: Int,
        enderecoId: Int,
        condicaoPagamento: String,
        frete: Double,
        valorFinal: Double,
        onSucesso: (String) -> Unit
    ) {
        if (clienteId <= 0 || enderecoId <= 0) return
        if (_itensPedido.value.isEmpty()) return
        if (condicaoPagamento.isBlank()) return

        val codigo = gerarCodigo()

        viewModelScope.launch {
            val pedido = PedidoEntity(
                codigo = codigo,
                clienteId = clienteId,
                enderecoId = enderecoId,
                condicaoPagamento = condicaoPagamento,
                valorFrete = frete,
                valorTotal = valorFinal,
                totalItens = _totalItens.value
            )

            val pedidoId = pedidoRepository.inserir(pedido)

            _itensPedido.value.forEach { item ->
                pedidoItemRepository.inserir(item.copy(pedidoId = pedidoId.toInt()))
            }

            onSucesso(codigo)
            limparPedido()
        }
    }

    private fun limparPedido() {
        _itensPedido.value = emptyList()
        _valorTotal.value = 0.0
        _totalItens.value = 0
        _parcelas.value = emptyList()
    }

    fun buscarPorCodigo(codigo: String) {
        viewModelScope.launch {
            _pedidoEncontrado.value = pedidoRepository.buscarPorCodigo(codigo)
        }
    }
}

class PedidoViewModelFactory(
    private val pedidoRepository: PedidoRepository,
    private val pedidoItemRepository: PedidoItemRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PedidoViewModel(pedidoRepository, pedidoItemRepository) as T
    }
}