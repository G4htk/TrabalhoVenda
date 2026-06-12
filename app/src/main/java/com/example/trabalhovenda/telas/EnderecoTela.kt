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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.trabalhovenda.database.DatabaseProvider
import com.example.trabalhovenda.repository.EnderecoRepository
import com.example.trabalhovenda.viewModel.EnderecoViewModel
import com.example.trabalhovenda.viewModel.EnderecoViewModelFactory
import androidx.compose.foundation.text.KeyboardOptions

@Composable
fun EnderecoTela(context: Context) {
    val db = DatabaseProvider.getDatabase(context)
    val repository = EnderecoRepository(db.enderecoDao())
    val viewModel: EnderecoViewModel = viewModel(factory = EnderecoViewModelFactory(repository))
    val enderecos by viewModel.enderecos.collectAsState(initial = emptyList())

    var codigo by remember { mutableStateOf("") }
    var nome by remember { mutableStateOf("") }
    var logradouro by remember { mutableStateOf("") }
    var numero by remember { mutableStateOf("") }
    var bairro by remember { mutableStateOf("") }
    var cidade by remember { mutableStateOf("") }
    var uf by remember { mutableStateOf("") }
    var erro by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Cadastro de Endereço",
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
            value = logradouro,
            onValueChange = { logradouro = it },
            label = { Text("Logradouro") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = numero,
            onValueChange = { numero = it },
            label = { Text("Número") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = bairro,
            onValueChange = { bairro = it },
            label = { Text("Bairro") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = cidade,
            onValueChange = { cidade = it },
            label = { Text("Cidade") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = uf,
            onValueChange = { if (it.length <= 2) uf = it.uppercase() },
            label = { Text("UF") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        if (erro.isNotBlank()) {
            Text(text = erro, color = MaterialTheme.colorScheme.error)
            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(
            onClick = {
                val sucesso = viewModel.adicionarEndereco(
                    codigo = codigo.toIntOrNull() ?: 0,
                    nome = nome,
                    logradouro = logradouro,
                    numero = numero.toIntOrNull() ?: 0,
                    bairro = bairro,
                    cidade = cidade,
                    uf = uf
                )
                if (sucesso) {
                    codigo = ""
                    nome = ""
                    logradouro = ""
                    numero = ""
                    bairro = ""
                    cidade = ""
                    uf = ""
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
            text = "Endereços Cadastrados",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        enderecos.forEach { endereco ->
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
                        Text(text = endereco.nome, style = MaterialTheme.typography.bodyLarge)
                        Text(
                            text = "${endereco.logradouro}, ${endereco.numero} - ${endereco.bairro}",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            text = "${endereco.cidade} - ${endereco.uf}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    IconButton(onClick = { viewModel.deletarEndereco(endereco) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Deletar")
                    }
                }
            }
        }
    }
}