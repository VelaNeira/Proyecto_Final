package com.example.parcial2.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.parcial2.MODEL.Mesa
import com.example.parcial2.database.AppDatabase
import com.example.parcial2.repository.MesaRepository
import com.example.parcial2.repository.PedidoRepository
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MesaScreen(navController: NavHostController) {
    val context = LocalContext.current
    val database = AppDatabase.getDatabase(context)
    val mesaDao = database.mesaDao()
    val repository = MesaRepository(mesaDao)
    val pedidoRepository = PedidoRepository(database.pedidoDao())
    val scope = rememberCoroutineScope()

    var mesas by remember { mutableStateOf<List<Mesa>>(emptyList()) }
    var mesasConPedidos by remember { mutableStateOf<Set<Int>>(setOf()) }
    var mostrarDialogoNuevaMesa by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        mesas = repository.obtenerMesas()
        mesasConPedidos = mesas.filter { mesa ->
            pedidoRepository.tienePedidosActivos(mesa.id)
        }.map { it.id }.toSet()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mesas") },
                actions = {
                    IconButton(
                        onClick = { mostrarDialogoNuevaMesa = true },
                        enabled = mesas.size < 15 // Limitamos a 15 mesas máximo
                    ) {
                        Text("+")
                    }
                }
            )
        }
    ) { padding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(8.dp),
            modifier = Modifier.padding(padding)
        ) {
            items(mesas) { mesa ->
                val tienePedidos = mesasConPedidos.contains(mesa.id)
                Card(
                    modifier = Modifier
                        .padding(4.dp)
                        .aspectRatio(1f),
                    colors = CardDefaults.cardColors(
                        containerColor = when {
                            tienePedidos -> Color(0xFFFFA500) // Naranja
                            else -> Color.Green
                        }
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            mesa.nombre,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(if (tienePedidos) "Ocupada" else "Libre")

                        // Botones
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            // Botón Ver Pedidos
                            IconButton(
                                onClick = { navController.navigate("detalle_pedido/${mesa.id}") }
                            ) {
                                Text("👁")
                            }

                            // Botón Eliminar (solo si no hay pedidos y hay más de 9 mesas)
                            if (!tienePedidos && mesas.size > 9) {
                                IconButton(
                                    onClick = {
                                        scope.launch {
                                            repository.eliminarMesa(mesa.id)
                                            mesas = repository.obtenerMesas()
                                        }
                                    }
                                ) {
                                    Text("🗑")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (mostrarDialogoNuevaMesa) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoNuevaMesa = false },
            title = { Text("Nueva Mesa") },
            text = {
                Text("¿Desea agregar una nueva mesa?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        scope.launch {
                            val nuevaMesa = Mesa(
                                nombre = "Mesa ${mesas.size + 1}",
                                estado = "Libre"
                            )
                            repository.insertMesa(nuevaMesa)
                            mesas = repository.obtenerMesas()
                            mostrarDialogoNuevaMesa = false
                        }
                    }
                ) {
                    Text("Agregar")
                }
            },
            dismissButton = {
                Button(onClick = { mostrarDialogoNuevaMesa = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}