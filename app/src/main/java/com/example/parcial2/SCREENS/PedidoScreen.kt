package com.example.parcial2.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.parcial2.MODEL.Pedido
import com.example.parcial2.database.AppDatabase
import com.example.parcial2.repository.PedidoRepository
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PedidoScreen() {
    // Inicializar repositorio
    val context = LocalContext.current
    val pedidoDao = AppDatabase.getDatabase(context).pedidoDao()
    val repository = PedidoRepository(pedidoDao)

    // Estado para la lista de pedidos
    var pedidos by remember { mutableStateOf<List<Pedido>>(emptyList()) }

    // Cargar pedidos
    LaunchedEffect(Unit) {
        pedidos = repository.getAllPedidos()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lista de Pedidos") }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            items(pedidos) { pedido ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Pedido #${pedido.id}",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Mesa: ${pedido.mesaId}",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = "Estado: ${pedido.estado}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "Fecha: ${formatearFecha(pedido.fecha)}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

private fun formatearFecha(timestamp: Long): String {
    val formato = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return formato.format(Date(timestamp))
}