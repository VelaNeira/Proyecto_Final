package com.example.parcial2.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.parcial2.MODEL.DetallePedido

@Dao
interface DetallePedidoDao {

    @Insert
    suspend fun insertDetallePedido(detallePedido: DetallePedido)

    @Query("SELECT * FROM detalle_pedido WHERE pedidoId = :pedidoId")
    suspend fun getDetallesByPedidoId(pedidoId: Long): List<DetallePedido>

    @Query("SELECT * FROM detalle_pedido WHERE pedidoId = :idPedido")
    suspend fun obtenerDetallesPorPedido(idPedido: Int): List<DetallePedido>

    @Query("SELECT * FROM detalle_pedido WHERE pedidoId = :idPedido")
    suspend fun obtenerDetallesPorPedido(idPedido: Long): List<DetallePedido>

    @Query("DELETE FROM detalle_pedido WHERE pedidoId = :pedidoId")
    suspend fun eliminarDetallesPedido(pedidoId: Long)


}
