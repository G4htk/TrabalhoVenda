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
import androidx.compose.foundation.text.KeyboardOptions
import com.example.trabalhovenda.database.DatabaseProvider
import com.example.trabalhovenda.repository.ItemRepository
import com.example.trabalhovenda.viewModel.ItemViewModel
import com.example.trabalhovenda.viewModel.ItemViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemTela(context: Context) {
    val db = DatabaseProvider.getDatabase(context)
    val repository = ItemRepository(db.itemDao())
    val viewModel: ItemViewModel = viewModel(factory = ItemViewModelFactory(repository))

    val itens by viewModel.itens.collectAsState(initial = emptyList())

    var codigo by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }
    var valor by remember { mutableStateOf("") }
    var unMedia by remember { mutableStateOf("") }
    var erro by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Cadastro de Item",
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
            value = descricao,
            onValueChange = { descricao = it },
            label = { Text("Descrição") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = valor,
            onValueChange = { valor = it },
            label = { Text("Valor") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = unMedia,
            onValueChange = { unMedia = it.uppercase() },
            label = { Text("Unidade de Medida (ex: UN, KG, CX)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        if (erro.isNotBlank()) {
            Text(text = erro, color = MaterialTheme.colorScheme.error)
            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(
            onClick = {
                val sucesso = viewModel.adicionarItem(
                    codigo = codigo.toIntOrNull() ?: 0,
                    descricao = descricao,
                    valor = valor.toDoubleOrNull() ?: 0.0,
                    unMedia = unMedia
                )
                if (sucesso) {
                    codigo = ""
                    descricao = ""
                    valor = ""
                    unMedia = ""
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
            text = "Itens Cadastrados",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        itens.forEach { item ->
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
                        Text(text = item.descricao, style = MaterialTheme.typography.bodyLarge)
                        Text(text = "Valor: R$ ${item.valor}", style = MaterialTheme.typography.bodySmall)
                        Text(text = "Unidade: ${item.unMedia}", style = MaterialTheme.typography.bodySmall)
                    }
                    IconButton(onClick = { viewModel.deletarItem(item) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Deletar")
                    }
                }
            }
        }
    }
}