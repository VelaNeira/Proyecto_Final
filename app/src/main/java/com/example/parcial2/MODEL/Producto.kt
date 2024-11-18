package com.example.parcial2.MODEL

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "producto")
data class Producto(
    @PrimaryKey(autoGenerate = true) val id: Long = 0, // id único
    val nombre: String,
    val precio: Double,
    val categoria: String // Esta es la categoría por la que buscas
)
