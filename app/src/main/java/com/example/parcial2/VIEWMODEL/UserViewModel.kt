package com.example.parcial2.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class UserViewModel {
    var rol by mutableStateOf<String?>(null)
        private set

    fun updateRol(nuevoRol: String) {
        rol = nuevoRol
    }

    fun clearRol() {
        rol = null
    }

    fun isAdmin() = rol == "ADMIN"

    fun canAccessRoute(route: String): Boolean {
        return when (route) {
            "login", "main_menu" -> true
            "mesa", "pedido", "detalle_pedido" -> true  // Todos pueden acceder
            "producto" -> isAdmin()  // Solo admin puede acceder
            else -> false
        }
    }
}