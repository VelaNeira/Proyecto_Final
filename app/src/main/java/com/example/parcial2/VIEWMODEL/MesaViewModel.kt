// MesaViewModel.kt
package com.example.parcial2.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.parcial2.MODEL.Mesa
import com.example.parcial2.MODEL.Pedido
import com.example.parcial2.database.AppDatabase
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MesaViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val mesaDao = database.mesaDao()
    private val pedidoDao = database.pedidoDao()

    // Para mesas
    suspend fun getAllMesas(): List<Mesa> {
        return mesaDao.getAllMesas()
    }

    suspend fun actualizarEstadoMesa(mesaId: Int, nuevoEstado: String) {
        val mesa = mesaDao.getMesaById(mesaId)
        mesa?.let {
            val mesaActualizada = it.copy(estado = nuevoEstado)
            mesaDao.actualizarMesa(mesaActualizada)
        }
    }

    // Para pedidos
    suspend fun crearPedido(mesaId: Int) {
        val nuevoPedido = Pedido(
            mesaId = mesaId,
            estado = "Pendiente",
            fecha = System.currentTimeMillis()
        )
        pedidoDao.insertPedido(nuevoPedido)
    }

    suspend fun getPedidosPorMesa(mesaId: Int): List<Pedido> {
        return pedidoDao.getPedidosPorMesa(mesaId)
    }

    suspend fun eliminarPedido(pedidoId: Long) {
        pedidoDao.deletePedido(pedidoId)
    }
}