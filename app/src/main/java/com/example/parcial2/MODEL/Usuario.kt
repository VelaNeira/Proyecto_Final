package com.example.parcial2.MODEL

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuario")
data class Usuario(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val username: String,
    val password: String,
    val rol: String  // "ADMIN" o "MESERO"
)