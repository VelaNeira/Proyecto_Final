// Pedido.kt
package com.example.parcial2.MODEL

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "pedido",
    foreignKeys = [
        ForeignKey(
            entity = Mesa::class,
            parentColumns = ["id"],
            childColumns = ["mesaId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Pedido(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val mesaId: Int,
    val estado: String,
    val fecha: Long
)