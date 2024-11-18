package com.example.parcial2.MODEL

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "detalle_pedido", foreignKeys = [
    ForeignKey(entity = Pedido::class, parentColumns = ["id"], childColumns = ["pedidoId"], onDelete = ForeignKey.CASCADE)
])
data class DetallePedido(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val pedidoId: Long,  // Relación con Pedido
    val productoId: Long,
    val cantidad: Int,
    val precioUnitario: Double
)
