package com.example.trabalhovenda.telas

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.Search
import com.example.trabalhovenda.database.DatabaseProvider
import com.example.trabalhovenda.entity.ClienteEntity
import com.example.trabalhovenda.entity.EnderecoEntity
import com.example.trabalhovenda.entity.ItemEntity
import com.example.trabalhovenda.repository.ClienteRepository
import com.example.trabalhovenda.repository.EnderecoRepository
import com.example.trabalhovenda.repository.ItemRepository
import com.example.trabalhovenda.repository.PedidoRepository
import com.example.trabalhovenda.repository.PedidoItemRepository
import com.example.trabalhovenda.viewModel.ClienteViewModel
import com.example.trabalhovenda.viewModel.ClienteViewModelFactory
import com.example.trabalhovenda.viewModel.EnderecoViewModel
import com.example.trabalhovenda.viewModel.EnderecoViewModelFactory
import com.example.trabalhovenda.viewModel.ItemViewModel
import com.example.trabalhovenda.viewModel.ItemViewModelFactory
import com.example.trabalhovenda.viewModel.PedidoViewModel
import com.example.trabalhovenda.viewModel.PedidoViewModelFactory
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PedidoTela(context: Context) {
    val db = DatabaseProvider.getDatabase(context)
    val pedidoRepository = PedidoRepository(db.pedidoDao())
    val pedidoItemRepository = PedidoItemRepository(db.pedidoItemDao())
    val clienteRepository = ClienteRepository(db.clienteDao())
    val enderecoRepository = EnderecoRepository(db.enderecoDao())
    val itemRepository = ItemRepository(db.itemDao())

    val viewModel: PedidoViewModel = viewModel(factory = PedidoViewModelFactory(pedidoRepository, pedidoItemRepository))
    val clienteViewModel: ClienteViewModel = viewModel(factory = ClienteViewModelFactory(clienteRepository))
    val enderecoViewModel: EnderecoViewModel = viewModel(factory = EnderecoViewModelFactory(enderecoRepository))
    val itemViewModel: ItemViewModel = viewModel(factory = ItemViewModelFactory(itemRepository))

    val clientes by clienteViewModel.clientes.collectAsState(initial = emptyList())
    val enderecos by enderecoViewModel.enderecos.collectAsState(initial = emptyList())
    val itens by itemViewModel.itens.collectAsState(initial = emptyList())
    val itensPedido by viewModel.itensPedido.collectAsState()
    val valorTotal by viewModel.valorTotal.collectAsState()
    val totalItens by viewModel.totalItens.collectAsState()
    val parcelas by viewModel.parcelas.collectAsState()
    val pedidoEncontrado by viewModel.pedidoEncontrado.collectAsState()

    var clienteSelecionado by remember { mutableStateOf<ClienteEntity?>(null) }
    var enderecoSelecionado by remember { mutableStateOf<EnderecoEntity?>(null) }
    var itemSelecionado by remember { mutableStateOf<ItemEntity?>(null) }
    var quantidade by remember { mutableStateOf("") }
    var condicaoPagamento by remember { mutableStateOf("avista") }
    var numeroParcelas by remember { mutableStateOf("") }
    var erro by remember { mutableStateOf("") }
    var codigoBusca by remember { mutableStateOf("") }

    var clienteExpanded by remember { mutableStateOf(false) }
    var enderecoExpanded by remember { mutableStateOf(false) }
    var itemExpanded by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Pedido de Venda",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = codigoBusca,
                onValueChange = { codigoBusca = it },
                label = { Text("Buscar pedido por código") },
                trailingIcon = {
                    IconButton(onClick = { viewModel.buscarPorCodigo(codigoBusca) }) {
                        Icon(Icons.Default.Search, contentDescription = "Buscar")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            pedidoEncontrado?.let {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(text = "Pedido: ${it.codigo}", style = MaterialTheme.typography.titleMedium)
                        Text(text = "Cliente ID: ${it.clienteId}", style = MaterialTheme.typography.bodyMedium)
                        Text(text = "Pagamento: ${it.condicaoPagamento}", style = MaterialTheme.typography.bodyMedium)
                        Text(text = "Frete: R$ ${"%.2f".format(it.valorFrete)}", style = MaterialTheme.typography.bodyMedium)
                        Text(text = "Total: R$ ${"%.2f".format(it.valorTotal)}", style = MaterialTheme.typography.bodyMedium)
                        Text(text = "Total Itens: ${it.totalItens}", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            ExposedDropdownMenuBox(
                expanded = clienteExpanded,
                onExpandedChange = { clienteExpanded = !clienteExpanded }
            ) {
                OutlinedTextField(
                    value = clienteSelecionado?.nome ?: "Selecione um cliente",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Cliente") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = clienteExpanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = clienteExpanded,
                    onDismissRequest = { clienteExpanded = false }
                ) {
                    clientes.forEach { cliente ->
                        DropdownMenuItem(
                            text = { Text(cliente.nome) },
                            onClick = {
                                clienteSelecionado = cliente
                                clienteExpanded = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            ExposedDropdownMenuBox(
                expanded = itemExpanded,
                onExpandedChange = { itemExpanded = !itemExpanded }
            ) {
                OutlinedTextField(
                    value = itemSelecionado?.descricao ?: "Selecione um item",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Item") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = itemExpanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = itemExpanded,
                    onDismissRequest = { itemExpanded = false }
                ) {
                    itens.forEach { item ->
                        DropdownMenuItem(
                            text = { Text("${item.descricao} - R$ ${item.valor} - ${item.unMedia}") },
                            onClick = {
                                itemSelecionado = item
                                itemExpanded = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            itemSelecionado?.let {
                Text(text = "Valor unitário: R$ ${it.valor}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Unidade: ${it.unMedia}", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
            }

            OutlinedTextField(
                value = quantidade,
                onValueChange = { quantidade = it },
                label = { Text("Quantidade") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    if (itemSelecionado == null) {
                        erro = "Selecione um item"
                        return@Button
                    }
                    val qtd = quantidade.toIntOrNull() ?: 0
                    if (qtd <= 0) {
                        erro = "Quantidade inválida"
                        return@Button
                    }
                    viewModel.adicionarItem(
                        itemId = itemSelecionado!!.id,
                        quantidade = qtd,
                        valorUnitario = itemSelecionado!!.valor
                    )
                    itemSelecionado = null
                    quantidade = ""
                    erro = ""
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Adicionar Item")
            }
            Spacer(modifier = Modifier.height(8.dp))

            if (itensPedido.isNotEmpty()) {
                Text(
                    text = "Itens do Pedido",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                itensPedido.forEach { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(text = "Item ID: ${item.itemId}", style = MaterialTheme.typography.bodyMedium)
                                Text(text = "Qtd: ${item.quantidade}", style = MaterialTheme.typography.bodySmall)
                                Text(text = "Valor: R$ ${item.valorTotal}", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Total de itens: $totalItens", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Valor total: R$ ${"%.2f".format(valorTotal)}", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
            }

            Text(text = "Condição de Pagamento", style = MaterialTheme.typography.titleMedium)
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = condicaoPagamento == "avista",
                    onClick = { condicaoPagamento = "avista" }
                )
                Text(text = "À Vista")
                Spacer(modifier = Modifier.width(16.dp))
                RadioButton(
                    selected = condicaoPagamento == "aprazo",
                    onClick = { condicaoPagamento = "aprazo" }
                )
                Text(text = "À Prazo")
            }
            Spacer(modifier = Modifier.height(8.dp))

            if (condicaoPagamento == "aprazo") {
                OutlinedTextField(
                    value = numeroParcelas,
                    onValueChange = {
                        numeroParcelas = it
                        val n = it.toIntOrNull() ?: 0
                        if (n > 0) {
                            val frete = calcularFrete(enderecoSelecionado)
                            val valorFinal = viewModel.calcularValorFinal(condicaoPagamento, frete)
                            viewModel.calcularParcelas(n, valorFinal)
                        }
                    },
                    label = { Text("Número de Parcelas") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                if (parcelas.isNotEmpty()) {
                    Text(text = "Parcelas:", style = MaterialTheme.typography.titleSmall)
                    parcelas.forEachIndexed { index, valor ->
                        Text(text = "${index + 1}x - R$ ${"%.2f".format(valor)}", style = MaterialTheme.typography.bodySmall)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            ExposedDropdownMenuBox(
                expanded = enderecoExpanded,
                onExpandedChange = { enderecoExpanded = !enderecoExpanded }
            ) {
                OutlinedTextField(
                    value = enderecoSelecionado?.nome ?: "Selecione endereço de entrega",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Endereço de Entrega") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = enderecoExpanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = enderecoExpanded,
                    onDismissRequest = { enderecoExpanded = false }
                ) {
                    enderecos.forEach { endereco ->
                        DropdownMenuItem(
                            text = { Text("${endereco.nome} - ${endereco.cidade}/${endereco.uf}") },
                            onClick = {
                                enderecoSelecionado = endereco
                                enderecoExpanded = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            val frete = calcularFrete(enderecoSelecionado)
            val valorFinal = viewModel.calcularValorFinal(condicaoPagamento, frete)

            if (itensPedido.isNotEmpty()) {
                Text(text = "Frete: R$ ${"%.2f".format(frete)}", style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = if (condicaoPagamento == "avista") "Desconto (5%): -R$ ${"%.2f".format(valorTotal * 0.05)}"
                    else "Acréscimo (5%): +R$ ${"%.2f".format(valorTotal * 0.05)}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Valor Final: R$ ${"%.2f".format(valorFinal)}",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (erro.isNotBlank()) {
                Text(text = erro, color = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.height(8.dp))
            }

            Button(
                onClick = {
                    if (clienteSelecionado == null) {
                        erro = "Selecione um cliente"
                        return@Button
                    }
                    if (enderecoSelecionado == null) {
                        erro = "Selecione um endereço de entrega"
                        return@Button
                    }
                    if (itensPedido.isEmpty()) {
                        erro = "Adicione pelo menos um item"
                        return@Button
                    }

                    viewModel.concluirPedido(
                        clienteId = clienteSelecionado!!.id,
                        enderecoId = enderecoSelecionado!!.id,
                        condicaoPagamento = condicaoPagamento,
                        frete = frete,
                        valorFinal = valorFinal,
                        onSucesso = { codigo ->
                            scope.launch {
                                snackbarHostState.showSnackbar("Pedido $codigo cadastrado com sucesso!")
                            }
                        }
                    )
                    clienteSelecionado = null
                    enderecoSelecionado = null
                    itemSelecionado = null
                    quantidade = ""
                    condicaoPagamento = "avista"
                    numeroParcelas = ""
                    erro = ""
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Concluir Pedido")
            }
        }
    }
}

fun calcularFrete(endereco: EnderecoEntity?): Double {
    if (endereco == null) return 0.0
    return when {
        endereco.uf != "PR" -> 50.0
        endereco.cidade != "Toledo" -> 20.0
        else -> 0.0
    }
}