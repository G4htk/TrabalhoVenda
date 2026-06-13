package com.example.trabalhovenda.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.trabalhovenda.telas.ClienteTela
import com.example.trabalhovenda.telas.EnderecoTela
import com.example.trabalhovenda.telas.ItemTela
import com.example.trabalhovenda.telas.MenuTela
import com.example.trabalhovenda.telas.PedidoTela

@Composable
fun NavGraph(context: Context) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "menu"
    ) {
        composable("menu") { MenuTela(navController) }
        composable("cliente") { ClienteTela(context) }
        composable("endereco") { EnderecoTela(context) }
        composable("item") { ItemTela(context) }
        composable("pedido") { PedidoTela(context) }
    }
}