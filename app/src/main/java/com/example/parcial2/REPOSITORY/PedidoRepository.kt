package com.example.parcial2.repository

import com.example.parcial2.DAO.PedidoDao
import com.example.parcial2.MODEL.Pedido
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PedidoRepository(private val pedidoDao: PedidoDao) {
    // Get All Pedidos
    suspend fun getAllPedidos(): List<Pedido> = withContext(Dispatchers.IO) {
        pedidoDao.getAllPedidos()
    }

    // Create
    suspend fun crearPedido(pedido: Pedido) = withContext(Dispatchers.IO) {
        pedidoDao.insertPedido(pedido)
    }

    // Read - Obtener pedidos por mesa
    suspend fun getPedidosPorMesa(mesaId: Int): List<Pedido> = withContext(Dispatchers.IO) {
        pedidoDao.getPedidosPorMesa(mesaId)
    }

    // Update
    suspend fun actualizarPedido(pedido: Pedido) = withContext(Dispatchers.IO) {
        pedidoDao.updatePedido(pedido)
    }

    // Delete
    suspend fun eliminarPedido(pedidoId: Long) = withContext(Dispatchers.IO) {
        pedidoDao.deletePedido(pedidoId)
    }

    // Método auxiliar para verificar si una mesa tiene pedidos
    suspend fun tienePedidosActivos(mesaId: Int): Boolean = withContext(Dispatchers.IO) {
        pedidoDao.getPedidosPorMesa(mesaId).isNotEmpty()
    }

    // Método para obtener el total de pedidos de una mesa
    suspend fun contarPedidosPorMesa(mesaId: Int): Int = withContext(Dispatchers.IO) {
        pedidoDao.getPedidosPorMesa(mesaId).size
    }
}