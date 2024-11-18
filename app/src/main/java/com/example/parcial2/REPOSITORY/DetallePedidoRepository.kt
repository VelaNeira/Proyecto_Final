package com.example.parcial2.repository

import com.example.parcial2.DAO.DetallePedidoDao
import com.example.parcial2.MODEL.DetallePedido
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DetallePedidoRepository(private val detallePedidoDao: DetallePedidoDao) {

    suspend fun insertDetallePedido(detallePedido: DetallePedido) {
        withContext(Dispatchers.IO) {
            detallePedidoDao.insertDetallePedido(detallePedido)
        }
    }

    suspend fun getDetallesByPedidoId(pedidoId: Long): List<DetallePedido> {
        return withContext(Dispatchers.IO) {
            detallePedidoDao.getDetallesByPedidoId(pedidoId)
        }
    }
    // Obtener los detalles de un pedido
    suspend fun obtenerDetallesPorPedido(idPedido: Long): List<DetallePedido> {
        return withContext(Dispatchers.IO) {
            detallePedidoDao.obtenerDetallesPorPedido(idPedido)
        }
    }

}
