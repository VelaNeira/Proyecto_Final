package com.example.parcial2.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.parcial2.MODEL.Producto
import com.example.parcial2.database.AppDatabase
import com.example.parcial2.repository.ProductoRepository
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductoScreen() {
    val context = LocalContext.current
    val productoDao = AppDatabase.getDatabase(context).productoDao()
    val repository = ProductoRepository(productoDao)
    val scope = rememberCoroutineScope()

    var productos by remember { mutableStateOf<List<Producto>>(emptyList()) }
    var mostrarDialogoNuevoProducto by remember { mutableStateOf(false) }

    // Estados para el nuevo producto
    var nombreProducto by remember { mutableStateOf("") }
    var precioProducto by remember { mutableStateOf("") }
    var categoriaProducto by remember { mutableStateOf("Bebida") }

    val categorias = listOf("Bebida", "Comida")

    LaunchedEffect(Unit) {
        productos = repository.obtenerProductos()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Productos") },
                actions = {
                    IconButton(onClick = { mostrarDialogoNuevoProducto = true }) {
                        Text("+")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(productos) { producto ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                producto.nombre,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text("$${producto.precio}")
                            Text(
                                producto.categoria,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        IconButton(
                            onClick = {
                                scope.launch {
                                    repository.eliminarProducto(producto.id)
                                    productos = repository.obtenerProductos()
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

    if (mostrarDialogoNuevoProducto) {
        AlertDialog(
            onDismissRequest = {
                mostrarDialogoNuevoProducto = false
                nombreProducto = ""
                precioProducto = ""
                categoriaProducto = "Bebida"
            },
            title = { Text("Nuevo Producto") },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TextField(
                        value = nombreProducto,
                        onValueChange = { nombreProducto = it },
                        label = { Text("Nombre") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    TextField(
                        value = precioProducto,
                        onValueChange = { precioProducto = it },
                        label = { Text("Precio") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Column {
                        Text("Categoría:", modifier = Modifier.padding(vertical = 8.dp))
                        categorias.forEach { categoria ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = categoriaProducto == categoria,
                                    onClick = { categoriaProducto = categoria }
                                )
                                Text(
                                    text = categoria,
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        scope.launch {
                            if (nombreProducto.isNotBlank() && precioProducto.isNotBlank()) {
                                val precio = precioProducto.toDoubleOrNull()
                                if (precio != null) {
                                    val nuevoProducto = Producto(
                                        nombre = nombreProducto,
                                        precio = precio,
                                        categoria = categoriaProducto
                                    )
                                    repository.insertProducto(nuevoProducto)
                                    productos = repository.obtenerProductos()
                                    mostrarDialogoNuevoProducto = false
                                    nombreProducto = ""
                                    precioProducto = ""
                                    categoriaProducto = "Bebida"
                                }
                            }
                        }
                    }
                ) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        mostrarDialogoNuevoProducto = false
                        nombreProducto = ""
                        precioProducto = ""
                        categoriaProducto = "Bebida"
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}