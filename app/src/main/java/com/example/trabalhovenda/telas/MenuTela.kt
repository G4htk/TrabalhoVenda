package com.example.trabalhovenda.telas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun MenuTela(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Força de Vendas",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Button(
            onClick = { navController.navigate("endereco") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        ) {
            Text("Endereços")
        }

        Button(
            onClick = { navController.navigate("cliente") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        ) {
            Text("Clientes")
        }

        Button(
            onClick = { navController.navigate("item") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        ) {
            Text("Itens")
        }

        Button(
            onClick = { navController.navigate("pedido") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        ) {
            Text("Pedidos")
        }
    }
}