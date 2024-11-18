package com.example.parcial2.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.parcial2.MODEL.Pedido
import com.example.parcial2.database.AppDatabase
import com.example.parcial2.repository.PedidoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PedidoViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PedidoRepository
    private val _pedidos = MutableStateFlow<List<Pedido>>(emptyList())
    val pedidos: StateFlow<List<Pedido>> = _pedidos

    init {
        val database = AppDatabase.getDatabase(application)
        repository = PedidoRepository(database.pedidoDao())
        cargarPedidos()
    }

    private fun cargarPedidos() {
        viewModelScope.launch {
            _pedidos.value = repository.getAllPedidos()
        }
    }

    suspend fun getAllPedidos(): List<Pedido> {
        return repository.getAllPedidos()
    }

    suspend fun crearPedido(mesaId: Int) {
        val nuevoPedido = Pedido(
            mesaId = mesaId,
            estado = "Pendiente",
            id = TODO(),
            fecha = TODO()
        )
        repository.crearPedido(nuevoPedido)
        cargarPedidos() // Recargar la lista después de crear
    }

    suspend fun actualizarPedido(pedido: Pedido) {
        repository.actualizarPedido(pedido)
        cargarPedidos() // Recargar la lista después de actualizar
    }

    suspend fun eliminarPedido(pedidoId: Long) {
        repository.eliminarPedido(pedidoId)
        cargarPedidos() // Recargar la lista después de eliminar
    }

    suspend fun getPedidosPorMesa(mesaId: Int): List<Pedido> {
        return repository.getPedidosPorMesa(mesaId)
    }
}