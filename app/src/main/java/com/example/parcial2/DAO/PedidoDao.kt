package com.example.parcial2.DAO

import androidx.room.*
import com.example.parcial2.MODEL.Pedido

@Dao
interface PedidoDao {
    // Obtener todos los pedidos
    @Query("SELECT * FROM pedido")
    suspend fun getAllPedidos(): List<Pedido>

    // Obtener pedidos por mesa
    @Query("SELECT * FROM pedido WHERE mesaId = :mesaId")
    suspend fun getPedidosPorMesa(mesaId: Int): List<Pedido>

    // Insertar pedido
    @Insert
    suspend fun insertPedido(pedido: Pedido): Long

    // Actualizar pedido
    @Update
    suspend fun updatePedido(pedido: Pedido)

    // Eliminar pedido
    @Query("DELETE FROM pedido WHERE id = :pedidoId")
    suspend fun deletePedido(pedidoId: Long)

    // Obtener pedido por ID
    @Query("SELECT * FROM pedido WHERE id = :pedidoId")
    suspend fun getPedidoById(pedidoId: Long): Pedido?
}