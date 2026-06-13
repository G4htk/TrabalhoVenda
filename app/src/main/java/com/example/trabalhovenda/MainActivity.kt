package com.example.trabalhovenda

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.trabalhovenda.navigation.NavGraph
import com.example.trabalhovenda.ui.theme.TrabalhoVendaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TrabalhoVendaTheme {
                NavGraph(context = this)
            }
        }

    }
}