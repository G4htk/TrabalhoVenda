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
import com.example.foradevenda.entity.EnderecoEntity
import com.example.trabalhovenda.database.DatabaseProvider
import com.example.trabalhovenda.repository.ClienteRepository
import com.example.trabalhovenda.repository.EnderecoRepository
import com.example.trabalhovenda.viewModel.ClienteViewModel
import com.example.trabalhovenda.viewModel.ClienteViewModelFactory
import com.example.trabalhovenda.viewModel.EnderecoViewModel
import com.example.trabalhovenda.viewModel.EnderecoViewModelFactory
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClienteScreen(context: Context) {
    val db = DatabaseProvider.getDatabase(context)
    val clienteRepository = ClienteRepository(db.clienteDao())
    val enderecoRepository = EnderecoRepository(db.enderecoDao())
    val viewModel: ClienteViewModel = viewModel(factory = ClienteViewModelFactory(clienteRepository))
    val enderecoViewModel: EnderecoViewModel = viewModel(factory = EnderecoViewModelFactory(
        enderecoRepository
    )
    )

    val clientes by viewModel.clientes.collectAsState(initial = emptyList())
    val enderecos by enderecoViewModel.enderecos.collectAsState(initial = emptyList())

    var codigo by remember { mutableStateOf("") }
    var nome by remember { mutableStateOf("") }
    var cpf by remember { mutableStateOf("") }
    var dataNac by remember { mutableStateOf("") }
    var enderecoSelecionado by remember { mutableStateOf<EnderecoEntity?>(null) }
    var dropdownExpanded by remember { mutableStateOf(false) }
    var erro by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Cadastro de Cliente",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = codigo,
            onValueChange = { codigo = it },
            label = { Text("Código") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = nome,
            onValueChange = { nome = it },
            label = { Text("Nome") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = cpf,
            onValueChange = { cpf = it },
            label = { Text("CPF") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = dataNac,
            onValueChange = { dataNac = it },
            label = { Text("Data de Nascimento (dd/MM/yyyy)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        ExposedDropdownMenuBox(
            expanded = dropdownExpanded,
            onExpandedChange = { dropdownExpanded = !dropdownExpanded }
        ) {
            OutlinedTextField(
                value = enderecoSelecionado?.nome ?: "Selecione um endereço",
                onValueChange = {},
                readOnly = true,
                label = { Text("Endereço") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownExpanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = dropdownExpanded,
                onDismissRequest = { dropdownExpanded = false }
            ) {
                enderecos.forEach { endereco ->
                    DropdownMenuItem(
                        text = { Text("${endereco.nome} - ${endereco.cidade}") },
                        onClick = {
                            enderecoSelecionado = endereco
                            dropdownExpanded = false
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        if (erro.isNotBlank()) {
            Text(text = erro, color = MaterialTheme.colorScheme.error)
            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(
            onClick = {
                val data = try {
                    val partes = dataNac.split("/")
                    LocalDate.of(partes[2].toInt(), partes[1].toInt(), partes[0].toInt())
                } catch (e: Exception) {
                    null
                }

                if (data == null) {
                    erro = "Data inválida. Use o formato dd/MM/yyyy"
                    return@Button
                }

                if (enderecoSelecionado == null) {
                    erro = "Selecione um endereço"
                    return@Button
                }

                val sucesso = viewModel.adicionarCliente(
                    codigo = codigo.toIntOrNull() ?: 0,
                    nome = nome,
                    cpf = cpf,
                    dataNac = data,
                    enderecoId = enderecoSelecionado!!.id
                )

                if (sucesso) {
                    codigo = ""
                    nome = ""
                    cpf = ""
                    dataNac = ""
                    enderecoSelecionado = null
                    erro = ""
                } else {
                    erro = "Preencha todos os campos corretamente."
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Salvar")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Clientes Cadastrados",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        clientes.forEach { cliente ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = cliente.nome, style = MaterialTheme.typography.bodyLarge)
                        Text(text = "CPF: ${cliente.cpf}", style = MaterialTheme.typography.bodySmall)
                        Text(text = "Nascimento: ${cliente.dataNac}", style = MaterialTheme.typography.bodySmall)
                    }
                    IconButton(onClick = { viewModel.deletarCliente(cliente) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Deletar")
                    }
                }
            }
        }
    }
}