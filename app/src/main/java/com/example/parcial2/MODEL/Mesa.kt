package com.example.parcial2.MODEL

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mesa")
data class Mesa(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val estado: String // Campo para representar el estado de la mesa (Libre, Ocupada, etc.)
)
