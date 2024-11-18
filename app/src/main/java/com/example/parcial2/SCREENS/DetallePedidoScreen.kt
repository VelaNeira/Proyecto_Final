package com.example.parcial2.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.parcial2.MODEL.*
import com.example.parcial2.database.AppDatabase
import com.example.parcial2.repository.PedidoRepository
import com.example.parcial2.repository.ProductoRepository
import kotlinx.coroutines.launch

// Clase auxiliar para mantener el detalle junto con la info del producto
data class DetalleConProducto(
    val detalle: DetallePedido,
    val producto: Producto,
    val subtotal: Double = detalle.cantidad * detalle.precioUnitario
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetallePedidoScreen(
    mesaId: Int,
    navController: NavHostController
) {
    val context = LocalContext.current
    val database = AppDatabase.getDatabase(context)
    val pedidoRepository = PedidoRepository(database.pedidoDao())
    val productoRepository = ProductoRepository(database.productoDao())
    val scope = rememberCoroutineScope()

    var pedidos by remember { mutableStateOf<List<Pedido>>(emptyList()) }
    var mostrarDialogoProductos by remember { mutableStateOf(false) }
    var productos by remember { mutableStateOf<List<Producto>>(emptyList()) }
    var cantidades = remember { mutableStateMapOf<Long, Int>() }
    var detallesPedidos = remember { mutableStateMapOf<Long, List<DetalleConProducto>>() }
    var pedidoEditando by remember { mutableStateOf<Long?>(null) }

    val estados = listOf("Pendiente", "Preparando", "Listo")

    LaunchedEffect(mesaId) {
        pedidos = pedidoRepository.getPedidosPorMesa(mesaId)
        productos = productoRepository.obtenerProductos()
        pedidos.forEach { pedido ->
            val detalles = database.detallePedidoDao().getDetallesByPedidoId(pedido.id)
            val detallesConProductos = detalles.mapNotNull { detalle ->
                val producto = productos.find { it.id == detalle.productoId }
                producto?.let { DetalleConProducto(detalle, it) }
            }
            detallesPedidos[pedido.id] = detallesConProductos
        }
    }

    // Función para cargar cantidades de un pedido existente
    fun cargarCantidadesParaEditar(pedidoId: Long) {
        cantidades.clear()
        detallesPedidos[pedidoId]?.forEach { detalle ->
            cantidades[detalle.producto.id] = detalle.detalle.cantidad
        }
        pedidoEditando = pedidoId
        mostrarDialogoProductos = true
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pedidos - Mesa $mesaId") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Text("←")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(pedidos) { pedido ->
                    val detalles = detallesPedidos[pedido.id] ?: emptyList()
                    val total = detalles.sumOf { it.subtotal }

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            // Encabezado del pedido
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    "Pedido #${pedido.id}",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Row {
                                    // Botón Editar
                                    Button(
                                        onClick = { cargarCantidadesParaEditar(pedido.id) },
                                        modifier = Modifier.padding(end = 8.dp)
                                    ) {
                                        Text("Editar")
                                    }
                                    // Botón Eliminar
                                    Button(
                                        onClick = {
                                            scope.launch {
                                                pedidoRepository.eliminarPedido(pedido.id)
                                                pedidos = pedidoRepository.getPedidosPorMesa(mesaId)
                                            }
                                        }
                                    ) {
                                        Text("Eliminar")
                                    }
                                }
                            }

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                            ) {
                                Text("Estado: ", style = MaterialTheme.typography.bodyMedium)
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    estados.forEach { estado ->
                                        FilledTonalButton(
                                            onClick = {
                                                scope.launch {
                                                    val pedidoActualizado =
                                                        pedido.copy(estado = estado)
                                                    pedidoRepository.actualizarPedido(
                                                        pedidoActualizado
                                                    )
                                                    pedidos =
                                                        pedidoRepository.getPedidosPorMesa(mesaId)
                                                }
                                            },
                                            modifier = Modifier
                                                .weight(1f)
                                                .padding(horizontal = 1.dp),
                                            colors = ButtonDefaults.filledTonalButtonColors(
                                                containerColor = when {
                                                    pedido.estado == estado -> MaterialTheme.colorScheme.primary
                                                    else -> MaterialTheme.colorScheme.surfaceVariant
                                                }
                                            )
                                        ) {
                                            Text(
                                                estado,
                                                style = MaterialTheme.typography.bodySmall,  // Texto más pequeño
                                                maxLines = 1                                 // Una sola línea
                                            )
                                        }
                                    }
                                }
                            }


                            // Lista de productos y total (se mantiene igual)
                            val detalles = detallesPedidos[pedido.id] ?: emptyList()
                            val total = detalles.sumOf { it.subtotal }

                            detalles.forEach { detalle ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("${detalle.detalle.cantidad}x ${detalle.producto.nombre}")
                                    Text("$${detalle.subtotal}")
                                }
                            }

                            Divider(modifier = Modifier.padding(vertical = 8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    "Total:",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    "$${total}",
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }
                    }
                }
            }

            Button(
                onClick = {
                    cantidades.clear()
                    pedidoEditando = null
                    mostrarDialogoProductos = true
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Nuevo Pedido")
            }
        }


        if (mostrarDialogoProductos) {
            Dialog(onDismissRequest = {
                mostrarDialogoProductos = false
                pedidoEditando = null
            }) {
                Card(
                    modifier = Modifier
                        .width(300.dp)
                        .height(400.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxSize()
                    ) {
                        Text(
                            text = if (pedidoEditando != null) "Editar Pedido" else "Nuevo Pedido",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        Text(
                            text = "Seleccionar Productos",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(productos) { producto ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(producto.nombre)
                                        Text("$${producto.precio}")
                                    }
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        IconButton(
                                            onClick = {
                                                val cantidad = cantidades[producto.id] ?: 0
                                                if (cantidad > 0) {
                                                    cantidades[producto.id] = cantidad - 1
                                                }
                                            },
                                        ) { Text("-") }

                                        Text("${cantidades[producto.id] ?: 0}")

                                        IconButton(
                                            onClick = {
                                                val cantidad = cantidades[producto.id] ?: 0
                                                cantidades[producto.id] = cantidad + 1
                                            }
                                        ) { Text("+") }
                                    }
                                }
                            }
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Button(onClick = {
                                mostrarDialogoProductos = false
                                pedidoEditando = null
                            }) {
                                Text("Cancelar")
                            }
                            Button(
                                onClick = {
                                    scope.launch {
                                        val productosSeleccionados =
                                            cantidades.filter { it.value > 0 }
                                        if (productosSeleccionados.isNotEmpty()) {
                                            if (pedidoEditando != null) {
                                                // Si estamos editando, primero eliminamos los detalles antiguos
                                                database.detallePedidoDao()
                                                    .eliminarDetallesPedido(pedidoEditando!!)

                                                // Creamos los nuevos detalles
                                                val nuevosDetalles =
                                                    mutableListOf<DetalleConProducto>()
                                                productosSeleccionados.forEach { (productoId, cantidad) ->
                                                    val producto =
                                                        productos.find { it.id == productoId }
                                                    producto?.let {
                                                        val detalle = DetallePedido(
                                                            pedidoId = pedidoEditando!!,
                                                            productoId = productoId,
                                                            cantidad = cantidad,
                                                            precioUnitario = it.precio
                                                        )
                                                        database.detallePedidoDao()
                                                            .insertDetallePedido(detalle)
                                                        nuevosDetalles.add(
                                                            DetalleConProducto(
                                                                detalle,
                                                                producto
                                                            )
                                                        )
                                                    }
                                                }
                                                detallesPedidos[pedidoEditando!!] = nuevosDetalles

                                            } else {
                                                // Lógica existente para nuevo pedido
                                                val nuevoPedido = Pedido(
                                                    mesaId = mesaId,
                                                    estado = "Pendiente",
                                                    fecha = System.currentTimeMillis()
                                                )
                                                val pedidoId =
                                                    pedidoRepository.crearPedido(nuevoPedido)
                                                val nuevosDetalles =
                                                    mutableListOf<DetalleConProducto>()
                                                productosSeleccionados.forEach { (productoId, cantidad) ->
                                                    val producto =
                                                        productos.find { it.id == productoId }
                                                    producto?.let {
                                                        val detalle = DetallePedido(
                                                            pedidoId = pedidoId,
                                                            productoId = productoId,
                                                            cantidad = cantidad,
                                                            precioUnitario = it.precio
                                                        )
                                                        database.detallePedidoDao()
                                                            .insertDetallePedido(detalle)
                                                        nuevosDetalles.add(
                                                            DetalleConProducto(
                                                                detalle,
                                                                producto
                                                            )
                                                        )
                                                    }
                                                }
                                                detallesPedidos[pedidoId] = nuevosDetalles
                                            }

                                            pedidos = pedidoRepository.getPedidosPorMesa(mesaId)
                                            mostrarDialogoProductos = false
                                            pedidoEditando = null
                                        }
                                    }
                                }
                            ) {
                                Text(if (pedidoEditando != null) "Actualizar" else "Guardar")
                            }
                        }
                    }
                }
            }
        }
    }

}